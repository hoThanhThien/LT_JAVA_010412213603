package com.example.laundry.security;

import com.example.laundry.services.impl.TokenBlackListServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class  JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;
  private final TokenBlackListServiceImpl tokenBlackListServiceImpl;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, TokenBlackListServiceImpl tokenBlackListServiceImpl) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
    this.tokenBlackListServiceImpl = tokenBlackListServiceImpl;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    // Get authorization header
    final String authorizationHeader = request.getHeader("Authorization");

    String username = null;
    String token = null;

    logger.debug("Authorization header: " + authorizationHeader);

    // Check if the header is present and has the correct format
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      token = authorizationHeader.substring(7);
      logger.debug("Extracted token: " + token);

      // Kiểm tra blacklist sau khi đã trích xuất token
      if(tokenBlackListServiceImpl.isBlacklisted(token)) {
        logger.debug("Token is blacklisted: " + token);
        filterChain.doFilter(request, response);
        return;
      }

      try {
        username = jwtUtil.validateTokenAndRetrieveSubject(token);
        logger.debug("Username from token: " + username);
      } catch (JwtException e) {
        logger.error("JWT token validation failed: " + e.getMessage());
      }
    }

    // If we have a username and no authentication exists in the context
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      // Create authentication token and set it in the context
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    filterChain.doFilter(request, response);
  }
}
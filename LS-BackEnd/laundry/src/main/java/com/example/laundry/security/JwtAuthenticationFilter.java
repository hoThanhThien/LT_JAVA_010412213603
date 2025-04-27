package com.example.laundry.security;

import com.example.laundry.services.impl.TokenBlackListServiceImpl;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

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
      if (tokenBlackListServiceImpl.isBlacklisted(token)) {
        logger.debug("Token is blacklisted: " + token);
        filterChain.doFilter(request, response);
        return;
      }

      try {
        username = jwtUtil.validateTokenAndRetrieveSubject(token);
        logger.debug("Username from token: " + username);
      } catch (JwtException e) {
        logger.error("JWT token validation failed: " + e.getMessage());

        // Trả về lỗi 401 ngay tại đây
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"Token không hợp lệ hoặc đã hết hạn\"}");
        return;
      }

    }

    // Nếu có người dùng nhưng chưa được xác thực
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      // Load người dùng từ database
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);

      // Lấy role từ token
      String role = jwtUtil.extractRole(token);
      List<SimpleGrantedAuthority> authorities = List.of(
              new SimpleGrantedAuthority("ROLE_" + role)
      );

      // Tạo AuthenticationToken
      UsernamePasswordAuthenticationToken authentication =
              new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    filterChain.doFilter(request, response);
  }
}
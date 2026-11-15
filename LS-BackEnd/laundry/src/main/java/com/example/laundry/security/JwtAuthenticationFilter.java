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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Set<String> PUBLIC_POST_PATHS = Set.of(
          "/auth/login",
          "/auth/logout",
          "/auth/refresh-token",
          "/auth/register",
          "/customer/register"
  );

  private final JwtUtil jwtUtil;
  private final UserDetailsService userDetailsService;
  private final TokenBlackListServiceImpl tokenBlackListServiceImpl;

  public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, TokenBlackListServiceImpl tokenBlackListServiceImpl) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
    this.tokenBlackListServiceImpl = tokenBlackListServiceImpl;
  }

  private String resolveRequestPath(HttpServletRequest request) {
    String requestUri = request.getRequestURI();
    if (requestUri != null && !requestUri.isEmpty()) {
      String contextPath = request.getContextPath();
      if (contextPath != null && !contextPath.isEmpty() && requestUri.startsWith(contextPath)) {
        return requestUri.substring(contextPath.length());
      }
      return requestUri;
    }
    String servletPath = request.getServletPath();
    return servletPath != null ? servletPath : "";
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = resolveRequestPath(request);
    String method = request.getMethod();
    if ("POST".equalsIgnoreCase(method) && PUBLIC_POST_PATHS.contains(path)) {
      return true;
    }
    if ("GET".equalsIgnoreCase(method)) {
      if ("/shops".equals(path)) {
        return true;
      }
      if (path.startsWith("/shop/") && (path.endsWith("/categories") || path.endsWith("/services"))) {
        return true;
      }
    }
    return false;
  }

  private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String safeMessage = message == null ? "" : message.replace("\\", "\\\\").replace("\"", "\\\"");
    response.getWriter().write("{\"message\": \"" + safeMessage + "\"}");
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");

    String principalId = null;
    String token = null;

    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      token = authorizationHeader.substring(7);

      if (tokenBlackListServiceImpl.isBlacklisted(token)) {
        logger.debug("Token is blacklisted");
        writeUnauthorized(response, "Token đã bị thu hồi");
        return;
      }

      try {
        principalId = jwtUtil.validateAccessTokenAndRetrieveSubject(token);
      } catch (JwtException e) {
        logger.warn("JWT token validation failed: " + e.getMessage());
        writeUnauthorized(response, "Token không hợp lệ hoặc đã hết hạn");
        return;
      }
    }

    if (principalId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      String role = jwtUtil.extractRole(token);
      if (role == null || role.isBlank()) {
        writeUnauthorized(response, "Token thiếu thông tin phân quyền");
        return;
      }

      try {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principalId);
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role.trim().toUpperCase(Locale.ROOT))
        );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (UsernameNotFoundException e) {
        logger.warn("Authenticated user no longer exists: " + principalId);
        writeUnauthorized(response, "Tài khoản không tồn tại hoặc đã bị xóa");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}
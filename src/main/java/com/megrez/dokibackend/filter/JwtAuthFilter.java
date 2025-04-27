package com.megrez.dokibackend.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 自定义过滤器
// 拦截所有请求，检查请求头中是否包含 Authorization 字段，并解析 JWT
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // 密钥
    private static final String SECRET_KEY = "1rS1Qur2XmrwIG9QgPwSc4sS89pZhaluU5hIX9feyA0";

    @Override
    protected void doFilterInternal(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, javax.servlet.FilterChain filterChain) throws javax.servlet.ServletException, IOException {
        // 从请求头中获取 Authorization 字段
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 提取 JWT
            String token = authHeader.substring(7);

            try {
                // 解析 JWT
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();

                // 从 JWT 中获取 userId
                Integer userId = claims.get("userId", Integer.class);
                // 将 userId 设置到 request 属性中
                request.setAttribute("userId", userId);

            } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
                // 如果 JWT 无效，返回 401 错误
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token");
                return;
            }
        }

        // 继续执行后续的过滤器或控制器
        filterChain.doFilter(request, response);
    }
}

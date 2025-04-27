package com.megrez.dokibackend.interceptor;

import com.megrez.dokibackend.utils.JWTUtil;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.http.server.ServerHttpResponse;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;

import javax.servlet.http.HttpServletRequest;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
            String authHeader = httpServletRequest.getHeader("Authorization");
            System.out.println("authHeader: " + authHeader);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7); // 去掉 "Bearer "
                String username = JWTUtil.extractUsername(token);
                if (username != null && JWTUtil.validateToken(token, username)) {
                    attributes.put("username", username);
                    return true; // 允许连接
                }
            }
        }
        return false; // 拒绝连接
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 握手后的处理逻辑
    }
}

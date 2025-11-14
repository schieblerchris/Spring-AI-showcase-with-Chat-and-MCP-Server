package com.github.sc.apps.saisc.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class MdcLoggingFilter extends OncePerRequestFilter {

    public static final String HDR_X_REQUEST_ID = "X-Request-Id";
    public static final String HDR_X_FORWARDED_FOR = "X-Forwarded-For";
    public static final String HDR_USER_AGENT = "User-Agent";
    public static final String MDC_REQUEST_ID = "requestId";
    public static final String MDC_METHOD = "method";
    public static final String MDC_PATH = "path";
    public static final String MDC_QUERY = "query";
    public static final String MDC_IP = "ip";
    public static final String MDC_USER_AGENT = "userAgent";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var requestId = resolveRequestId(request);
        var method = Optional.ofNullable(request.getMethod()).orElse("?");
        var path = Optional.ofNullable(request.getRequestURI()).orElse("/");
        var query = resolveQuery(request);
        var ip = resolveClientIp(request);
        var ua = Optional.ofNullable(request.getHeader(HDR_USER_AGENT)).orElse("n/a");

        try {
            putMdc(MDC_REQUEST_ID, requestId);
            putMdc(MDC_METHOD, method);
            putMdc(MDC_PATH, path);
            putMdc(MDC_IP, ip);
            putMdc(MDC_USER_AGENT, ua);
            if (!query.isEmpty()) {
                putMdc(MDC_QUERY, query);
            }
            response.setHeader(HDR_X_REQUEST_ID, requestId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    private static String resolveRequestId(HttpServletRequest request) {
        var fromHeader = request.getHeader(HDR_X_REQUEST_ID);
        return StringUtils.isBlank(fromHeader) ? UUID.randomUUID().toString() : fromHeader;
    }

    private static String resolveQuery(HttpServletRequest request) {
        var raw = request.getQueryString();
        return StringUtils.isBlank(raw) ? "" : ("?" + raw);
    }

    private static String resolveClientIp(HttpServletRequest request) {
        var xff = request.getHeader(HDR_X_FORWARDED_FOR);
        if (!StringUtils.isBlank(xff)) {
            var parts = xff.split(",");
            return (parts.length > 0 ? parts[0] : xff).trim();
        }
        return Optional.ofNullable(request.getRemoteAddr()).orElse("n/a");
    }

    private static void putMdc(String key, String value) {
        if (!StringUtils.isBlank(value)) {
            MDC.put(key, value);
        }
    }
}

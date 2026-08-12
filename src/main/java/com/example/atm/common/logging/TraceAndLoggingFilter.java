package com.example.atm.common.logging;

import com.example.atm.common.util.AppUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class TraceAndLoggingFilter extends OncePerRequestFilter {

  private static final String TRACE_ID_HEADER = "X-Trace-Id";
  private static final String MDC_TRACE_ID_KEY = "traceId";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    long startTime = System.currentTimeMillis();

    String traceId = request.getHeader(TRACE_ID_HEADER);

    if (traceId == null || traceId.isBlank()) {
      traceId = AppUtil.generateUUID();
    }

    MDC.put(MDC_TRACE_ID_KEY, traceId);

    response.setHeader(TRACE_ID_HEADER, traceId);

    try {

      log.info("HTTP IN | method={} uri={}", request.getMethod(), request.getRequestURI());

      filterChain.doFilter(request, response);

    } finally {

      long executionTime = System.currentTimeMillis() - startTime;

      log.info(
          "HTTP OUT | method={} uri={} status={} duration={}ms",
          request.getMethod(),
          request.getRequestURI(),
          response.getStatus(),
          executionTime);

      MDC.remove(MDC_TRACE_ID_KEY);
    }
  }
}

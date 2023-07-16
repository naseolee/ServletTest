package com.example;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SecondFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 필터 초기화 작업
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	System.out.println("SecondFilter Start");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        System.out.println("second filter: " + httpRequest.getRequestURL());
        
        // 다음 필터로 요청 전달
        chain.doFilter(request, response);
        System.out.println("SecondFilter End");

        // 후처리 작업
    }

    @Override
    public void destroy() {
        // 필터 종료 시 호출되는 작업
    }
}


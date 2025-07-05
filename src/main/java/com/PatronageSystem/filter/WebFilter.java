package com.PatronageSystem.filter;

import com.PatronageSystem.utils.IPUtil;
import nl.bitwalker.useragentutils.UserAgent;
import org.springframework.core.annotation.Order;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;


@jakarta.servlet.annotation.WebFilter(filterName = "sessionFilter", urlPatterns = "/*")
@Order(1)
public class WebFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        UserAgent userAgent = UserAgent.parseUserAgentString(req.getHeader("user-agent"));
        String browserName = userAgent.getBrowser().getName();
        String os = userAgent.getOperatingSystem().getName();
        req.getSession().setAttribute("ip", IPUtil.getIpAddress(req));
        req.getSession().setAttribute("ua", browserName + " " + os);
        filterChain.doFilter(servletRequest, servletResponse);
        /*UserAgent userAgent = UserAgent.parseUserAgentString(req.getHeader("user-agent"));
        //获取系统信息
        String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
        System.out.println("系统信息 = " + clientType);

        //系统名称
        String os = userAgent.getOperatingSystem().getName();
        System.out.println("系统名称 = " + os);
        //浏览器名称
        String browserName = userAgent.getBrowser().getName();
        System.out.println("浏览器名称 =" + browserName);
        //获取浏览器信息
        String browser = userAgent.getBrowser().toString();
        System.out.println("浏览器信息 = " + browser);*/
    }
}
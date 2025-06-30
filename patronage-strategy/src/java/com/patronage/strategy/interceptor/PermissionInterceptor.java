package src.java.com.patronage.strategy.interceptor;

import src.java.com.patronage.strategy.annotation.RequiresPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 */
@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是方法处理器，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        
        // 检查方法上的权限注解
        RequiresPermission methodPermission = handlerMethod.getMethodAnnotation(RequiresPermission.class);
        if (methodPermission != null) {
            return checkPermission(request, methodPermission.value());
        }

        // 检查类上的权限注解
        RequiresPermission classPermission = handlerMethod.getBeanType().getAnnotation(RequiresPermission.class);
        if (classPermission != null) {
            return checkPermission(request, classPermission.value());
        }

        return true;
    }

    /**
     * 检查权限
     */
    private boolean checkPermission(HttpServletRequest request, String permission) {
        // 这里实现具体的权限检查逻辑
        // 可以从请求头中获取token，然后验证用户权限
        
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            log.warn("请求缺少Authorization头");
            return false;
        }

        // TODO: 实现具体的权限验证逻辑
        // 这里暂时返回true，实际项目中需要根据token验证用户权限
        
        log.info("权限检查通过: permission={}, token={}", permission, token);
        return true;
    }
}
package com.jsj.mq.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * controller层的日志记录及统一异常处理
 *
 * @author jiangshenjie
 */
@Configuration
@Aspect
@Slf4j
public class WebLogAspect {

    @Pointcut("execution(public * com.jsj.mq.web.*.*(..))")
    public void controller() {
    }

    /**
     * 环绕通知
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around("controller()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 接收到请求，记录请求内容
        HttpServletRequest request = attributes.getRequest();
        // 拿到响应
        HttpServletResponse response = attributes.getResponse();
        log.info("Request , IP：[{}]，path: [{}]，method：[{}]，params：[{}]", request.getRemoteAddr(),
                request.getRequestURI(), request.getMethod(), Arrays.toString(proceedingJoinPoint.getArgs()));
        Object o = null;
        try {
            //目标类的方法调用
            o = proceedingJoinPoint.proceed();
            log.info("Response , IP：[{}]，path: [{}]，method：[{}]，params：[{}] stateCode: [{}], response: [{}]",
                    request.getRemoteAddr(), request.getRequestURI(), request.getMethod(),
                    Arrays.toString(proceedingJoinPoint.getArgs()), response.getStatus(), o);
        } catch (Throwable e) {
            log.error("Exception, IP：[{}]，path: [{}]，method：[{}]，params：[{}], message: [{}]",
                    request.getRemoteAddr(), request.getRequestURI(), request.getMethod(),
                    Arrays.toString(proceedingJoinPoint.getArgs()), e.getMessage());
        }
        return o;
    }
}

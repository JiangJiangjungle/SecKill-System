//package com.jsj.mq.aop;
//
//import com.alibaba.fastjson.JSON;
//import com.jsj.api.BuyResultEnum;
//import com.jsj.mq.pojo.vo.Message;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.ServletOutputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//
///**
// * controller层的日志记录及统一异常处理
// */
//@Configuration
//@Aspect
//@Slf4j
//public class WebLogAspect {
//
//    @Pointcut("execution(public * com.jsj.mq.web.*.*(..))")
//    public void controller() {
//    }
//
//    /**
//     * 环绕通知
//     *
//     * @param proceedingJoinPoint
//     * @return
//     */
//    @Around("controller()")
//    public Object arround(ProceedingJoinPoint proceedingJoinPoint) {
//        // 接收到请求，记录请求内容
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        // 接收到请求，记录请求内容
//        HttpServletRequest request = attributes.getRequest();
//        // 拿到响应
//        HttpServletResponse response = attributes.getResponse();
//        // 记录下请求内容
//        log.info("获取请求-------请求IP：" + request.getRemoteAddr() + "，请求路径：" + request.getRequestURI() + "，请求方法："
//                + request.getMethod() + "，请求参数：" + Arrays.toString(proceedingJoinPoint.getArgs()));
//        Object o;
//        try {
//            //目标类的方法调用
//            o = proceedingJoinPoint.proceed();
//        } catch (Throwable e) {
//            log.error("异常记录-------请求路径：" + request.getRequestURI() + " 发生错误, 错误信息:" + e.getMessage());
//            Message<Object> message = new Message<>();
//            message.setStatusCode(BuyResultEnum.SYSTEM_EXCEPTION.getValue());
//            message.setStatusMessage(e.getMessage());
//            try (ServletOutputStream os = response.getOutputStream()) {
//                os.write(JSON.toJSONString(message).getBytes());
//                os.flush();
//            } catch (IOException i) {
//                log.error("Exception:", e);
//            }
//            o = message;
//        }
//        log.info("获取响应-------请求IP：" + request.getRemoteAddr() + "，请求路径：" + request.getRequestURI() + "，请求方法："
//                + request.getMethod() + "，请求参数：" + Arrays.toString(proceedingJoinPoint.getArgs()) +
//                "，响应状态码：" + response.getStatus() + "，响应结果：" + o);
//        return o;
//    }
//}

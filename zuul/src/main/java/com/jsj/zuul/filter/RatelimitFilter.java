package com.jsj.zuul.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Slf4j
@Component
public class RatelimitFilter extends ZuulFilter {
    /**
     * 初始化 放入 200令牌/s  时间窗口为 1s
     */
    private RateLimiter rateLimiter = RateLimiter.create(200.0);

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        //若获取失败则拒绝请求
        if (!rateLimiter.tryAcquire()) {
            // 过滤该请求，不对其进行路由
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            log.info("Reject! :" + LocalTime.now() + ": " + rateLimiter.getRate());
        } else {
            ctx.setResponseStatusCode(200);
            log.info("OK! :" + LocalTime.now() + ": " + rateLimiter.getRate());
        }
        return null;
    }

    /**
     * 需要执行
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 路由之前执行
     *
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 优先级
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }
}

package com.example.reggiepro.interceptor;

import com.alibaba.fastjson.JSON;
import com.example.reggiepro.common.BaseContext;
import com.example.reggiepro.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("preHandle拦截的请求路径是{}",requestURI);
        Object employee = request.getSession().getAttribute("employee");
        Object user = request.getSession().getAttribute("user");
        if (employee!=null){
            log.info("用户已登录，用户id为{}",employee);
            BaseContext.setId((Long) employee);
            return true;
        }
        if (user!=null){
            log.info("用户已登录，用户id为{}",user);
            BaseContext.setId((Long) user);
            return true;
        }
        log.info("用户未登录");
        //如果没登录则返回未登录界面，通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

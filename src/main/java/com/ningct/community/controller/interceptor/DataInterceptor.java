package com.ningct.community.controller.interceptor;

import com.ningct.community.entity.User;
import com.ningct.community.service.DataService;
import com.ningct.community.util.HostHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.DatagramSocket;

@Component
public class DataInterceptor implements HandlerInterceptor {
    @Resource
    private HostHolder hostHolder;
    @Resource
    private DataService dataService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //记录UV
        dataService.recordUV(request.getRemoteHost());
        //记录DAU
        User holderUser = hostHolder.getUser();
        if(holderUser != null){
            dataService.recordDAU(holderUser.getId());
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}

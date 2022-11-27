package com.ningct.community.controller;

import com.ningct.community.entity.Event;
import com.ningct.community.event.EventProducer;
import com.ningct.community.util.CommunityConstant;
import com.ningct.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ShareController implements CommunityConstant {
    @Resource
    private EventProducer eventProducer;
    @Value("${ali.endpoint}")
    private String endPoint;
    @Value("${ali.bucketName}")
    private String bucketName;

    @RequestMapping(path = "/share",method = RequestMethod.GET)
    @ResponseBody
    public String share(String htmlUrl){
        //生成文件名
        String fileName = CommunityUtil.generateUUID();
        //异步生成长图
        Event even = new Event();
        even.setTopic(TOPIC_SHARE);
        even.setData("fileName",fileName)
                .setData("htmlUrl",htmlUrl)
                .setData("suffix",".png");
        eventProducer.fireEvent(even);
        //返回图片访问路径：阿里云路径
        Map<String, Object> map = new HashMap<>();
        String host = "https://" +bucketName+ "." +endPoint;
        map.put("shareUrl",host+"/"+fileName +".png");

        return CommunityUtil.getJSONString(0,null,map);
    }
}

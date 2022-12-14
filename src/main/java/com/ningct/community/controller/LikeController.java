package com.ningct.community.controller;

import com.ningct.community.annotation.LoginRequired;
import com.ningct.community.entity.Event;
import com.ningct.community.entity.User;
import com.ningct.community.event.EventProducer;
import com.ningct.community.service.LikeService;
import com.ningct.community.util.CommunityConstant;
import com.ningct.community.util.CommunityUtil;
import com.ningct.community.util.HostHolder;
import com.ningct.community.util.RedisKeyUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController implements CommunityConstant {
    @Resource
    private LikeService likeService;
    @Resource
    private HostHolder hostHolder;
    @Resource
    private EventProducer eventProducer;
    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @LoginRequired
    @ResponseBody
    public String like(int entityType, int entityId, int entityUserId, int postId){
        User user = hostHolder.getUser();
        likeService.like(user.getId(),entityType,entityId, entityUserId);
        Map<String, Object> map = new HashMap<>();
        map.put("likeCount",likeService.findEntityLikeCount(entityType,entityId));
        int likeStatus = likeService.findEntityLikeStatus(user.getId(),entityType,entityId);
        map.put("likeStatus",likeStatus);

        //触发点赞事件
        if(likeStatus == 1){
            Event event = new Event();
            event.setTopic(TOPIC_LIKE);
            event.setUserId(user.getId());
            event.setEntityType(entityType);
            event.setEntityId(entityId);
            event.setEntityUserId(entityUserId);
            event.setData("postId",postId);

            eventProducer.fireEvent(event);
        }
        if(entityType == ENTITY_TYPE_POST){
            //更新帖子分数
            String key = RedisKeyUtil.getScorePostRefreshKey();
            redisTemplate.opsForSet().add(key,postId);
        }

        return CommunityUtil.getJSONString(0,null,map);
    }
}

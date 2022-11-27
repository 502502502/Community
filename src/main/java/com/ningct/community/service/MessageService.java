package com.ningct.community.service;

import com.ningct.community.entity.Message;
import com.ningct.community.mapper.MessageMapper;
import com.ningct.community.util.SensitiveFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MessageService {
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private SensitiveFilter filter;

    public List<Message> findConversations(int userId, int offset, int limit){
        return messageMapper.selectConversations(userId,offset,limit);
    }

    public int findConversationCount(int userId){
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findLetters(String conversationId, int offset, int limit){
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    public int findLetterCount(String conversationId){
        return messageMapper.selectLetterCount(conversationId);
    }

    public int findLetterUnReadCount(int userId, String conversationId){
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    public Message findLastNotice(int userId, String topic){
        return messageMapper.selectLastNotice(userId,topic);
    }

    public int findNoticeCount(int userId, String topic){
        return messageMapper.selectNoticeCount(userId, topic);
    }

    public int findNoticeUnreadCount(int userId, String topic){
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    public List<Message> findNotices(int userId, String topic, int offset, int limit){
        return messageMapper.selectNotics(userId, topic, offset, limit);
    }

    public int addMessage(Message message){
        if(message == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent((filter.filter(message.getContent())));
        return messageMapper.insertMessage(message);
    }

    public int readMessage(List<Integer> ids){
        return messageMapper.updateMessageStatus(ids,1);
    }

}

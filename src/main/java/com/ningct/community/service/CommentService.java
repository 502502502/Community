package com.ningct.community.service;

import com.ningct.community.entity.Comment;
import com.ningct.community.mapper.CommentMapper;
import com.ningct.community.util.CommunityConstant;
import com.ningct.community.util.SensitiveFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import org.unbescape.html.HtmlEscape;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentService implements CommunityConstant{
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private SensitiveFilter filter;
    @Resource
    private DiscussPostService discussPostService;

    public List<Comment> findCommentsByEntity(int entityType, int enityId, int offset, int limid){
        return commentMapper.selectCommentByEntity(entityType, enityId, offset, limid);
    }

    public int findCommentCount(int entityType, int entityId){
        return commentMapper.selectCommentCount(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public int addComment(Comment comment){
        if(comment == null){
            throw  new IllegalArgumentException("参数不能为空！");
        }
        //过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(filter.filter(comment.getContent()));
        //插入评论
        int rows = commentMapper.insertComment(comment);
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            int count = commentMapper.selectCommentCount(comment.getEntityType(),comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }
        return rows;
    }

    public Comment findCommentById(int id){
        return commentMapper.selectCommentById(id);
    }
}

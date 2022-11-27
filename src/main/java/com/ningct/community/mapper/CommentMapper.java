package com.ningct.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ningct.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    List<Comment> selectCommentByEntity(@Param("entityType") int entityType, @Param("entityId") int entityId, @Param("offset") int offset, @Param("limit") int limit);

    int selectCommentCount(@Param("entityType") int entityType, @Param("entityId") int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(@Param("id") int id);
}

package com.ningct.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ningct.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface DiscussPostMapper extends BaseMapper<DiscussPost> {

    List<DiscussPost> selectDiscussPosts(@Param("userId") int userId, @Param("offset") int offset, @Param("limit") int limit,@Param("orderMode") int orderMode);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost post);

    DiscussPost selectDiscussPostById(@Param("id") int id);

    int updatePostCommentCount(@Param("id")int id, @Param("commentCount")int commentCount);

    int updatePostType(@Param("id") int id, @Param("type") int type);

    int updatePostStatus(@Param("id") int id, @Param("status") int status);
    int updatePostScore(@Param("id") int id, @Param("score") double score);

}

package com.ningct.community.quartz;

import com.ningct.community.entity.DiscussPost;
import com.ningct.community.service.DiscussPostService;
import com.ningct.community.service.ElasticSearchService;
import com.ningct.community.service.LikeService;
import com.ningct.community.util.CommunityConstant;
import com.ningct.community.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreRefreshJob implements Job, CommunityConstant {
    public static final Logger logger = LoggerFactory.getLogger(ScoreRefreshJob.class);
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private DiscussPostService discussPostService;
    @Resource
    private LikeService likeService;
    @Resource
    private ElasticSearchService elasticSearchService;
    private static final Date epoch;
    static {
        try {
            epoch =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        } catch (ParseException e) {
            throw new RuntimeException("初始化牛客纪元失败",e);
        }
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String key = RedisKeyUtil.getScorePostRefreshKey();
        BoundSetOperations operations = redisTemplate.boundSetOps(key);
        if(operations.size() == 0){
            logger.info("【任务取消】没有帖子需要刷新！");
            return ;
        }
        logger.info("【开始刷新帖子】：" +operations.size());
        while(operations.size() > 0){
            this.refresh((Integer) operations.pop());
        }
        logger.info("【刷新结束】帖子分数刷新完毕！");
    }
    public void refresh(int postId){
        DiscussPost post = discussPostService.selectDiscussPostById(postId);
        if(post == null){
            logger.error("该帖子不存在！");
            return;
        }
        int status = post.getStatus();
        int commentCount = post.getCommentCount();
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, postId);

        //计算权重
        double w = (status == 1 ? 75 : 0) + commentCount *10 + likeCount *2;
        double score = Math.log10(Math.max(w,1))
                +(post.getCreateTime().getTime() -epoch.getTime())/(1000*3600*24);
        post.setScore(score);
        discussPostService.updatePostScore(postId,score);
        elasticSearchService.addPost(post);
    }
}

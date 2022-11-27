package com.ningct.community.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.ningct.community.entity.DiscussPost;
import com.ningct.community.mapper.DiscussPostMapper;
import com.ningct.community.util.CommunityUtil;
import com.ningct.community.util.SensitiveFilter;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DiscussPostService {
    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);
    @Resource
    private DiscussPostMapper discussPostMapper;
    @Resource
    private SensitiveFilter sensitiveFilter;
    @Value("${caffeine.post.max-size}")
    private int postSize;
    @Value("${caffeine.post.expire-seconds}")
    private int expireTime;
    //帖子缓存
    private LoadingCache<String,List<DiscussPost>> postListCache;
    //行数缓存
    private LoadingCache<Integer, Integer> postRowCache;

    @PostConstruct
    public void init(){
        postListCache = Caffeine.newBuilder()
                .maximumSize(postSize)
                .expireAfterWrite(expireTime, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Override
                    public @Nullable List<DiscussPost> load(String key) throws Exception {
                        if(key == null || key.length() == 0){
                            throw new IllegalArgumentException("参数不能为空！");
                        }
                        String[] params = key.split(":");
                        if(params == null || params.length != 2){
                            throw new IllegalArgumentException("参数错误！");
                        }
                        int offset = Integer.parseInt(params[0]);
                        int limit = Integer.parseInt(params[1]);

                        //从数据库读取数据
                        logger.info("load post list from DB.");
                        return discussPostMapper.selectDiscussPosts(0,offset,limit, 1);
                    }
                });
        postRowCache = Caffeine.newBuilder()
                .maximumSize(postSize)
                .expireAfterWrite(expireTime, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public @Nullable Integer load(Integer key) throws Exception {
                        if(key == null || key != 0){
                            throw new IllegalArgumentException("参数不能为空！");
                        }
                        //从数据库读取数据
                        logger.info("load post row from DB.");
                        return discussPostMapper.selectDiscussPostRows(key);
                    }
                });
    }

    public List<DiscussPost> findDiscussPosts(int id, int offset, int limit, int orderMode){
        //从缓存取数据
        if(id == 0 && orderMode == 1){
            return postListCache.get(offset+":"+limit);
        }
        //从数据库读取帖子
        logger.info("load post list from DB.");
        return discussPostMapper.selectDiscussPosts(id,offset, limit,orderMode);
    }

    public int addDiscussPost(DiscussPost post){
        if(post == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转移html标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        //过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        //插入
        return discussPostMapper.insertDiscussPost(post);
    }

    public  int findDiscussPostRows(int id){
        if(id == 0){
            return postRowCache.get(id);
        }
        //从数据库读取行数
        logger.info("load post row from DB.");
        return discussPostMapper.selectDiscussPostRows(id);
    }

    public DiscussPost selectDiscussPostById(int id){
        return discussPostMapper.selectDiscussPostById(id);
    }

    public int updateCommentCount(int id, int count){
        return discussPostMapper.updatePostCommentCount(id,count);
    }

    public int updatePostType(int id, int type){
        return discussPostMapper.updatePostType(id,type);
    }

    public int updatePostStatus(int id, int status){
        return discussPostMapper.updatePostStatus(id, status);
    }
    public int updatePostScore(int id, double score){
        return discussPostMapper.updatePostScore(id, score);
    }

}

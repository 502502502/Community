package com.ningct.community.controller;

import com.ningct.community.entity.DiscussPost;
import com.ningct.community.entity.Page;
import com.ningct.community.service.DiscussPostService;
import com.ningct.community.service.ElasticSearchService;
import com.ningct.community.service.LikeService;
import com.ningct.community.service.UserService;
import com.ningct.community.util.CommunityConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SearchController implements CommunityConstant {
    @Resource
    private ElasticSearchService elasticSearchService;
    @Resource
    private UserService userService;
    @Resource
    private LikeService likeService;

    @RequestMapping(path = "/search",method = RequestMethod.GET)
    public String search(String keyword, Model model, Page page){

        page.setLimit(5);
        page.setPath("/search?keyword=" +keyword);
        page.setRows(elasticSearchService.findPostCount(keyword));

        List<DiscussPost> postList = elasticSearchService.searchPosts(keyword, page.getOffset(), page.getLimit());
        List<Map<String, Object>> posts = new ArrayList<>();
        if(postList != null && !postList.isEmpty()){
            for (DiscussPost post : postList) {
                System.out.println(post.toString());
                Map<String, Object> map = new HashMap<>();
                map.put("post",post);
                map.put("user",userService.findUserById(post.getUserId()));
                map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId()));

                posts.add(map);
            }
        }
        model.addAttribute("posts",posts);
        model.addAttribute("keyword",keyword);

        return "/site/search";
    }
}

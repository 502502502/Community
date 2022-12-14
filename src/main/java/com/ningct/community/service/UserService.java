package com.ningct.community.service;

import com.ningct.community.entity.LoginTicket;
import com.ningct.community.entity.User;
import com.ningct.community.mapper.LoginTicketMapper;
import com.ningct.community.mapper.UserMapper;
import com.ningct.community.util.CommunityConstant;
import com.ningct.community.util.CommunityUtil;
import com.ningct.community.util.MailClient;
import com.ningct.community.util.RedisKeyUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService implements CommunityConstant {
    @Resource
    private UserMapper userMapper;
    @Resource
    private MailClient mailClient;
    @Resource
    private TemplateEngine templateEngine;
//    @Resource
//    private LoginTicketMapper ticketMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id){

        //return userMapper.selectById(id);
        User user = getCache(id);
        if(user == null){
            user = initCache(id);
        }
        return user;
    }

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        // 空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }

        // 验证账号
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }

        // 验证邮箱
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("emailMsg", "该邮箱已被注册!");
            return map;
        }

        // 注册用户
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        //从数据库获取对象
        user = userMapper.selectByName(user.getUsername());

        // 激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        // http://localhost:8080/community/activation/101/code
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sentMail(user.getEmail(), "激活账号", content);

        return map;
    }
    public int activation(int userId, String activationCode){
        User user = userMapper.selectById(userId);
        if(user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        }else if(user.getActivationCode().equals(activationCode)){
            userMapper.updateStatus(userId, 1 );
            return ACTIVATION_SUCCESS;
        }
        return ACTIVATION_FALSE;
    }

    public Map<String ,Object> login(String username, String password, int expriedSeconds){
        Map<String ,Object> map = new HashMap<>();
        //空值处理
        if(StringUtils.isBlank(username)){
            map.put("usernameMsg","账号不能为空！");
            return map;
        }
        if(StringUtils.isBlank(username)){
            map.put("passwordMsg","密码不能为空！");
            return map;
        }

        //账号是否存在
        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("usernameMsg","账号不存在！");
            return map;
        }

        //状态检查
        if(user.getStatus() == 0){
            map.put("usernameMsg","账号未激活！");
            return map;
        }
        //密码检查
        if(!user.getPassword().equals(CommunityUtil.md5(password +user.getSalt()))){
            map.put("passwordMsg","密码不正确！");
            return map;
        }
        //登录凭证生成
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(user.getId());
        ticket.setStatus(0);
        ticket.setExpired(new Date(System.currentTimeMillis() +expriedSeconds));
        ticket.setTicket(CommunityUtil.generateUUID());

        //ticketMapper.insertLoginTicket(ticket);
        String ticketKey = RedisKeyUtil.getTicketKey(ticket.getTicket());
        redisTemplate.opsForValue().set(ticketKey,ticket);

        //返回ticket
        map.put("ticket",ticket.getTicket());
        return map;
    }

    public void logout(String ticket){

        //ticketMapper.updateState(ticket, 1);
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(ticketKey,loginTicket);
    }

    public LoginTicket findLoginTicket(String ticket){
        //return ticketMapper.getLoginTicketByTicket(ticket);
        String ticketKey = RedisKeyUtil.getTicketKey(ticket);
        return (LoginTicket) redisTemplate.opsForValue().get(ticketKey);
    }

    public int updateHeader(int userId, String headerUrl){
        int rows = userMapper.updateHeader(userId,headerUrl);
        clearCache(userId);
        return rows;
    }

    public Map<String ,Object> updatePassword(User user, String oldPassword, String newPassword, String confirmPassword){
       Map<String ,Object> map = new HashMap<>();
       //空值检查
        if(user == null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        if(StringUtils.isBlank(oldPassword)){
            map.put("oldPasswordMsg","初始密码不能为空！");
            return map;
        }
        if(StringUtils.isBlank(newPassword)){
            map.put("newPasswordMsg","新密码不能为空！");
            return map;
        }
        if(StringUtils.isBlank(newPassword)){
            map.put("confirmPasswordMsg","确认密码不能为空！");
            return map;
        }
        oldPassword = CommunityUtil.md5(oldPassword +user.getSalt());
        if(!oldPassword.equals(user.getPassword())){
            map.put("oldPasswordMsg","初始密码不正确！");
            return map;
        }
        if(newPassword.length() < 8){
            map.put("newPasswordMsg","密码不能小于8位！");
            return map;
        }
        if(!newPassword.equals(confirmPassword)){
            map.put("confirmPasswordMsg","两次输入密码不一致！");
            return map;
        }
        //修改密码
        newPassword = CommunityUtil.md5(newPassword +user.getSalt());
        userMapper.updatePassword(user.getId(), newPassword);
        return map;
    }
    public User findUserByName(String username){
        return userMapper.findUserByName(username);
    }

    //从缓存获取User
    public User getCache(int userId){
        String userKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(userKey);
    }
    //初始化缓存的User
    public User initCache(int userId){
        String userKey = RedisKeyUtil.getUserKey(userId);
        User user = userMapper.selectById(userId);
        redisTemplate.opsForValue().set(userKey,user,3600, TimeUnit.SECONDS);
        return user;
    }
    //清理缓存的User
    public void clearCache(int userId){
        String userKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(userKey);
    }

    //查询用户的权限
    public  Collection<? extends GrantedAuthority>  getAuthorities(int userId){
        User user = findUserById(userId);

        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()){
                    case 1:
                        return AUTHORITY_ADMIN;
                    case 2:
                        return AUTHORITY_MODERATOR;
                    default:
                        return AUTHORITY_USER;
                }
            }
        });
        return list;
    }

}

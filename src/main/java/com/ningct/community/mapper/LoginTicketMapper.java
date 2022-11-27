package com.ningct.community.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ningct.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@Deprecated
public interface LoginTicketMapper extends BaseMapper<LoginTicket> {

    int insertLoginTicket(LoginTicket loginTicket);

    LoginTicket getLoginTicketByTicket(@Param("ticket")String ticket);

    int updateState(@Param("ticket") String ticket, @Param("status")int status);
}

package com.ningct.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
public class wkConfig {
    private static final Logger logger = LoggerFactory.getLogger(wkConfig.class);
    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @PostConstruct
    public void init(){
        File file = new File(wkImageStorage);
        if(!file.exists()){
            file.mkdir();
            logger.info("创建wkImage文件目录：" +wkImageStorage);
        }
    }
}

package com.ningct.community.event;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.ningct.community.entity.DiscussPost;
import com.ningct.community.entity.Event;
import com.ningct.community.entity.Message;
import com.ningct.community.service.DiscussPostService;
import com.ningct.community.service.ElasticSearchService;
import com.ningct.community.service.MessageService;
import com.ningct.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Component
public class EventConsumer implements CommunityConstant {

    private static  final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    @Autowired
    private MessageService messageService;
    @Resource
    private DiscussPostService discussPostService;
    @Resource
    private ElasticSearchService elasticSearchService;
    @Value("${wk.image.command}")
    private String getWkImageCmd;
    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @Value("${ali.accessKeyId}")
    private String accessKeyId;
    @Value("${ali.accessKeySecret}")
    private String accessKeySecret;
    @Value("${ali.bucketName}")
    private String bucketName;
    @Value("${ali.endpoint}")
    private String endPoint;
    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    //??????????????????????????????
    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("?????????????????????!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("??????????????????!");
            return;
        }

        // ??????????????????
        Message message = new Message();
        message.setFromId(SYSTEM_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);

    }
    //????????????
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePostOnES(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("????????????????????????!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("??????????????????????????????!");
            return;
        }

        // ??????ES??????
        DiscussPost post = discussPostService.selectDiscussPostById(event.getEntityId());
        elasticSearchService.addPost(post);

    }

    //??????????????????
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeletePostOnES(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("????????????????????????!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("??????????????????????????????!");
            return;
        }

        // ??????ES??????
        elasticSearchService.deletePost(event.getEntityId());

    }

    //?????????????????????????????????????????????
    @KafkaListener(topics = {TOPIC_SHARE})
    public void handleShare(ConsumerRecord record){
        if (record == null || record.value() == null) {
            logger.error("????????????????????????!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("??????????????????!");
            return;
        }
        Map<String, Object> data = event.getData();
        String htmlUrl = (String) data.get("htmlUrl");
        String fileName = (String) data.get("fileName");
        String suffix = (String) data.get("suffix");

        String cmd = getWkImageCmd + " --quality 75 "
                + htmlUrl + " " + wkImageStorage + "/" + fileName + suffix;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            logger.error("?????????????????????" +e.getMessage());
        }

        //???????????????????????????????????????????????????????????????
        UpdateTask updateTask = new UpdateTask(fileName,suffix);
        Future future = threadPoolTaskScheduler.scheduleAtFixedRate(updateTask, 500);
        updateTask.setFuture(future);
    }
    class UpdateTask implements Runnable{
        private String fileName;
        private String suffix;
        //??????????????????
        private Future future;
        //????????????
        private long startTime;
        //????????????
        private int updateTimes;
        public void setFuture(Future future){
            this.future = future;
        }
        public UpdateTask(String fileName, String suffix){
            this.fileName = fileName;
            this.suffix = suffix;
            startTime =System.currentTimeMillis();
        }

        @Override
        public void run() {
            //????????????
            if(System.currentTimeMillis() -startTime >30000){
                logger.error("????????????????????????????????????");
                future.cancel(true);
                return;
            }
            //????????????
            if(updateTimes >= 3){
                logger.error("????????????????????????????????????");
                future.cancel(true);
                return;
            }

            //??????????????????
            String path = wkImageStorage+"/"+fileName+suffix;
            //????????????????????????
            File file = new File(path);

            //???????????????????????????
            if(file.exists()){
                logger.info(String.format("?????????%d?????????%s", ++updateTimes,fileName));
                //??????
                OSS ossClient = new OSSClientBuilder().build(endPoint,accessKeyId,accessKeySecret);
                try {
                    PutObjectResult result = ossClient.putObject(bucketName, fileName+suffix, file);
                    //????????????
                    logger.info(String.format("???%d??????????????????%s???", updateTimes,fileName));
                    future.cancel(true);
                }catch (OSSException oe){
                    //????????????
                    logger.error(String.format("?????????%d??????????????????%s???", updateTimes,fileName) + oe.getErrorCode());
                }
            }else{
                //??????????????????
                logger.info("??????????????????+???"+fileName+"???.");
            }
        }
    }
}

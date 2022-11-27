package com.ningct.community;

import com.ningct.community.entity.Event;
import com.ningct.community.event.EventProducer;
import com.ningct.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@SpringBootTest
public class KafkaTest implements CommunityConstant {
    @Resource
    private EventProducer eventProducer;

    @Test
    public void testEventKafka(){
        Event event = new Event();
        event.setTopic(TOPIC_LIKE);
        event.setUserId(11111);
        event.setEntityType(-1);
        eventProducer.fireEvent(event);
    }
}
package cn.itcast.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/mq")
@RestController
public class MQController {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    /**
     * 发送MQ消息
     * @return 操作结果
     */
    @GetMapping("/sendMsg")
    public String sendMqMsg(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", 123456);
        map.put("name", "黑马");

        //发送消息到mq
        jmsMessagingTemplate.convertAndSend("spring.boot.mq.queue", map);

        return "发送MQ 队列spring.boot.mq.queue消息成功。";
    }

    /**
     * 发送短信MQ消息
     * @return 操作结果
     */
    @GetMapping("/sendSmsMsg")
    public String sendSmsMsg(){
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", "15919606556");
        map.put("signName", "黑马");
        map.put("templateCode", "SMS_125018593");
        map.put("templateParam", "{\"code\":\"654321\"}");

        //发送消息到mq
        jmsMessagingTemplate.convertAndSend("itcast_sms_queue", map);

        return "发送MQ 队列itcast_sms_queue消息成功。";
    }
}

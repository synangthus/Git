package cn.itcast.springboot.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    /*
    * 发送一个map消息到MQ的队列中
    * */
    @GetMapping("/send")
    public String sendMapMsg(){

        Map<String ,Object> map = new HashMap<>();
        map.put("id",132L);
        map.put("name","海龙马");
        jmsMessagingTemplate.convertAndSend("spring.boot.map.queue",map);


        return  "发送消息完成";
    }
}

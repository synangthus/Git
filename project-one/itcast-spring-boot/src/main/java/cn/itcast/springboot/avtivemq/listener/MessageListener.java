package cn.itcast.springboot.avtivemq.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageListener {

    /*
    * 接收MQ的消息
    * */
    @JmsListener(destination =  "spring.boot.map.queue")
    public void receiveMsg(Map<String,Object> map){
        System.out.println(map);
    }
}

package com.example.mqdemo.conf;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.UnsupportedEncodingException;

/**
 * Created by simon.liu on 2017/12/2.
 */
@Configuration
@EnableScheduling
public class Conf {

    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer("producer");
        producer.setNamesrvAddr("112.91.86.162:9876");
        producer.setRetryTimesWhenSendFailed(3);
        producer.start();
        return producer;
    }

    @Scheduled(fixedDelay = 5000)
    public void method() {
        try {
            String msg = "test";
            Message message = new Message("Payment", "visionSystem1", msg.getBytes("UTF-8"));
            SendResult sendResult = defaultMQProducer().send(message);
            System.out.println(sendResult);
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (MQBrokerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

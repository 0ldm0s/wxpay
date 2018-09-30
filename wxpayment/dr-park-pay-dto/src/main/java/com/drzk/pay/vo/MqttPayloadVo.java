package com.drzk.pay.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

/**
 * 2018/6/23 cx
 */
public class MqttPayloadVo<T> implements Serializable{

    //头信息
    private MqttHeadVo head;

    //发送内容
    private T body;

    @JsonIgnore
    private String topic;

    public MqttHeadVo getHead() {
        if(head==null){
            this.head=new MqttHeadVo();
        }
        return head;
    }

    public void setHead(MqttHeadVo head) {
        this.head = head;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "MqttSendPayloadVo{" +
                "head=" + head +
                ", body=" + body +
                ", topic='" + topic + '\'' +
                '}';
    }
}

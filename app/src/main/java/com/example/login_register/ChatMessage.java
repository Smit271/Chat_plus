package com.example.login_register;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String fromUserId;
    private String touserId;
    private long messageTime;

    //empty constructor needed for firebase
    public ChatMessage(){ }

    public ChatMessage(String messageText, String fromUserId, String toUserId) {
        this.messageText = messageText;
        this.fromUserId = fromUserId;
        this.touserId = toUserId;
        messageTime = new Date().getTime();;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getTouserId() {
        return touserId;
    }

    public void setTouserId(String touserId) {
        this.touserId = touserId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}

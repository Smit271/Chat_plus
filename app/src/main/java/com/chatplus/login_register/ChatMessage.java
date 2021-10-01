package com.chatplus.login_register;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String senderId;
    private String receiverId;
    private long messageTime;

    //empty constructor needed for firebase
    public ChatMessage(){ }


    public ChatMessage(String messageText, String senderId, String receiverId) {
        this.messageText = messageText;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageTime = messageTime;
        messageTime = new Date().getTime();;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}

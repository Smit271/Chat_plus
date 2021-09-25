package com.example.login_register;

import java.util.Date;

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private String meesageUserid;
    private long messageTime;

    //empty constructor needed for firebase
    public ChatMessage(){ }

    public ChatMessage(String messageText, String messageUser, String meesageUserid) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.meesageUserid = meesageUserid;
        messageTime = new Date().getTime();;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMeesageUserid() {
        return meesageUserid;
    }

    public void setMeesageUserid(String meesageUserid) {
        this.meesageUserid = meesageUserid;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}

package com.chatplus.login_register;

public class SearchUserInfo {

    private String user_id,friend_id,isFriend,currentState;
    int index;


    public SearchUserInfo(String user_id, String friend_id, String isFriend, String currentState,int index) {
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.isFriend = isFriend;
        this.currentState = currentState;
        this.index = index;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

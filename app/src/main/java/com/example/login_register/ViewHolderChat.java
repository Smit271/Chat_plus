package com.example.login_register;

import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewHolderChat {

    TextView mText;
    TextView mUserId;
    TextView time;

    public ViewHolderChat(TextView mUserId, TextView mText, TextView time) {
        this.mText = mText;
        this.mUserId = mUserId;
        this.time = time;
    }

    public ViewHolderChat(TextView mText, TextView time) {
        this.mText = mText;
        this.time = time;
    }

    public TextView getmText() {
        return mText;
    }

    public void setmText(TextView mText) {
        this.mText = mText;
    }

    public TextView getmUserId() {
        return mUserId;
    }

    public void setmUserId(TextView mUserId) {
        this.mUserId = mUserId;
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }
}

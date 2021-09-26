package com.example.login_register;

import android.app.Activity;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ChatListAdapter extends ArrayAdapter<ChatMessage> {

    ChatScreen1 activity;
    ArrayList<ChatMessage> items;
    private static final String TAG = "Kinetic";
    public ChatListAdapter(ChatScreen1 context, ArrayList<ChatMessage> chatList) {
        super(context,0,chatList);
        activity = context;
        items = chatList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage message = getItem(position);
        //Log.d(TAG,"++++++++++ " + message.getMessageText() + " +++++++++++++");
        String myUserId = activity.getLoggedinUserId();
        String myFriendId = activity.getMyFriendUserId();

        String senderId = message.getSenderId();
        String receiverId = message.getReceiverId();

        if (convertView == null) {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_received, parent, false);

            if (senderId.equals(myUserId)) {
                //Log.d(TAG,"Me, In getView "+myUserId);
                Log.d(TAG,"Me:++++++++++ " + message.getMessageText() + " +++++++++++++");
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_sent, parent, false);
                TextView mUser = (TextView) convertView.findViewById(R.id.message_user);
                TextView mText = (TextView) convertView.findViewById(R.id.message_text);
                TextView mTime = (TextView) convertView.findViewById(R.id.message_time);

//                Log.d(TAG,"TextView in adapter: mUser"+ mUser);
//                Log.d(TAG,"TextView in adapter: mText"+ mText);
//                Log.d(TAG,"TextView in adapter: mTime"+ mTime);

                mUser.setText(myUserId);
                mText.setText(message.getMessageText());
                mTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));

            } else if (senderId.equals(myFriendId)) {

                //Log.d(TAG,"Friend, In getView "+myFriendId);
                Log.d(TAG,"Friend: ++++++++++ " + message.getMessageText() + " +++++++++++++");
                convertView = activity.getLayoutInflater().inflate(R.layout.message_received, parent, false);
                TextView mUser = (TextView) convertView.findViewById(R.id.message_user);
                TextView mText = (TextView) convertView.findViewById(R.id.message_text);
                TextView mTime = (TextView) convertView.findViewById(R.id.message_time);

//                Log.d(TAG,"TextView in adapter: mUser"+ mUser);
//                Log.d(TAG,"TextView in adapter: mText"+ mText);
//                Log.d(TAG,"TextView in adapter: mTime"+ mTime);

                mUser.setText(myFriendId);
                mText.setText(message.getMessageText());
                mTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));

            } else {
                Log.d(TAG,"opps no condition ran$$$$$$");
            }
        }
        else{
                //ChatMessage msj = (ChatMessage) convertView.getTag();

            Log.d(TAG,"Not null: ******* "+ message.getMessageText() +" ******");
            LinearLayout view = (LinearLayout) convertView;
            TextView uname = (TextView) view.getChildAt(0);
            uname.setText(message.getSenderId());

            TextView msj = (TextView) view.getChildAt(1);
            msj.setText(message.getMessageText());

            TextView ti = (TextView) view.getChildAt(2);
            ti.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));

            return view;
        }


        return convertView;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Nullable
    @Override
    public ChatMessage getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
}

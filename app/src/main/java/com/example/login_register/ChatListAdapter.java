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
    private String myUserId;
    private String myFriendUserId;

    public ChatListAdapter(ChatScreen1 context, ArrayList<ChatMessage> chatList) {
        super(context,0,chatList);
        activity = context;
        items = chatList;
        myUserId = context.getLoggedinUserId();
        myFriendUserId = context.getMyFriendUserId();
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
        ChatMessage chatMessage = getItem(position);
        String senderId = chatMessage.getSenderId();
        String receiverId = chatMessage.getReceiverId();

        if (senderId.equals(myUserId)) {
            return 0;
        }
        else {
            return 1;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChatMessage message = getItem(position);
        //Log.d(TAG,"++++++++++ " + message.getMessageText() + " +++++++++++++");

        String senderId = message.getSenderId();
        String receiverId = message.getReceiverId();

        ViewHolderChat viewHolderChat = null;

        if (convertView == null) {
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_received, parent, false);

            if (senderId.equals(myUserId)) {
                //Log.d(TAG,"Me, In getView "+myUserId);
                Log.d(TAG,"Me:++++++++++ " + message.getMessageText() + " +++++++++++++");
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_sent, parent, false);
                TextView mUser = (TextView) convertView.findViewById(R.id.message_user);
                TextView mText = (TextView) convertView.findViewById(R.id.message_text);
                TextView mTime = (TextView) convertView.findViewById(R.id.message_time);

                viewHolderChat = new ViewHolderChat(mUser,mText,mTime);
                viewHolderChat.getmUserId().setText(myUserId);

//                mUser.setText(myUserId);
//                mText.setText(message.getMessageText());
//                mTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));

            } else if (senderId.equals(myFriendUserId)) {

                //Log.d(TAG,"Friend, In getView "+myFriendUserId);
                Log.d(TAG,"Friend: ++++++++++ " + message.getMessageText() + " +++++++++++++");
                convertView = activity.getLayoutInflater().inflate(R.layout.message_received, parent, false);
                TextView mUser = (TextView) convertView.findViewById(R.id.message_user);
                TextView mText = (TextView) convertView.findViewById(R.id.message_text);
                TextView mTime = (TextView) convertView.findViewById(R.id.message_time);

                viewHolderChat = new ViewHolderChat(mUser,mText,mTime);

                viewHolderChat.getmUserId().setText(myFriendUserId);
//                mUser.setText(myFriendUserId);
//                mText.setText(message.getMessageText());
//                mTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));


            }
            convertView.setTag(viewHolderChat);
        }
        else{
                //ChatMessage msj = (ChatMessage) convertView.getTag();

            viewHolderChat = (ViewHolderChat) convertView.getTag();
            Log.d(TAG,"Not null: ******* "+ message.getMessageText() +" ******");

//            LinearLayout view = (LinearLayout) convertView;
//            TextView uname = (TextView) view.getChildAt(0);
//            uname.setText(message.getSenderId());
//
//            TextView msj = (TextView) view.getChildAt(1);
//            msj.setText(message.getMessageText());
//
//            TextView ti = (TextView) view.getChildAt(2);
//            ti.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));

        }

        viewHolderChat.getmText().setText(message.getMessageText());
        viewHolderChat.getTime().setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));

        return convertView;
    }

//    @Override
//    public int getCount() {
//        return this.items.size();
//    }
//
//    @Nullable
//    @Override
//    public ChatMessage getItem(int position) {
//        return this.items.get(position);
//    }
//
//    @Override
//    public long getItemId(int arg0) {
//        return arg0;
//    }


}

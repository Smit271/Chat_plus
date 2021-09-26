package com.example.login_register;

import android.app.Activity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.database.FirebaseListAdapter;

import com.google.firebase.database.DatabaseReference;

public class AdapterMessage extends FirebaseListAdapter<ChatMessage> {

    private ChatScreen1 activity;
    private static final String TAG = "Kinetic";

    public AdapterMessage(ChatScreen1 activity, Class<ChatMessage> modelClass, int modelLayout, DatabaseReference ref) {
        super(activity,modelClass,modelLayout,ref);
        this.activity = activity;
    }

    @Override
    //method to fill values in message item
    protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {
//        TextView mUser = (TextView) v.findViewById(R.id.message_user);
//        TextView mText = (TextView) v.findViewById(R.id.message_text);
//        TextView mTime = (TextView) v.findViewById(R.id.message_time);
//
//        mUser.setText(model.getMessageUser());
//        mText.setText(model.getMessageText());
//        mTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
//
//        mUser.setGravity(Gravity.RIGHT);
//        mText.setGravity(Gravity.RIGHT);
//        mTime.setGravity(Gravity.RIGHT);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ChatMessage message = getItem(position);
        String myUserId = activity.getLoggedinUserId();
        String myFriendId = activity.getMyFriendUserId();
        //Log.d(TAG,"Not Null - Me, In getView "+message.getMeesageUserid());
        //Log.d(TAG,"Not Null - Friend, In getView "+message.getMeesageUserid());
        //convertView = activity.getLayoutInflater().inflate(R.layout.message, parent, false);

        if (convertView == null) {
            //Log.d(TAG,"Null - Me, In getView "+message.getMeesageUserid());
            Log.d(TAG,"Yes view is null !!!!!!!! ");

            /*
            For example, if I clicked on malav123 then I (karm123)
            can see only message of mine and malav123
             */

            String toUserId = message.getTouserId();
            String fromUserId = message.getFromUserId();
            convertView = activity.getLayoutInflater().inflate(R.layout.message, parent, false);

            if (fromUserId.equals(myUserId) && toUserId.equals(myFriendId)){
                //for Logged in user (me)
                Log.d(TAG,"Me, In getView "+myUserId);

                TextView mUser = (TextView) convertView.findViewById(R.id.message_user);
                TextView mText = (TextView) convertView.findViewById(R.id.message_text);
                TextView mTime = (TextView) convertView.findViewById(R.id.message_time);

                mUser.setText(myUserId);
                mText.setText(message.getMessageText());
                mTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));

                mUser.setGravity(Gravity.RIGHT);
                mText.setGravity(Gravity.RIGHT);
                mTime.setGravity(Gravity.RIGHT);

            }

            //Show friend message to me
            else if (toUserId.equals(myUserId) && fromUserId.equals(myFriendId)){
                Log.d(TAG,"Friend, In getView "+myFriendId);

                TextView mUser = (TextView) convertView.findViewById(R.id.message_user);
                TextView mText = (TextView) convertView.findViewById(R.id.message_text);
                TextView mTime = (TextView) convertView.findViewById(R.id.message_time);

                mUser.setText(myFriendId);
                mText.setText(message.getMessageText());
                mTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", message.getMessageTime()));

                mUser.setGravity(Gravity.LEFT);
                mText.setGravity(Gravity.LEFT);
                mTime.setGravity(Gravity.LEFT);
            }
            else
                Log.d(TAG,"opps no condition ran$$$$$$");

        }

        populateView(convertView,message,position);
        return convertView;
    }

    //Total no of types of item here we have 2 (sender & receiver)
//    @Override
//    public int getViewTypeCount() {
//        return 1;
//    }
//
//    //to distiguish between sender & receiver
//    @Override
//    public int getItemViewType(int position) {
//        return position%1;
//    }
}

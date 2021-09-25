package com.example.login_register;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

public class AdapterMessage extends FirebaseListAdapter<ChatMessage> {



    public AdapterMessage(Activity activity, Class<ChatMessage> modelClass, int modelLayout, DatabaseReference ref) {
        super(activity,modelClass,modelLayout,ref);

    }


    @Override
    //method to fill values in message item
    protected void populateView(@NonNull View v, @NonNull ChatMessage model, int position) {

        TextView mUser = (TextView) v.findViewById(R.id.message_user);
        TextView mText = (TextView) v.findViewById(R.id.message_text);
        TextView mTime = (TextView) v.findViewById(R.id.message_time);

        mUser.setText(model.getMessageUser());
        mText.setText(model.getMessageText());
        mTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ChatMessage message = getItem(position);

        if (convertView != null) {
            LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
            populateView(convertView,message,position);
        }



        return convertView;
    }

    //Total no of types of item here we have 2 (sender & receiver)
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //to distiguish between sender & receiver
    @Override
    public int getItemViewType(int position) {
        return position%2;
    }
}

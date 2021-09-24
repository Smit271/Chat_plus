package com.example.login_register;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class adapter_listview_of_friends_in_chat extends ArrayAdapter <SingleFriend> {

    public adapter_listview_of_friends_in_chat(Activity context, ArrayList <SingleFriend> Friends){
        super(context,0,Friends);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //List view where the items will be displayed
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_friend, parent, false);
        }

        //get single friend detail from pool of list
        SingleFriend friend = (SingleFriend) getItem(position);

        //Prepare single friend view

        //set usname
        TextView funame = (TextView) listItemView.findViewById(R.id.username);
        funame.setText(friend.getUsername());
        //set profile image
        ImageView fprofile_image = (ImageView) listItemView.findViewById(R.id.profile_picture);
        fprofile_image.setImageResource(friend.getProfile_image_id());

        return listItemView;
    }
}

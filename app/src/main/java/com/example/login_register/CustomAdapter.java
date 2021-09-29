package com.example.login_register;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<dataHandler> localDataSet;
    private ArrayList<Search_user_data> user_dataset;
    private ArrayList<String> friends_id;
    DatabaseReference mref;
    String user_id;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        Button item_btn;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            name = (TextView) view.findViewById(R.id.name);
            email = (TextView) view.findViewById(R.id.email);
            item_btn = (Button) view.findViewById(R.id.item_btn);

        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet       String[] containing the data to populate views to be used
     *                      by RecyclerView.
     */
    public CustomAdapter(ArrayList<dataHandler> dataSet,ArrayList<Search_user_data> user_dataset) {
        super();
        this.localDataSet = dataSet;
        this.user_dataset = user_dataset;
        mref = FirebaseDatabase.getInstance().getReference("users");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        dataHandler thisuser = localDataSet.get(position);
        Search_user_data one_user_detail = user_dataset.get(position);

        viewHolder.name.setText(thisuser.getName());
        viewHolder.email.setText(thisuser.getEmail());
        System.out.println("---"+one_user_detail.getUser_id()+one_user_detail.getIsFriend()+one_user_detail.getCurrentState()+one_user_detail.getFriend_id());
        if (one_user_detail.getIsFriend().equals("accepted")) {
            viewHolder.item_btn.setText("Remove Friend");
            viewHolder.item_btn.setBackgroundColor(0xFFD40000);
            viewHolder.item_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mref.child(one_user_detail.getUser_id()).child("friends").child(one_user_detail.getFriend_id()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            viewHolder.item_btn.setText("Add Friend");
                            viewHolder.item_btn.setBackgroundColor(0xFF000000);
                            Toast.makeText(view.getContext(), "Removed " + one_user_detail.getFriend_id(), Toast.LENGTH_LONG).show();
                            System.out.println("2222222");
//                            viewHolder.item_btn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (one_user_detail.getCurrentState().equals("not_received")) {
//                                        mref.child(one_user_detail.getFriend_id()).child("Request").child(one_user_detail.getUser_id()).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                viewHolder.item_btn.setText("Cancel Request");
//                                                viewHolder.item_btn.setBackgroundColor(0xFFD40000);
//
//                                            }
//                                        });
//                                    } else if (one_user_detail.getCurrentState().equals("received")) {
//                                        mref.child(one_user_detail.getFriend_id()).child("Request").child(one_user_detail.getUser_id()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                viewHolder.item_btn.setText("Add Friend");
//                                                viewHolder.item_btn.setBackgroundColor(0xFF000000);
//                                            }
//                                        });
//                                    }
//                                }
//                            });
                        }
                    });
                    mref.child(one_user_detail.getFriend_id()).child("friends").child(one_user_detail.getUser_id()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            System.out.println("33333333");
                        }
                    });

                    System.out.println("111111");
                }
            });
        } else if (one_user_detail.getIsFriend().equals("not_accepted")) {
            if (one_user_detail.getUser_id().equals(one_user_detail.getFriend_id())) {
                System.out.println("Hello");
                viewHolder.item_btn.setVisibility(View.INVISIBLE);
            } else {
                if (one_user_detail.getCurrentState().equals("not_received")) {
                    viewHolder.item_btn.setText("Add Friend");
                    viewHolder.item_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mref.child(one_user_detail.getFriend_id()).child("Request").child(one_user_detail.getUser_id()).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    viewHolder.item_btn.setText("Cancel Request");
                                    viewHolder.item_btn.setBackgroundColor(0xFFD40000);
                                    System.out.println("send-------");
//                                    viewHolder.item_btn.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            mref.child(one_user_detail.getFriend_id()).child("Request").child(one_user_detail.getUser_id()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    viewHolder.item_btn.setText("Add Friend");
//                                                    viewHolder.item_btn.setBackgroundColor(0xFF000000);
//                                                    System.out.println("cancel -------");
//                                                }
//                                            });
//                                        }
//                                    });
                                }
                            });
                        }
                    });
                } else if (one_user_detail.getCurrentState().equals("received")) {
                    viewHolder.item_btn.setText("Cancel Request1");
                    viewHolder.item_btn.setBackgroundColor(0xFFD40000);
                    viewHolder.item_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mref.child(one_user_detail.getFriend_id()).child("Request").child(one_user_detail.getUser_id()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    viewHolder.item_btn.setText("Add Friend");
                                    viewHolder.item_btn.setBackgroundColor(0xFF000000);
                                    System.out.println("cancel -------");
//                                    viewHolder.item_btn.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            mref.child(one_user_detail.getFriend_id()).child("Request").child(one_user_detail.getUser_id()).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    viewHolder.item_btn.setText("Cancel Request");
//                                                    viewHolder.item_btn.setBackgroundColor(0xFFD40000);
//                                                    System.out.println("send-------");
//                                                }
//                                            });
//                                        }
//                                    });
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
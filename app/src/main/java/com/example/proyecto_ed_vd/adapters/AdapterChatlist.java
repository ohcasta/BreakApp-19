package com.example.proyecto_ed_vd.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_ed_vd.ArrayListUser;
import com.example.proyecto_ed_vd.Chat_Activity;
import com.example.proyecto_ed_vd.HashMapMessage;
import com.example.proyecto_ed_vd.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdapterChatlist extends RecyclerView.Adapter<AdapterChatlist.MyHolder> {

    Context context;
    ArrayListUser userList;
    private HashMapMessage lastMessageMap;

    public AdapterChatlist(Context context, ArrayListUser userList) {
        this.context = context;
        this.userList = userList;
        lastMessageMap = new HashMapMessage(19);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final String hisUID = userList.get(i).getUid();
        String userImage = userList.get(i).getImage();
        String userName = userList.get(i).getName();
        String lastMessage = lastMessageMap.Get(hisUID);

        myHolder.nameTv.setText(userName);
        if (lastMessage==null || lastMessage.equals("default")) {
            myHolder.lastMessageTv.setVisibility(View.GONE);
        } else {
            myHolder.lastMessageTv.setVisibility(View.VISIBLE);
            myHolder.lastMessageTv.setText(lastMessage);
        }
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.ic_default_img).into(myHolder.profileIv);
        } catch (Exception e) {
            Picasso.get().load(R.drawable.ic_default_img).into(myHolder.profileIv);
        }

        myHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat_Activity.class);
                intent.putExtra("hisUID", hisUID);
                context.startActivity(intent);
            }
        });

    }

    public void setLastMessageMap(String userId, String lastMessage) {
        lastMessageMap.Set(userId, lastMessage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView profileIv;
        TextView nameTv, lastMessageTv;

        public MyHolder(@NonNull View itemView) {

            super(itemView);

            profileIv = itemView.findViewById(R.id.profileIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            lastMessageTv = itemView.findViewById(R.id.lastMessageTv);
        }

    }





}

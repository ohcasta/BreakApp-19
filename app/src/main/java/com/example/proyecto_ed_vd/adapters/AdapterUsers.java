package com.example.proyecto_ed_vd.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_ed_vd.Chat_Activity;
import com.example.proyecto_ed_vd.HashMapUsers;
import com.example.proyecto_ed_vd.InfoActivity;
import com.example.proyecto_ed_vd.Node;
import com.example.proyecto_ed_vd.R;
import com.example.proyecto_ed_vd.ThereProfileActivity;
import com.example.proyecto_ed_vd.models.ModelUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdapterUsers extends  RecyclerView.Adapter<AdapterUsers.MyHolder>{

    Context context;
    Node userList;

    public AdapterUsers(Context context, Node userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        final Node node = datos(userList,position);
        final String hisUID = node.user.getUid();
        String userImage = node.user.getImage();
        String userName = node.user.getName();
        final String userEmail = node.user.getEmail();

        holder.mNameTv.setText(userName);
        holder.mEmailTv.setText(userEmail);
        try{
            Picasso.get().load(userImage).placeholder(R.drawable.ic_default_img).into(holder.mAvatarIv);
        }
        catch (Exception e){

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setItems(new String[]{"Perfil", "Chat", "Estadisticas"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if(which==0){
                            Intent intent= new Intent(context, ThereProfileActivity.class);
                            intent.putExtra("uid",hisUID);
                            context.startActivity(intent);
                        }
                        if (which==1){
                            Intent intent = new Intent(context, Chat_Activity.class);
                            intent.putExtra("hisUID", hisUID);
                            context.startActivity(intent);
                        }
                        if (which==2){
                            Intent intent = new Intent(context, InfoActivity.class);
                            intent.putExtra("hisUID", hisUID);
                            intent.putExtra("Usuario", node.user);
                            context.startActivity(intent);
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Node start = userList;
        int cont = 0;
        while(start!=null){
            cont++;
            start = start.next;
        }
        return cont;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView mAvatarIv;
        TextView mNameTv, mEmailTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mAvatarIv = itemView.findViewById(R.id.avatarIv);
            mNameTv = itemView.findViewById(R.id.nameTv);
            mEmailTv = itemView.findViewById(R.id.emailTv);
        }
    }

    public Node datos(Node head, int i){
        Node start = head;
        int cont = 0;
        while(cont!=i){
            start = start.next;
            cont++;
        }
        return start;
    }


}

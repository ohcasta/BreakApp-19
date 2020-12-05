package com.example.proyecto_ed_vd.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEventSource;
import android.widget.*;;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_ed_vd.Node;
import com.example.proyecto_ed_vd.Node_chat;
import com.example.proyecto_ed_vd.R;
import com.example.proyecto_ed_vd.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context copntext;
    Node_chat mensajes;
    String imageUrl;

    FirebaseUser fUser;

    public AdapterChat(Context copntext, Node_chat mensajes, String imageUrl) {
        this.copntext = copntext;
        this.mensajes = mensajes;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(copntext).inflate(R.layout.row_chat_right, parent, false);
            return new MyHolder(view);
        }
        else {
            View view = LayoutInflater.from(copntext).inflate(R.layout.row_chat_left, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        String mensaje = datos(mensajes,position).message.message;

        holder.messageTv.setText(mensaje);
        try{
            Picasso.get().load(imageUrl).into(holder.profileIv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_default_img_white).into(holder.profileIv);
        }


        holder.messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(datos(mensajes,position).message.sender.equals(fUser.getUid())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(copntext);
                    builder.setTitle("Eliminar");
                    builder.setMessage("¿Estas seguro de eliminar el mansaje?");
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteMessage(datos(mensajes,position).message);
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });



    }

    private void deleteMessage(final ModelChat message) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Chats");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.child("sender").getValue().toString().equals(fUser.getUid()) && ds.child("message").getValue().toString().equals(message.message)){
                        ds.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        Node_chat start = mensajes;
        int cont = 0;
        while(start!=null){
            cont++;
            start = start.next;
        }
        return cont;
    }

    @Override
    public int getItemViewType(int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if(datos(mensajes,position).message.sender.equals(fUser.getUid())){
            return  MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView profileIv;
        TextView messageTv;
        LinearLayout messageLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileIv = itemView.findViewById(R.id.profileIv);
            messageTv = itemView.findViewById(R.id.messageTv);
            messageLayout = itemView.findViewById(R.id.messageLayout);
        }
    }
    public Node_chat datos(Node_chat head, int i){
        Node_chat start = head;
        int cont = 0;
        while(cont!=i){
            start = start.next;
            cont++;
        }
        return start;
    }

}

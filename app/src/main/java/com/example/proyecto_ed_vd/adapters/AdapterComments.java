package com.example.proyecto_ed_vd.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto_ed_vd.R;
import com.example.proyecto_ed_vd.models.ModelComment;

import com.example.proyecto_ed_vd.Queue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyHolder> {
    Context context;
    Queue<ModelComment> commentList;
    String myUid, postId;

    public AdapterComments(Context context, Queue<ModelComment> commentList, String myUid, String postId) {
        this.context = context;
        this.commentList = commentList;
        this.myUid = myUid;
        this.postId = postId;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comments, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ModelComment aux = dato(commentList,position);
        final String uid = aux.getUid();
        String name = aux.getuName();
        String email = aux.getuEmail();
        String image = aux.getuDp();
        final String cid = aux.getcId();
        String comment = aux.getComment();
        String timeStamp = aux.getTimeStamp();

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        holder.nameTv.setText(name);
        holder.commentTv.setText(comment);
        holder.timeTv.setText(pTime);

        try{
            Picasso.get().load(image).placeholder(R.drawable.ic_default_img).into(holder.avatarIv);
        }catch (Exception e){
            Picasso.get().load(R.drawable.ic_default_img).into(holder.avatarIv);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myUid.equals(uid)){
                    AlertDialog.Builder builder =  new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("Eliminar");
                    builder.setMessage("¿Estas seguro de eliminar el comentario?");
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteComment(cid);
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.create().show();
                }else{
                    Toast.makeText(context, "No puede eliminar comentarios de otros", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteComment(String cid) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.child("Comments").child(cid).removeValue();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String comments = ""+ dataSnapshot.child("pComments").getValue();
                int newCommentVal = Integer.parseInt(comments)-1;
                ref.child("pComments").setValue(""+newCommentVal);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView avatarIv;
        TextView nameTv, commentTv, timeTv;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            avatarIv = itemView.findViewById(R.id.avatarIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            commentTv = itemView.findViewById(R.id.commentTv);
            timeTv = itemView.findViewById(R.id.timeTv);
        }
    }

    public ModelComment dato(Queue<ModelComment> m, int position){
        int cont = 0;
        ModelComment respuesta = new ModelComment();
        Queue<ModelComment> aux = new Queue<ModelComment>();
        while(m.size()!=0){
            if(cont == position){
                respuesta = m.peek();
            }
            aux.enqueue(m.desqueue());
            cont++;
        }
        while (aux.size()!=0){
            m.enqueue(aux.desqueue());
        }
        return  respuesta;
    }

}

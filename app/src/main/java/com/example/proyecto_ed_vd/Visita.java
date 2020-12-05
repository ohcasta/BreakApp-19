package com.example.proyecto_ed_vd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.proyecto_ed_vd.models.ModelUser;
import com.example.proyecto_ed_vd.models.ModelVisit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Visita extends AppCompatActivity {

    MaxHeap maxHeap = new MaxHeap(15);

    ImageView avatarIv, coverIv;
    TextView nameTv, emailTv, phoneTv;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visita);

        avatarIv = findViewById(R.id.RavatarIv);
        coverIv = findViewById(R.id.RcoverIv);
        nameTv = findViewById(R.id.RnameTv);
        emailTv = findViewById(R.id.RemailTv);
        phoneTv = findViewById(R.id.RphoneTv);

        continueBtn = findViewById(R.id.continuarbtn);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        llenar_maxheap();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    ModelUser user_now = null;
                    user_now = maxHeap.extractMax().usuario;
                    acomodar(user_now);

                } catch (Exception e) { }

            }
        });
    }

    private void llenar_maxheap() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot ds : dataSnapshot.getChildren()){
                    DatabaseReference df = FirebaseDatabase.getInstance().getReference("Posts");
                    df.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            int cont = 0;
                            for(DataSnapshot dp : dataSnapshot1.getChildren()){
                                if(ds.child("uid").getValue().equals(dp.child("uid").getValue())){
                                    cont+= Integer.parseInt(dp.child("pLikes").getValue().toString());
                                }
                            }
                            String name = ds.child("name").getValue().toString();       String email = ds.child("email").getValue().toString();
                            String phone = ds.child("phone").getValue().toString();     String tipo_usuario = ds.child("Usuario").getValue().toString();
                            String image = ds.child("image").getValue().toString();     String cover = ds.child("cover").getValue().toString();
                            String uid = ds.child("uid").getValue().toString();

                            if (tipo_usuario.equals("Empresa")) {
                                try {
                                    ModelUser us = new ModelUser(name,email,phone,tipo_usuario,image,cover,uid);
                                    ModelVisit model = new ModelVisit(us, cont);
                                    maxHeap.insert(model);
                                } catch (Exception e) { }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void acomodar(ModelUser model_now) {
        try {

            nameTv.setText(model_now.name);
            emailTv.setText(model_now.email);
            phoneTv.setText(model_now.phone);

            try {
                Picasso.get().load(model_now.image).into(avatarIv);
            }catch(Exception e){
                Picasso.get().load(R.drawable.ic_default_img_white).into(avatarIv);
            }
            try {
                Picasso.get().load(model_now.cover).into(coverIv);
            }catch(Exception e){
                Picasso.get().load(R.drawable.ic_default_img_white).into(coverIv);
            }
        } catch (Exception e) {

        }
    }
}
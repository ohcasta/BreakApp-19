package com.example.proyecto_ed_vd;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.proyecto_ed_vd.adapters.AdapterUsers;
import com.example.proyecto_ed_vd.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class RecomendacionActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRootReference;

    ImageView avatarIv, coverIv;
    TextView nameTv, emailTv, phoneTv;

    Stack<ModelUser> Stack_mostrando = new Stack<ModelUser>();
    Stack<ModelUser> Stack_anterior = new Stack<ModelUser>();

    String cameraPermissions[];
    String storagePermissions[];

    Button siguienteBtn, anteriorBtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomendacion);

        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        avatarIv = findViewById(R.id.RavatarIv);
        coverIv = findViewById(R.id.RcoverIv);
        nameTv = findViewById(R.id.RnameTv);
        emailTv = findViewById(R.id.RemailTv);
        phoneTv = findViewById(R.id.RphoneTv);

        anteriorBtn = findViewById(R.id.anterior);
        siguienteBtn = findViewById(R.id.siguiente);

        Toolbar toolbar = findViewById(R.id.toolbar_recomendacion);
        //setSupportActionBar(toolbar_recomendacion);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getEmpresas();

        //actualizar();

        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pasar_siguiente();
                actualizar();
                }
            }
        );

        anteriorBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pasar_anterior();
                actualizar();
                }
            }
        );


    }

    private void getEmpresas() {

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = getInstance();
        mRootReference  = firebaseDatabase.getReference("Users");

        Query query = mRootReference.orderByChild("email");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    String name = ds.child("name").getValue().toString();       String email = ds.child("email").getValue().toString();
                    String phone = ds.child("phone").getValue().toString();     String tipo_usuario = ds.child("Usuario").getValue().toString();
                    String image = ds.child("image").getValue().toString();     String cover = ds.child("cover").getValue().toString();
                    String uid = ds.child("uid").getValue().toString();

                    if (true) {
                        ModelUser model = new ModelUser(name,email,phone,tipo_usuario,image,cover,uid);

                        Stack_mostrando.push(model);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void pasar_siguiente() {
        if (Stack_mostrando.isEmpty()==false) {
            Stack_anterior.push(Stack_mostrando.pop());
        }
    }

    public void pasar_anterior() {
        if (Stack_anterior.isEmpty()==false) {
            Stack_mostrando.push(Stack_anterior.pop());
        }
    }

    public void actualizar() {

        try {
            ModelUser model_now = Stack_mostrando.peek();

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
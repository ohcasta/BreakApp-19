package com.example.proyecto_ed_vd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.proyecto_ed_vd.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class Busqueda extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference mRootReference;

    Button BuscarBtn;

    ImageView uPictureIv, pImageIv;
    TextView uNameTv, pTimeTv, pTitleTv, pDescriptionTv, pLikesTv;
    EditText valor;

    ModelPost result;
    int num;

    BinarySearchTreePost postTree = new BinarySearchTreePost();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        uPictureIv=findViewById(R.id.uPictureIv);
        pImageIv=findViewById(R.id.pImageIv);
        uNameTv=findViewById(R.id.uNameTv);
        pTimeTv=findViewById(R.id.pTimeTv);
        pTitleTv=findViewById(R.id.pTitleTv);
        pDescriptionTv=findViewById(R.id.pDescriptionTv);
        pLikesTv=findViewById(R.id.pLikesTv);
        BuscarBtn=findViewById(R.id.buscarbtn);
        valor=findViewById(R.id.valor_buscar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getTreePosts();

        BuscarBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    num = Integer.parseInt(valor.getText().toString());

                    result = postTree.search(num);

                    if (result != null) {
                        acomodar(result);
                    }
                } catch (Exception e) { }

            }
        });
    }

    private void getTreePosts() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = getInstance();
        mRootReference  = firebaseDatabase.getReference("Posts");

        Query query = mRootReference.orderByChild("email");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    ModelPost modelPost=ds.getValue(ModelPost.class);

                    postTree.insert(modelPost);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void acomodar(ModelPost post_now) {
        try {
            uNameTv.setText(post_now.getuName());

            String pTimeStamp = post_now.getpTime();

            Calendar calendar=Calendar.getInstance(Locale.getDefault());
            calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
            String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

            pTimeTv.setText(pTime);

            pTitleTv.setText(post_now.getpTitle());
            pDescriptionTv.setText(post_now.getpDescr());

            try {
                Picasso.get().load(post_now.getuDp()).placeholder(R.drawable.ic_default_img).into(uPictureIv);

            }
            catch (Exception e){

            }
            //set post imagen

            //si no hay imagen
            if (post_now.getpImage().equals("noImage")){
                // ocultar ImageView
                pImageIv.setVisibility(View.GONE);
            }
            else{
                try {
                    Picasso.get().load(post_now.getpImage()).into(pImageIv);
                }
                catch (Exception e){

                }

            }
        } catch (Exception e) {

        }
    }


}
package com.example.proyecto_ed_vd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyecto_ed_vd.adapters.AdapterUsers;
import com.example.proyecto_ed_vd.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostLikedByActivity extends AppCompatActivity {

    String postId;

    private RecyclerView recyclerView;
    AdapterUsers adapterUsers;

    private FirebaseAuth firebaseAuth;

    private Node head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_liked_by);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post Liked By");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        actionBar.setSubtitle(firebaseAuth.getCurrentUser().getEmail());

        recyclerView = findViewById(R.id.reciclerview);

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Likes");
        ref.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                head=null;
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String hisUid = ""+ds.getRef().getKey();

                    getUsers(hisUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUsers(String hisUid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(hisUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            String name = ds.child("name").getValue().toString();       String email = ds.child("email").getValue().toString();
                            String phone = ds.child("phone").getValue().toString();     String tipo_usuario = ds.child("Usuario").getValue().toString();
                            String image = ds.child("image").getValue().toString();     String cover = ds.child("cover").getValue().toString();
                            String uid = ds.child("uid").getValue().toString();

                            ModelUser model = new ModelUser(name,email,phone,tipo_usuario,image,cover,uid);
                            head = insert(head, model);
                        }
                        adapterUsers = new AdapterUsers(PostLikedByActivity.this, head);
                        recyclerView.setAdapter(adapterUsers);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    Node insert(Node node, ModelUser user) {
        Node p = new Node(user);
        if (node == null) {
            node = p;
        } else if (node.next == null) {
            node.next = p;
        } else {
            Node start = node;
            while (start.next != null) {
                start = start.next;
            }
            start.next = p;
        }
        return node;
    }
}
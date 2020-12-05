package com.example.proyecto_ed_vd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.proyecto_ed_vd.adapters.AdapterUsers;
import com.example.proyecto_ed_vd.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Ranking extends AppCompatActivity {
    Node head;
    AdapterUsers adapterUsers;
    RecyclerView recyclerView;
    Button b;
    BinarySearchTreeUser usersTree;

    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        recyclerView = findViewById(R.id.rank);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Ranking.this));
        usersTree = new BinarySearchTreeUser();
        head = null;

        llenar_arbol();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b = findViewById(R.id.revelar);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayListUser ranked_users = usersTree.toArray();

                for (int i=ranked_users.size()-1; i>=0; i--) {
                    head = insert(head, ranked_users.get(i));
                }

                adapterUsers = new AdapterUsers(Ranking.this, head);
                recyclerView.setAdapter(adapterUsers);
            }
        });

    }

    private void llenar_arbol() {
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

                            ModelUser model = new ModelUser(name,email,phone,tipo_usuario,image,cover,uid);
                            usersTree.insert(model, cont);
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

    int size(Node node){
        int size = 0;
        Node current = node;
        while(current!=null){
            size++;
            current = current.next;
        }
        return size;
    }


}
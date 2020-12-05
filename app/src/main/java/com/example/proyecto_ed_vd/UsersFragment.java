package com.example.proyecto_ed_vd;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.proyecto_ed_vd.adapters.AdapterUsers;
import com.example.proyecto_ed_vd.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

    RecyclerView recyclerView;
    AdapterUsers adapterUsers;
    FirebaseAuth firebaseAuth;
    Node head;
    HashMapUsers hashUsers;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UsersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = view.findViewById(R.id.users_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        firebaseAuth = FirebaseAuth.getInstance();

        getAllUsers();

        return view;
    }

    private void getAllUsers() {
        head = null;
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ds.child("name").getValue().toString();       String email = ds.child("email").getValue().toString();
                    String phone = ds.child("phone").getValue().toString();     String tipo_usuario = ds.child("Usuario").getValue().toString();
                    String image = ds.child("image").getValue().toString();     String cover = ds.child("cover").getValue().toString();
                    String uid = ds.child("uid").getValue().toString();

                    ModelUser model = new ModelUser(name,email,phone,tipo_usuario,image,cover,uid);

                    if(!uid.equals(fUser.getUid())) {
                        head = insert(head,model);

                    }

                    adapterUsers = new AdapterUsers(getActivity(),head);
                    recyclerView.setAdapter(adapterUsers);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        // ocultar icono addpost de este fragmento
        menu.findItem(R.id.action_add_post).setVisible(false);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim())){
                    searchUsers(s);
                }else {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }

    private void searchUsers(final String query) {
        head = null;
        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ds.child("name").getValue().toString();       String email = ds.child("email").getValue().toString();
                    String phone = ds.child("phone").getValue().toString();     String tipo_usuario = ds.child("Usuario").getValue().toString();
                    String image = ds.child("image").getValue().toString();     String cover = ds.child("cover").getValue().toString();
                    String uid = ds.child("uid").getValue().toString();

                    ModelUser model = new ModelUser(name,email,phone,tipo_usuario,image,cover,uid);

                    if(!uid.equals(fUser.getUid())) {
                        if(model.getName().toLowerCase().contains(query.toLowerCase()) ||
                           model.getEmail().toLowerCase().contains(query.toLowerCase()) ){
                            head = insert(head,model);
                        }
                    }

                    adapterUsers = new AdapterUsers(getActivity(),head);
                    adapterUsers.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterUsers);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkUserStatus() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
        }
        else {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

}
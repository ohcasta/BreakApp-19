package com.example.proyecto_ed_vd;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.SearchView;

import com.example.proyecto_ed_vd.adapters.AdapterChatlist;
import com.example.proyecto_ed_vd.models.ModelChat;
import com.example.proyecto_ed_vd.models.ModelChatlist;
import com.example.proyecto_ed_vd.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ChatListFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    ArrayListChatList chatlistList;
    ArrayListUser userList;
    DatabaseReference reference;
    FirebaseUser currentUser;
    AdapterChatlist adapterChatlist;


    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat_list, container, false);
        firebaseAuth= FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.recyclerView);

        chatlistList = new ArrayListChatList();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(currentUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlistList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelChatlist chatlist = ds.getValue(ModelChatlist.class);
                    chatlistList.add(chatlist);
                }

                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void loadChats() {
        userList = new ArrayListUser();

        reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    String name = ds.child("name").getValue().toString();       String email = ds.child("email").getValue().toString();
                    String phone = ds.child("phone").getValue().toString();     String tipo_usuario = ds.child("Usuario").getValue().toString();
                    String image = ds.child("image").getValue().toString();     String cover = ds.child("cover").getValue().toString();
                    String uid = ds.child("uid").getValue().toString();

                    ModelUser user = new ModelUser(name,email,phone,tipo_usuario,image,cover,uid);

                    for (int i=0; i<chatlistList.size(); i++) {
                        if (user.getUid() != null && user.getUid().equals(chatlistList.get(i).getId())) {
                            userList.add(user);
                            break;
                        }
                    }

                    adapterChatlist = new AdapterChatlist(getContext(), userList);

                    recyclerView.setAdapter(adapterChatlist);

                    for (int i=0; i<userList.size(); i++) {
                        lastMessage(userList.get(i).getUid());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void lastMessage(final String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String theLastMessage = "default";

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat==null) {
                        continue;
                    }
                    String sender = chat.getSender();
                    String receiver = chat.getReceiver();

                    if (sender==null || receiver==null) {
                        continue;
                    }
                    if (chat.getReceiver().equals(currentUser.getUid()) &&
                            chat.getSender().equals(userId) ||
                    chat.getReceiver().equals(userId) && chat.getSender().equals(currentUser.getUid())) {
                        theLastMessage = chat.getMessage();
                    }
                }

                adapterChatlist.setLastMessageMap(userId, theLastMessage);
                adapterChatlist.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        // ocultar icono addpost de este fragmento
        menu.findItem(R.id.action_add_post).setVisible(false);


        super.onCreateOptionsMenu(menu,inflater);
    }

}
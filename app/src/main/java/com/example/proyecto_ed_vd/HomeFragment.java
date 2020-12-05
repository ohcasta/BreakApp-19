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
import android.widget.Toast;

import com.example.proyecto_ed_vd.adapters.AdapterPosts;
import com.example.proyecto_ed_vd.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    FirebaseAuth firebaseAuth;

    RecyclerView recyclerView;
    ArrayListClass postList;
    AdapterPosts adapterPosts;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        firebaseAuth = FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        //recycler view y propiedades
        recyclerView=view.findViewById(R.id.postsRecyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        //Mostrar los post mas recientes primero
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        // set layout
        recyclerView.setLayoutManager(layoutManager);

        //init post list
        postList=new ArrayListClass();
        loadPosts();

        return view;
    }

    private void loadPosts() {
        //path de todos los posts
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        //get data de la ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost=ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                    //adapter
                    adapterPosts=new AdapterPosts(getActivity(),postList);
                    //
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void searchPosts(final String searchQuery){

        //path de todos los posts
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        //get data de la ref
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    ModelPost modelPost=ds.getValue(ModelPost.class);

                    if (modelPost.getpTitle().toLowerCase().contains(searchQuery.toLowerCase()) || modelPost.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())){
                        postList.add(modelPost);
                    }
                    //adapter
                    adapterPosts=new AdapterPosts(getActivity(),postList);
                    //
                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //en caso de error
                Toast.makeText(getActivity(),""+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        //SearchView para buscar posts por  titulo o descripcion
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(item);

        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //cuando el usuario presiona el boton
                if (!TextUtils.isEmpty(s)){
                    searchPosts(s);
                }
                else{
                    loadPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //cuando ele usuario presiona cualquier letra
                if (!TextUtils.isEmpty(s)){
                    searchPosts(s);
                }
                else{
                    loadPosts();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        if (id == R.id.action_add_post) {
            startActivity(new Intent(getActivity(),AddPostActivity.class));
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
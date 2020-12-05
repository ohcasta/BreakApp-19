package com.example.proyecto_ed_vd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText mEmailEt, mPasswordEt;
    TextView notHaveAccountTv;
    Button mLoginBtn;

    private FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mLoginBtn = findViewById(R.id.loginBtn);
        notHaveAccountTv = findViewById(R.id.nothave_accountTv);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEt.getText().toString();
                String passw = mPasswordEt.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmailEt.setError("Email invalido");
                    mEmailEt.setFocusable(true);
                }
                else {
                    loginUser(email, passw);
                }

            }
        });

        notHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        pd = new ProgressDialog(this);
        pd.setMessage("Ingresando...");

    }

    private void loginUser(String email, String passw) {
        pd.show();
        mAuth.signInWithEmailAndPassword(email, passw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(task.getResult().getAdditionalUserInfo().isNewUser()){
                                //Obtenemos el email y uid de auth
                                String email = user.getEmail();
                                String uid = user.getUid();
                                //Guardamos la información personal del usuario en la base de datos en tiempo real tambien y para ellos usamos un HashMap
                                HashMap<Object,String> hashMap = new HashMap<>();
                                hashMap.put("email",email);
                                hashMap.put("uid",uid);
                                //La siguiente información puede llenarse en la edición del perfil del usuario
                                hashMap.put("name","");
                                hashMap.put("phone","");
                                hashMap.put("image","");
                                hashMap.put("cover","");
                                //Guardamos esta información en la base de datos en tiempo real
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference mRootReference = database.getReference("Users");
                                mRootReference.child(uid).setValue(hashMap);
                            }

                            startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                            finish();
                        } else {
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "Fallo en autenticacion",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp () {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
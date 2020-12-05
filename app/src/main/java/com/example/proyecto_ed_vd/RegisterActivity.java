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
import android.widget.CheckBox;
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

public class RegisterActivity extends AppCompatActivity {

    EditText mEmailEt, mPasswordEt;
    Button mRegisterBtn;
    TextView mHaveAccountTv;
    CheckBox caja;

    ProgressDialog progressDialog;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Crear cuenta");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mHaveAccountTv = findViewById(R.id.have_accountTv);
        caja = findViewById(R.id.checkBox);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando usuario...");

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmailEt.setError("Email Invalido");
                    mEmailEt.setFocusable(true);
                }
                else if (password.length()<6) {
                    mPasswordEt.setError("La Password debe tener al menos 6 caracteres");
                    mPasswordEt.setFocusable(true);
                }
                else {
                    registerUser(email, password);
                }

            }
        });

        mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //Obtenemos el email y uid de auth
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String verificación = caja.getText().toString();
                            //Guardamos la información personal del usuario en la base de datos en tiempo real tambien y para ellos usamos un HashMap
                            HashMap<Object,String> hashMap = new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            //La siguiente información puede llenarse en la edición del perfil del usuario
                            hashMap.put("name","");
                            hashMap.put("phone","");
                            hashMap.put("image","");
                            hashMap.put("cover","");
                            if(caja.isChecked()){
                                hashMap.put("Usuario","Empresa");
                            }
                            else{
                                hashMap.put("Usuario","Común");
                            }
                            //Guardamos esta información en la base de datos en tiempo real
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mRootReference = database.getReference("Users");
                            mRootReference.child(uid).setValue(hashMap);

                            Toast.makeText(RegisterActivity.this, "Registrado... \n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, DashBoardActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Fallo de Autenticacion.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

                Toast.makeText(RegisterActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp () {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
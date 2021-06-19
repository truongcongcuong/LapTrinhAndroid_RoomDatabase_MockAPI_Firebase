package com.example.laptrinhandroid_roomdatabase_mockapi_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button btn_main_login, btn_main_registry;
    EditText edt_main_user, edt_main_password;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        btn_main_login = findViewById(R.id.btn_main_login);
        btn_main_registry = findViewById(R.id.btn_main_registry);
        edt_main_user = findViewById(R.id.edt_main_user);
        edt_main_password = findViewById(R.id.edt_main_password);

        edt_main_user.setText("18084791_TruongCongCuong@gmail.com");
        edt_main_password.setText("123456");

        btn_main_login.setOnClickListener(v->{
            if(validData())
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful())
                                    Toast.makeText(MainActivity.this,"Dang nhap that bai.....",Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(MainActivity.this,"Dang nhap thanh cong.....",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this,CRUDActivity.class));
                                }
                            }
                        });
        });

        btn_main_registry.setOnClickListener(v->{
            if(validData())
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(MainActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful())
                                    Toast.makeText(MainActivity.this,"Dang ky that bai.....",Toast.LENGTH_SHORT).show();
                                else{
                                    Toast.makeText(MainActivity.this,"Dang ky thanh cong.....",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(MainActivity.this,CRUDActivity.class));
                                }
                            }
                        });
        });
    }

    private boolean validData() {
        this.email = edt_main_user.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(MainActivity.this,"Nhap Email.....",Toast.LENGTH_SHORT).show();
            return false;
        }
        this.password = edt_main_password.getText().toString();
        if(TextUtils.isEmpty(password)){
            Toast.makeText(MainActivity.this,"Nhap pass.....",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
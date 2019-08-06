package com.aptitude.challengetwo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterActivity extends AppCompatActivity {
    EditText email,password, name;
    Button registerButton;
    FirebaseAuth firebaseAuth;
    LinearLayout progresslayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        email = (EditText) findViewById(R.id.edt_email);
        password = (EditText) findViewById(R.id.edt_pass);
        name = findViewById(R.id.edt_name);
        registerButton = (Button) findViewById(R.id.signUpButton);
        progresslayout = findViewById(R.id.progresslayout);

        firebaseAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_text = email.getText().toString();
                String password_text = password.getText().toString();
                final String fullname_text = name.getText().toString();

                if(TextUtils.isEmpty(email_text)){
                    Toast.makeText(RegisterActivity.this,"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(fullname_text)){
                    Toast.makeText(RegisterActivity.this,"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password_text)){
                    Toast.makeText(RegisterActivity.this,"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password_text.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password must be at least 6 characters",Toast.LENGTH_SHORT).show();
                    return;
                }
                progresslayout.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email_text,password_text)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    saveDetails(fullname_text);
                                }
                                else{
                                    progresslayout.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this,"E-mail or password is wrong",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

//        if(firebaseAuth.getCurrentUser()!=null){
//            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
//        }
    }

    private void saveDetails(String fullname){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullname)
                .build();

        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progresslayout.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(RegisterActivity.this,"Registration failed, please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}

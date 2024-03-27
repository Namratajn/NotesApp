package com.piyu.wrapyournotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    private EditText msignupEmail , msignupPassword;
    private RelativeLayout msignup ;
    private TextView mgotologin;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // getSupportActionBar().hide();

        mgotologin=findViewById(R.id.gotologin);
        msignup= findViewById(R.id.signup);
        msignupEmail=findViewById(R.id.signupEmail);
        msignupPassword=findViewById(R.id.signupPass);

        firebaseAuth=FirebaseAuth.getInstance();



        mgotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, MainActivity.class);
                startActivity(intent);
            }
        });

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String mail = msignupEmail.getText().toString().trim();
               String password = msignupPassword.getText().toString().trim();
               
               if(mail.isEmpty() || password.isEmpty()){

                   Toast.makeText(signup.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
               }
               else if (password.length()<8){
                   Toast.makeText(signup.this, "Password should contain at least 8 letters ", Toast.LENGTH_SHORT).show();
               }
               else {
                   /// register the user to firebase
                   firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {

                           if(task.isSuccessful()){
                               Toast.makeText(signup.this, "Registration Succesfull", Toast.LENGTH_SHORT).show();
                               sendEmailVerification();
                           }
                           else{
                               Toast.makeText(signup.this, "failed to register", Toast.LENGTH_SHORT).show();
                           }

                       }
                   });
               }
            }
        });

    }

    // send Email verification
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser !=null){
             firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     Toast.makeText(signup.this, "verification email is Sent,Verify and log in Again", Toast.LENGTH_SHORT).show();
                     firebaseAuth.signOut();
                     finish();
                     startActivity(new Intent(signup.this,MainActivity.class));
                     
                 }
             });
        }
        else{
            Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
        }

    }
}
package com.example.sitter;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText email_txt;
    private EditText Password_txt;
    private TextView mStatusTextView;
    private TextView _signupLink;
    private Button LogIn_btn;

    private FirebaseAuth mAuth;
    constants c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();
        // Set up the login form.
        mStatusTextView = findViewById(R.id.status);
        email_txt = findViewById(R.id.email);
        Password_txt =  findViewById(R.id.password);
        _signupLink =  findViewById(R.id.link_signup);
        LogIn_btn =  findViewById(R.id.log_in);

        LogIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogIn();

            }
        });
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), signUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();

            }
        });

    }


    private void startLogIn() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        LogIn_btn.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


     String email = email_txt.getText().toString();
     String password = Password_txt.getText().toString();

         mAuth.signInWithEmailAndPassword(email, password)

                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                     @Override

                     public void onComplete(@NonNull Task<AuthResult> task) {

                         if (task.isSuccessful()) {

                             FirebaseUser user = mAuth.getCurrentUser();
                             mStatusTextView.setText(R.string.auth_successful);
                             Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                             startActivity(intent);


                         } else {

                             mStatusTextView.setText(R.string.auth_failed);

                             if (!task.isSuccessful()) {

                                 mStatusTextView.setText(R.string.auth_failed);

                             }


                         }
                     }

                 });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
  }

    public void onLoginSuccess() {
        LogIn_btn.setEnabled(true);

        finish();

    }
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        LogIn_btn.setEnabled(true);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
    public boolean validate() {
        boolean valid = true;

        String email = email_txt.getText().toString();
        String password = Password_txt.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_txt.setError("enter a valid email address");
            valid = false;
        } else {
            email_txt.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            Password_txt.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            Password_txt.setError(null);
        }

        return valid;
    }


    }


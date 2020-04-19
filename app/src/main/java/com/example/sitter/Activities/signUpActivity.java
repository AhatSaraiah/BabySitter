package com.example.sitter.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sitter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signUpActivity extends AppCompatActivity {

    private EditText email_txt;
    private EditText Password_txt;
    private EditText reEnterPassword_txt;
    private TextView loginLink;
    private RadioButton signParent;
    private RadioButton signSitter;
    private Button signUp;
    private boolean failed=false;
    private static final String TAG = "SignupActivity";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        email_txt = findViewById(R.id.email);
        reEnterPassword_txt = findViewById(R.id.reEnterPassword);
        Password_txt =  findViewById(R.id.password);
        loginLink =  findViewById(R.id.link_login);
        signParent = findViewById(R.id.radioB_Parent);
        signSitter = findViewById(R.id.radioB_Sitter);
        signUp = findViewById(R.id.sign_up);

         signUp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 signup();
                 if ((signParent.isChecked() ||signSitter.isChecked())&& validate()) {
                     Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
                     startActivity(intent);

                 }
//
                 else if(failed){
                     onSignupFailed();
                 }

             }
         });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }


    public void signup() {
        failed=false;
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signUp.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(signUpActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String email = email_txt.getText().toString();
        String password = Password_txt.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success

                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            failed=true;

                        }

                    }

                });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // depending on success
                            onSignupSuccess();

                       // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 5000);

    }
    public void onSignupSuccess() {
        signUp.setEnabled(true);
        setResult(RESULT_OK, null);
       failed=false;
        finish();
    }

    public boolean validate() {
        boolean valid = true;

        String email = email_txt.getText().toString();
        String password = Password_txt.getText().toString();
        String repassword = reEnterPassword_txt.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_txt.setError("enter a valid email address");
            valid = false;
        } else {
            email_txt.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            Password_txt.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }else
            Password_txt.setError(null);

        if (repassword.isEmpty() || !repassword.contentEquals(password)) {
            reEnterPassword_txt.setError("doesn't match password ");
            valid = false;
        }else {
            reEnterPassword_txt.setError(null);
        }


        return valid;
    }
    private void signOut() {

        mAuth.signOut();

    }
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
        signUp.setEnabled(true);
    }

}
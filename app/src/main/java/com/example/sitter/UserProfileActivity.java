package com.example.sitter;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    private TextView userName, userProfName, useraboutMe, userCity, userGender,userDOB;
    private CircleImageView userProfileImage;
    private  Button sendMessage_btn;
    private DatabaseReference  UsersRef;
    private FirebaseAuth mAuth;
  //  private String saveCurrentDate;
    private String CurrentUserId,receviverUserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();
        CurrentUserId = mAuth.getCurrentUser().getUid();

        receviverUserId = getIntent().getExtras().get("visit_user_id").toString();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        IntializeFields();

        UsersRef.child(receviverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String myUserName = dataSnapshot.child("username").getValue().toString();
                    String myProfileName = dataSnapshot.child("fullname").getValue().toString();
                    String aboutMe = Objects.requireNonNull(dataSnapshot.child("aboutMe").getValue()).toString();
                    String myDOB = dataSnapshot.child("dob").getValue().toString();
                    String myCity = dataSnapshot.child("city").getValue().toString();
                    String myGender = dataSnapshot.child("gender").getValue().toString();

                    Picasso.with(UserProfileActivity.this).load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    userName.setText("@" + myUserName);
                    userProfName.setText(myProfileName);
                    useraboutMe.setText(aboutMe);
                    userDOB.setText("DOB: " + myDOB);
                    userCity.setText("Country: " + myCity);
                    userGender.setText("Gender: " + myGender);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void IntializeFields() {
        userName = findViewById(R.id.person_username);
        userProfName= findViewById(R.id.person_full_name);
        useraboutMe = findViewById(R.id.user_details);
        userCity = findViewById(R.id.user_city);
        userGender = findViewById(R.id.user_gender);
        userDOB = findViewById(R.id.user_dob);
        userProfileImage = findViewById(R.id.user_profile_pic);
        sendMessage_btn=findViewById(R.id.send_message_btn);

//        sendMessage_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SendUserToMessagesActivity();
//            }
//        });

    }

//    private void SendUserToMessagesActivity()
//    {
//        Intent FindSitterIntent = new Intent(UserProfileActivity.this, MessagesActivity.class);
//        startActivity(FindSitterIntent);
//    }

}

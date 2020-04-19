package com.example.sitter.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sitter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    private TextView userName, userProfName, useraboutMe, userCity;
    private CircleImageView userProfileImage;
    private  Button sendMessage_btn;
    private DatabaseReference  UsersRef;
    private FirebaseAuth mAuth;
  MaterialCalendarView materialCalendarView;

    private String senderUserid, receviverUserId;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        senderUserid = mAuth.getCurrentUser().getUid();

        receviverUserId = getIntent().getExtras().get("visit_user_id").toString();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        materialCalendarView = findViewById(R.id.calendarView);

        IntializeFields();

        UsersRef.child(receviverUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String myUserName = dataSnapshot.child("username").getValue().toString();
                    String myProfileName = dataSnapshot.child("fullname").getValue().toString();
                    String myCity = dataSnapshot.child("city").getValue().toString();
                    String aboutMe = dataSnapshot.child("aboutMe").getValue().toString();

                    Picasso.with(UserProfileActivity.this).load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    userProfName.setText(myProfileName);
                    userName.setText("@" + myUserName);
                    userCity.setText("City: " + myCity);
                    useraboutMe.setText("About me:"+ aboutMe);
                    getSupportActionBar().setTitle(userProfName.getText().toString());

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
        userProfileImage = findViewById(R.id.user_profile_pic);
        sendMessage_btn=findViewById(R.id.send_message_btn);

        sendMessage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToChatActivity();
            }
        });

    }

    private void SendUserToChatActivity()
    {
        Intent Chatintent = new Intent(UserProfileActivity.this, ChatActivity.class);
        Chatintent.putExtra("visit_user_id",receviverUserId);
        Chatintent.putExtra("userName",userName.getText().toString());
        startActivity(Chatintent);

    }

}

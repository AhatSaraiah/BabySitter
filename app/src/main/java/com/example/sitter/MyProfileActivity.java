package com.example.sitter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfileActivity extends AppCompatActivity {
    private TextView userName, userProfName, aboutMe, userCity, userGender;
    private CircleImageView userProfileImage;

    private DatabaseReference profileUserRef, FriendsRef, PostsRef;
    private FirebaseAuth mAuth;
    private Button MyPosts;

    private String currentUserid;
    private int countPosts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Intent intent = getIntent();

        mAuth = FirebaseAuth.getInstance();
        currentUserid = mAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);
        FriendsRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

         userName = findViewById(R.id.my_username);
       //  userName.setText(intent.getStringExtra("USER_NAME"));
         userProfName= findViewById(R.id.my_full_name);
       //  userName.setText(intent.getStringExtra("FULL_NAME"));

        aboutMe = findViewById(R.id.about_me);
      //  userName.setText(intent.getStringExtra("ABOUT_ME"));

        userCity =findViewById(R.id.my_city);
      //  userName.setText(intent.getStringExtra("CITY"));

        userProfileImage = findViewById(R.id.my_profile_pic);
     //   MyPosts =findViewById(R.id.my_post_button);

//
//        MyPosts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                SendUsertoMyPostsActivity();
//            }
//        });

//        PostsRef.orderByChild("uid")
//                .startAt(currentUserid).endAt(currentUserid + "\uf8ff")
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists())
//                        {
//                      //      countPosts = (int) dataSnapshot.getChildrenCount();
//                       //     MyPosts.setText(Integer.toString(countPosts) + "  Posts");
//                        }
//                        else
//                        {
//                            MyPosts.setText("0  Posts");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });



        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String myProfileImage = dataSnapshot.child("profileimage").getValue().toString();
                    String myUserName = dataSnapshot.child("username").getValue().toString();
                    String myProfileName = dataSnapshot.child("fullname").getValue().toString();
                    String myCity = dataSnapshot.child("city").getValue().toString();
                    String moreAboutMe= dataSnapshot.child("aboutMe").getValue().toString();

                    Picasso.with(MyProfileActivity.this).load(myProfileImage).placeholder(R.drawable.profile).into(userProfileImage);
                    userName.setText("@" + myUserName);
                    userProfName.setText(myProfileName);
                   aboutMe.setText(moreAboutMe);
                    userCity.setText("City: " + myCity);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


//    private void SendUsertoMyPostsActivity() {
//        Intent settingsIntent =  new Intent(MyProfileActivity.this, MyPostsActivity.class);
//        startActivity(settingsIntent);
//    }
}

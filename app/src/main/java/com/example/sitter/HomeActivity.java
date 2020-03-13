package com.example.sitter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private static final String REQUIRED = "Required";

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView postList;
    private Toolbar mToolbar;

    private FirebaseRecyclerAdapter adapter;
    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef,LikesRef;
    private DatabaseReference CommentsRef;
    private StorageReference UserProfileImageRef;

    String currentUserID;

    private RecyclerView postsList;
    Boolean LikeChecker = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        drawerLayout = findViewById(R.id.drw);
        navigationView = findViewById(R.id.navigation);
        postsList = findViewById(R.id.all_users_posts);
   //     postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postsList.setLayoutManager(linearLayoutManager);

        mToolbar = (Toolbar) findViewById(R.id.home_toolbar);
           setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View navView = navigationView.inflateHeaderView(R.layout.header_nav);

        NavProfileImage = (CircleImageView) navView.findViewById(R.id.nav_profile_image);
        NavProfileUserName = (TextView) navView.findViewById(R.id.nav_user_full_name);

        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.hasChild("fullname"))
                    {
                        String fullname = dataSnapshot.child("fullname").getValue().toString();
                        NavProfileUserName.setText(fullname);
                    }
                    if(dataSnapshot.hasChild("profileimage"))
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(HomeActivity.this).load(image).placeholder(R.drawable.profile).into(NavProfileImage);
                    }
                    else
                    {
                        Toast.makeText(HomeActivity.this, "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });

        DisplayAllUsersPosts();
    }

    private void DisplayAllUsersPosts() {
        Query SortPostsInDecendingOrder = PostsRef.orderByChild("counter");

        FirebaseRecyclerOptions<Post> options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(SortPostsInDecendingOrder, Post.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Post, PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Post model) {
                final String PostKey = getRef(position).getKey();

                holder.setFullname(model.getFullname());
                holder.setTime(model.getTime());
                holder.setDate(model.getDate());
                holder.setDescription(model.getDescription());
                holder.setProfileimage(getApplicationContext(), model.getProfileimage());

               // holder.setLikeButtonStatus(PostKey);
//                holder.mView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent clickPostIntent = new Intent(HomeActivity.this, ClickPostActivity.class);
//                        clickPostIntent.putExtra("PostKey", PostKey);
//                        startActivity(clickPostIntent);
//                    }
//                });

//                holder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent commentsIntent = new Intent(HomeActivity.this, CommentsActivity.class);
//                        commentsIntent.putExtra("PostKey", PostKey);
//                        startActivity(commentsIntent);
//                    }
//                });

//                holder.LikePostButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        LikeChecker = true;
//
//                        LikesRef.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                if (LikeChecker == true) {
//                                    if (dataSnapshot.child(PostKey).hasChild(currentUserID)) {
//                                        LikesRef.child(PostKey).child(currentUserID).removeValue();
//                                        LikeChecker = false;
//                                    } else {
//                                        LikesRef.child(PostKey).child(currentUserID).setValue(true);
//                                        LikeChecker = false;
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//                });

            }

            @Override
            public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    //
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_post, parent, false);

                return new PostsViewHolder(view);
            }
        };

        postsList.setAdapter(adapter);

       // updateUserStatus("online");
        }




    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        ImageButton LikePostButton, CommentPostButton;
        TextView DisplayNoOfLikes;
        int countLikes;
        String currentUserID;
        DatabaseReference LikesRef;

        public PostsViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;

//           LikePostButton = (ImageButton) mView.findViewById(R.id.like_button);
//            CommentPostButton = (ImageButton) mView.findViewById(R.id.comment_button);
//            DisplayNoOfLikes = (TextView) mView.findViewById(R.id.dislike_no_of_like);

            LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        }

//        public void setLikeButtonStatus(final String PostKey)
//        {
//            LikesRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if(dataSnapshot.child(PostKey).hasChild(currentUserID))
//                    {
//                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
//                    //    LikePostButton.setImageResource(R.drawable.like);
//                        DisplayNoOfLikes.setText((Integer.toString(countLikes) + " Likes"));
//                    }
//                    else
//                    {
//                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
//                      //  LikePostButton.setImageResource(R.drawable.dislike);
//                        DisplayNoOfLikes.setText((Integer.toString(countLikes) + " Likes"));
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }

        public void setFullname(String fullname)
        {
            TextView username = (TextView) mView.findViewById(R.id.postAuthor);
            username.setText(fullname);
        }

        public void setProfileimage(Context ctx, String profileimage)
        {
            CircleImageView image = (CircleImageView) mView.findViewById(R.id.nav_profile_image);
            Picasso.with(ctx).load(profileimage).into(image);
        }

        public void setTime(String time)
        {
            TextView PostTime = (TextView) mView.findViewById(R.id.post_time);
            PostTime.setText("    " + time);
        }

        public void setDate(String date)
        {
            TextView PostDate = (TextView) mView.findViewById(R.id.post_date);
            PostDate.setText("    " + date);
        }

        public void setDescription(String description)
        {
            //
            TextView PostDescription = (TextView) mView.findViewById(R.id.postBody);
            PostDescription.setText(description);
        }

       }

        public  void updateUserStatus(String state)
        {
            String saveCurrentDate, saveCurrentTime;

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTime= new SimpleDateFormat("hh:mm a");
            saveCurrentTime = currentTime.format(calForTime.getTime());

            Map currentMap = new HashMap<>();
            currentMap.put("time", saveCurrentTime);
            currentMap.put("date", saveCurrentDate);
            currentMap.put("type", state);

            UsersRef.child(currentUserID).child("userState")
                    .updateChildren(currentMap);

        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void UserMenuSelector(MenuItem item) {
          String id;
        switch (item.getItemId()) {

            case R.id.home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_profile:
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                SendUserToMyProfileActivity();
                break;
            case R.id.nav_search:
                    SendUserToFindSitterActivity();
                break;
//            case R.id.nav_friends:
//                break;
//            case R.id.nav_messages:
//                break;
//            case R.id.nav_settings:
//                break;
            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToLoginActivity();
                break;
           }
        }



    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null)
        {
            SendUserToLoginActivity();
        }
        else
        {
            CheckUserExistence();
        }
    }



    private void CheckUserExistence() {

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            final String current_user_id = user.getUid();

            UsersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(current_user_id)) {
                        //   SendUserToSetupActivity();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            // User is not signed in
            // Do some stuff
        }
     }
    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
    private void SendUserToFindSitterActivity()
    {
        Intent FindSitterIntent = new Intent(HomeActivity.this, FindSitterActivity.class);
        startActivity(FindSitterIntent);
    }

    private void SendUserToMyProfileActivity()
    {
        Intent ProfileIntent = new Intent(HomeActivity.this, MyProfileActivity.class);
        startActivity(ProfileIntent);
    }

}

package com.example.sitter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

 public class FindSitterActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private TextView simpleText;
    private Button SearchButton;
    private SearchView SearchInputText;

    private RecyclerView SearchResultList;
    private DatabaseReference allUsersDatabaseRef;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    ArrayList<FindSitter> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_sitter);

        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Find Sitters");

        SearchResultList =findViewById(R.id.search_result_list);
        SearchResultList.setHasFixedSize(true);
        SearchResultList.setLayoutManager(new LinearLayoutManager(this));

        SearchButton = findViewById(R.id.search_people_button);
        simpleText = findViewById(R.id.simpleTextID);
        SearchInputText =  findViewById(R.id.search_box_input);

        SearchSitters("search");
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchBoxInput = SearchInputText.toString();
                SearchSitters(searchBoxInput);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(allUsersDatabaseRef != null)
        {
            allUsersDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        list = new ArrayList<FindSitter>();
                        for(DataSnapshot ds: dataSnapshot.getChildren())
                        {
                            FindSitter sitter;
                            sitter = new  FindSitter(ds.child("profileimage").getValue(String.class),ds.child("fullname").getValue(String.class),ds.child("aboutMe").getValue(String.class),ds.getKey());
                            list.add(sitter);



                        }
                        Adapter adapterClass = new Adapter(list, getApplicationContext());
                        SearchResultList.setAdapter(adapterClass);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        if(SearchInputText != null)
        {
            SearchInputText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

        firebaseRecyclerAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    private void search(String str)
    {
        ArrayList<FindSitter> myList = new ArrayList<>();
        for(FindSitter ob: list)
        {
            if(ob.fullname.toLowerCase().contains(str.toLowerCase()))
            {
                myList.add(ob);
            }
        }
        Adapter adapterClass = new Adapter(myList, getApplicationContext());
        SearchResultList.setAdapter(adapterClass);
    }

    private void SearchSitters(String searchBoxInput)
    {
        Query searchPeopleQuery = allUsersDatabaseRef.orderByChild("city")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");

        FirebaseRecyclerOptions<FindSitter> options =
                new FirebaseRecyclerOptions.Builder<FindSitter>()
                        .setQuery(searchPeopleQuery, FindSitter.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindSitter, FindSitterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindSitterViewHolder holder, final int position, @NonNull FindSitter model) {
                holder.setFullname(model.getFullname());
                holder.setAboutMe(model.getAboutMe());

                holder.setProfileimage(getApplicationContext(), model.getProfileimage());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String visit_user_id = getRef(position).getKey();

                        Intent profileIntent = new Intent(FindSitterActivity.this, UserProfileActivity.class);
                        profileIntent.putExtra("visit_user_id", visit_user_id);
                        startActivity(profileIntent);
                    }
                });
            }

            @Override
            public FindSitterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.all_users_layout, parent, false);

                return new FindSitterViewHolder(view);
            }
        };

        SearchResultList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class FindSitterViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public FindSitterViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setProfileimage(Context ctx, String profileiamge) {
            CircleImageView myImage =  mView.findViewById(R.id.nav_profile_image);
            Picasso.with(ctx).load(profileiamge).placeholder(R.drawable.profile).into(myImage);
        }

        public void setFullname(String fullname) {
            TextView myName = mView.findViewById(R.id.all_users_profile_full_name);
            myName.setText(fullname);
        }

        public void setAboutMe(String aboutme) {
            TextView aboutMe = mView.findViewById(R.id.all_users_aboutme);
            aboutMe.setText(aboutme);
        }

    }

}


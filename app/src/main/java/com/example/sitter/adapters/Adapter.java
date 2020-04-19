package com.example.sitter.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sitter.Activities.UserProfileActivity;
import com.example.sitter.models.FindSitter;
import com.example.sitter.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter extends RecyclerView.Adapter<Adapter.FindFriendsViewHolder> {
    ArrayList<FindSitter> list;
    Context ct;

    public Adapter(ArrayList<FindSitter> list, Context ct) {
        this.list = list;
        this.ct = ct;
    }

    @NonNull
    @Override
    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_layout, parent, false);
        return new FindFriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.FindFriendsViewHolder holder, final int position) {
        holder.setFullname(list.get(position).getFullname());
        holder.setAbout(list.get(position).getAboutMe());
        holder.setProfileimage(ct, list.get(position).getProfileimage());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String visit_user_id = list.get(position).userID;
                Toast.makeText(ct,visit_user_id, Toast.LENGTH_LONG ).show();
                Intent profileIntent = new Intent(ct, UserProfileActivity.class);
                profileIntent.putExtra("visit_user_id", visit_user_id);
                ct.startActivity(profileIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class FindFriendsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public FindFriendsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setProfileimage(Context ctx, String profileiamge) {
            CircleImageView myImage = mView.findViewById(R.id.nav_profile_image);
            Picasso.with(ctx).load(profileiamge).placeholder(R.drawable.profile).into(myImage);
        }

        public void setFullname(String fullname) {
            TextView myName = mView.findViewById(R.id.all_users_profile_full_name);
            myName.setText(fullname);
        }

        public void setAbout(String aboutme) {
            TextView about =  mView.findViewById(R.id.all_users_aboutme);
            about.setText(aboutme);
        }
    }
}
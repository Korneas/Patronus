package com.example.camilomontoya.patronus.Friends;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camilomontoya.patronus.R;
import com.example.camilomontoya.patronus.Utils.CurrentUser;
import com.example.camilomontoya.patronus.Utils.Typo;
import com.example.camilomontoya.patronus.Utils.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Camilo Montoya on 11/19/2017.
 */

public class FriendsFragment extends Fragment {
    private static final String TAG = "MapFragment";

    private TextView titleToolbar,noFriends;
    private ImageView addFriend;

    private RecyclerView friends;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        titleToolbar = view.findViewById(R.id.toolbar_friends_title);
        titleToolbar.setTypeface(Typo.getInstance().getTitle());

        noFriends = view.findViewById(R.id.no_friends);
        noFriends.setTypeface(Typo.getInstance().getContent());

        List allFriends = new ArrayList();

        if(CurrentUser.getRef().getFriends().size() > 0) {
            for(User item: CurrentUser.getRef().getFriends()){
                allFriends.add(new FriendItem(item.getProfilePic(),item.getName(),item.getEmail(), ""));
            }
        }

        if(allFriends.size() > 0) {
            noFriends.setText("");
            noFriends.setVisibility(View.INVISIBLE);
        }

        friends = view.findViewById(R.id.friends_recycler);
        friends.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        friends.setLayoutManager(layoutManager);

        adapter = new FriendsAdapter(allFriends, new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FriendItem item) {

            }
        });

        friends.setAdapter(adapter);

        addFriend = view.findViewById(R.id.add_friends_btn);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddFriendActivity.class));
            }
        });

        return view;
    }
}

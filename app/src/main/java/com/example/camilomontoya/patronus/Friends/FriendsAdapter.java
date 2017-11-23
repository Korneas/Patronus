package com.example.camilomontoya.patronus.Friends;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.camilomontoya.patronus.R;
import com.example.camilomontoya.patronus.Utils.Typo;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Camilo Montoya on 11/23/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendsViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(FriendItem item);
    }

    private final List<FriendItem> items;
    private final OnItemClickListener listener;

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView img;
        public TextView name;

        public FriendsViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.friend_profile_pic);
            name = itemView.findViewById(R.id.friend_name);
        }

        public void bind(final FriendItem item,final OnItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public FriendsAdapter(List<FriendItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_friend, viewGroup, false);
        return new FriendsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FriendsViewHolder viewHolder, int i) {
        ImageLoader.getInstance().displayImage(items.get(i).getProfilePicUrl(),viewHolder.img);
        viewHolder.name.setText(items.get(i).getName());
        viewHolder.name.setTypeface(Typo.getInstance().getContent());
        viewHolder.bind(items.get(i),listener);
    }
}
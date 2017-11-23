package com.example.camilomontoya.patronus.Map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.camilomontoya.patronus.Friends.FriendItem;
import com.example.camilomontoya.patronus.R;
import com.example.camilomontoya.patronus.Utils.Typo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Camilo Montoya on 11/23/2017.
 */

public class FriendsChooseAdapter extends RecyclerView.Adapter<FriendsChooseAdapter.FriendsChooseViewHolder> {

    private final List<FriendChooseItem> items;

    public static class FriendsChooseViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView img;
        public TextView name;
        public CheckBox checkBox;

        public FriendsChooseViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.friend_profile_pic);
            name = itemView.findViewById(R.id.friend_name);
            checkBox = itemView.findViewById(R.id.friend_checkbox);
        }

        public void bind(final FriendChooseItem item){
        }
    }

    public FriendsChooseAdapter(List<FriendChooseItem> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public FriendsChooseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_friend_check, viewGroup, false);
        return new FriendsChooseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FriendsChooseViewHolder viewHolder, final int i) {
        ImageLoader.getInstance().displayImage(items.get(i).getProfilePicUrl(),viewHolder.img);
        viewHolder.name.setText(items.get(i).getName());
        viewHolder.name.setTypeface(Typo.getInstance().getContent());
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.get(i).setChoosed(viewHolder.checkBox.isChecked());
            }
        });
    }
}
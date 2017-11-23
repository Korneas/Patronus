package com.example.camilomontoya.patronus.Friends;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilomontoya.patronus.R;
import com.example.camilomontoya.patronus.Utils.CurrentUser;
import com.example.camilomontoya.patronus.Utils.Typo;
import com.example.camilomontoya.patronus.Utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    private static final String TAG = "AddFriendActivity";

    private TextView titleToolbar;
    private Button searchBtn;
    private EditText searchUser;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private boolean added;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        mAuth = FirebaseAuth.getInstance();

        titleToolbar = findViewById(R.id.toolbar_add_friends_title);
        titleToolbar.setTypeface(Typo.getInstance().getTitle());

        searchUser = findViewById(R.id.search_user);

        final ArrayList<FriendItem> usersFounded = new ArrayList<>();

        recycler = findViewById(R.id.friends_founded);

        layoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(layoutManager);

        adapter = new FriendsAdapter(usersFounded, new FriendsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FriendItem item) {
                addFriend(item.getUid());
                usersFounded.clear();
                finish();
            }
        });

        recycler.setAdapter(adapter);

        searchBtn = findViewById(R.id.btn_search_friend);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aviso("Buscando...");
                hideKeyboard();
                if (usersFounded.size() <= 0) {
                    final String searched = searchUser.getText().toString();
                    usersFounded.clear();
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            for (DataSnapshot users : dataSnapshot.getChildren()) {
                                if (users.child("email").getValue().toString().contains(searched)) {
                                    usersFounded.add(new FriendItem(users.child("profile_pic").getValue().toString(),
                                            users.child("name").getValue().toString(),
                                            users.child("email").getValue().toString(),
                                            users.getKey().toString()));
                                }
                            }

                            adapter.notifyItemInserted(usersFounded.size() - 1);
                            adapter.notifyDataSetChanged();
                            aviso("Usuario encontrado");
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            aviso("No se encontro ningun usuario");
                        }
                    });
                }

            }
        });
    }

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private void aviso(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    private void addFriend(final String uid) {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    if (!added) {
                        if (users.getKey().toString().contains(uid)) {
                            User newFriend = new User(users.child("name").getValue().toString(), users.child("email").getValue().toString(),
                                    users.child("residence").getValue().toString(), (long) users.child("distance").getValue(), (boolean) users.child("suburb").getValue(),
                                    (boolean) users.child("comercial").getValue(), (boolean) users.child("street").getValue(), (boolean) users.child("people").getValue(),
                                    (boolean) users.child("car").getValue(), new ArrayList<User>(), users.child("profile_pic").getValue().toString());
                            CurrentUser.getRef().getFriends().add(newFriend);
                            myRef.child(mAuth.getCurrentUser().getUid()).child("friends").setValue(CurrentUser.getRef().getFriends());
                            aviso("Amigo agregado: " + CurrentUser.getRef().getFriends().get(CurrentUser.getRef().getFriends().size() - 1).getName());
                            finish();
                            added = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
}

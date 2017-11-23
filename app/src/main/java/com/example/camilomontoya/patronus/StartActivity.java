package com.example.camilomontoya.patronus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilomontoya.patronus.Utils.CurrentUser;
import com.example.camilomontoya.patronus.Utils.Typo;
import com.example.camilomontoya.patronus.Utils.UniversalLoaderImage;
import com.example.camilomontoya.patronus.Utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    private TextInputLayout inputEmail, inputPass;
    private EditText email, pass;
    private Button btnLogin;
    private TextView register;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setStatusBarColor(Color.parseColor("#1C2335"));

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        UniversalLoaderImage loaderImage = new UniversalLoaderImage(this);
        ImageLoader.getInstance().init(loaderImage.getConfig());

        Typo.getInstance().setTitle(Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Bold.ttf"));
        Typo.getInstance().setSpecial(Typeface.createFromAsset(getAssets(), "fonts/Raleway-BoldItalic.ttf"));
        Typo.getInstance().setContent(Typeface.createFromAsset(getAssets(), "fonts/Quicksand-Regular.ttf"));

        inputEmail = (TextInputLayout) findViewById(R.id.input_email_log);
        inputPass = (TextInputLayout) findViewById(R.id.input_pass_log);
        email = (EditText) findViewById(R.id.email_log);
        pass = (EditText) findViewById(R.id.pass_log);
        btnLogin = (Button) findViewById(R.id.btn_log);
        register = (TextView) findViewById(R.id.register_txt);

        inputEmail.setTypeface(Typo.getInstance().getContent());
        inputPass.setTypeface(Typo.getInstance().getContent());
        email.setTypeface(Typo.getInstance().getContent());
        pass.setTypeface(Typo.getInstance().getContent());
        btnLogin.setTypeface(Typo.getInstance().getTitle());
        register.setTypeface(Typo.getInstance().getContent());

        email.setHighlightColor(Color.parseColor("#ffffff"));
        email.setTextColor(Color.parseColor("#ffffff"));
        email.setHintTextColor(Color.parseColor("#ffffff"));
        email.setLinkTextColor(Color.parseColor("#ffffff"));

        String negrilla = "Registrarse";
        String inicioTexto = "No tienes cuenta? ";
        SpannableString str = new SpannableString(inicioTexto + negrilla);
        str.setSpan(new ForegroundColorSpan(Color.parseColor("#2f80ed")), inicioTexto.length(), inicioTexto.length() + negrilla.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), inicioTexto.length(), inicioTexto.length() + negrilla.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setText(str);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            for (DataSnapshot users : dataSnapshot.getChildren()) {
                                //aviso("Datos de la base de datos: " + users.getKey().toString());
                                //aviso("Datos del usuario: " + user.getUid());

                                if (user.getUid().toString().contains(users.getKey().toString())) {
                                    aviso("El usuario es: " + users.child("name").getValue().toString());
                                    CurrentUser.getRef().setValues(users.child("name").getValue().toString(), users.child("email").getValue().toString(),
                                            users.child("residence").getValue().toString(), (long) users.child("distance").getValue(), (boolean) users.child("suburb").getValue(),
                                            (boolean) users.child("comercial").getValue(), (boolean) users.child("street").getValue(), (boolean) users.child("people").getValue(),
                                            (boolean) users.child("car").getValue(), users.child("friends").exists() ? users.child("friends").getValue( new GenericTypeIndicator<ArrayList<User>>() {}): new ArrayList<User>(), users.child("profile_pic").getValue().toString());

                                    startActivity(new Intent(StartActivity.this,HomeActivity.class));
                                    finish();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                            aviso("No se pudieron leer los valores");
                        }
                    });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        //ref.child("users").child("0311").setValue(new User("Camilo","cajomo0311@gmail.com","Pampalinda",3,true,true,false,false,true));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailAuth = email.getText().toString();
                String passAuth = pass.getText().toString();

                if (!emailAuth.equals("") && !passAuth.equals("")) {
                    if (emailAuth.contains("@")) {
                        mAuth.signInWithEmailAndPassword(emailAuth, passAuth).addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    aviso("No se pudo ingresar, intenta mas tarde");
                                } else {
                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            // This method is called once with the initial value and again
                                            // whenever data at this location is updated.
                                            for (DataSnapshot users : dataSnapshot.getChildren()) {
                                                //aviso("Datos de la base de datos: " + users.getKey().toString());
                                                //aviso("Datos del usuario: " + user.getUid());

                                                if (mAuth.getCurrentUser().getUid().toString().contains(users.getKey().toString())) {
                                                    aviso("El usuario es: " + users.child("name").getValue().toString());
                                                    CurrentUser.getRef().setValues(users.child("name").getValue().toString(), users.child("email").getValue().toString(),
                                                            users.child("residence").getValue().toString(), (long) users.child("distance").getValue(), (boolean) users.child("suburb").getValue(),
                                                            (boolean) users.child("comercial").getValue(), (boolean) users.child("street").getValue(), (boolean) users.child("people").getValue(),
                                                            (boolean) users.child("car").getValue(), users.child("friends").exists() ? users.child("friends").getValue( new GenericTypeIndicator<ArrayList<User>>() {}): new ArrayList<User>(), users.child("profile_pic").getValue().toString());
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError error) {
                                            // Failed to read value
                                            Log.w(TAG, "Failed to read value.", error.toException());
                                            aviso("No se pudieron leer los valores");
                                        }
                                    });
                                    startActivity(new Intent(StartActivity.this, HomeActivity.class));
                                    finish();
                                }
                            }
                        });
                    } else {
                        aviso("Ingresa un correo valido");
                    }
                } else {
                    aviso("Completa los campos para ingresar");
                }
            }
        });
    }

    private void aviso(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}

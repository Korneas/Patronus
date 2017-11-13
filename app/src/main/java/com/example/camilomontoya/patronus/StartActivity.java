package com.example.camilomontoya.patronus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    private EditText email,pass;
    private Button btnLogin;
    private TextView register;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        Typo.getInstance().setTitle(Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Bold.ttf"));
        Typo.getInstance().setSpecial(Typeface.createFromAsset(getAssets(),"fonts/Raleway-BoldItalic.ttf"));
        Typo.getInstance().setContent(Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Regular.ttf"));

        email = (EditText) findViewById(R.id.email_reg);
        pass = (EditText) findViewById(R.id.pass_reg);
        btnLogin = (Button) findViewById(R.id.btn_reg);
        register = (TextView) findViewById(R.id.register_txt);

        email.setTypeface(Typo.getInstance().getContent());
        pass.setTypeface(Typo.getInstance().getContent());
        btnLogin.setTypeface(Typo.getInstance().getTitle());
        register.setTypeface(Typo.getInstance().getContent());

        String negrilla = "Registrarse";
        String inicioTexto = "No tienes cuenta? ";
        SpannableString str = new SpannableString(inicioTexto + negrilla);
        str.setSpan(new ForegroundColorSpan(Color.rgb(236,110,173)), inicioTexto.length(), inicioTexto.length()+negrilla.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), inicioTexto.length(), inicioTexto.length()+negrilla.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setText(str);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAuth = email.getText().toString();
                String passAuth = pass.getText().toString();

                if(!emailAuth.equals("") && !passAuth.equals("")){
                    mAuth.signInWithEmailAndPassword(emailAuth, passAuth);
                } else {

                }
            }
        });
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

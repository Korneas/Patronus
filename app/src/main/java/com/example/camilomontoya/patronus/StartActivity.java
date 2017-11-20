package com.example.camilomontoya.patronus;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

import com.example.camilomontoya.patronus.Utils.Typo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = "StartActivity";

    private TextInputLayout inputEmail, inputPass;
    private EditText email,pass;
    private Button btnLogin;
    private TextView register;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setStatusBarColor(Color.parseColor("#1C2335"));

        mAuth = FirebaseAuth.getInstance();

        Typo.getInstance().setTitle(Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Bold.ttf"));
        Typo.getInstance().setSpecial(Typeface.createFromAsset(getAssets(),"fonts/Raleway-BoldItalic.ttf"));
        Typo.getInstance().setContent(Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Regular.ttf"));

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
        str.setSpan(new ForegroundColorSpan(Color.parseColor("#2f80ed")), inicioTexto.length(), inicioTexto.length()+negrilla.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), inicioTexto.length(), inicioTexto.length()+negrilla.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        register.setText(str);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(StartActivity.this,HomeActivity.class));
                    finish();
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
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailAuth = email.getText().toString();
                String passAuth = pass.getText().toString();

                if(!emailAuth.equals("") && !passAuth.equals("")){
                    if(emailAuth.contains("@")) {
                        mAuth.signInWithEmailAndPassword(emailAuth, passAuth).addOnCompleteListener(StartActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    aviso("No se pudo ingresar, intenta mas tarde");
                                } else {
                                    aviso("Ingresaste como: "+emailAuth);
                                    startActivity(new Intent(StartActivity.this,HomeActivity.class));
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

    private void aviso (String txt) {
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

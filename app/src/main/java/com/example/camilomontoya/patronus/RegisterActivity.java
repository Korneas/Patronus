package com.example.camilomontoya.patronus;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilomontoya.patronus.Utils.Typo;
import com.example.camilomontoya.patronus.Utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private TextInputLayout inputName, inputEmail, inputResidence, inputPass, inputConfirm;
    private EditText name, email, residence, pass, passConfirm;
    private TextView distance_short, distance_long, distance_txt, extra_info,profile_pic_txt, title_reg;
    private ImageButton profilePic,backReg;
    private SeekBar distance;
    private CheckBox optionS, optionC, optionA, optionP, optionCar;
    private Button btnReg;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setStatusBarColor(Color.parseColor("#1C2335"));

        mAuth = FirebaseAuth.getInstance();

        profilePic = (ImageButton) findViewById(R.id.profile_pic_reg);
        backReg = (ImageButton) findViewById(R.id.back_reg);

        inputName = (TextInputLayout) findViewById(R.id.input_name_reg);
        inputEmail = (TextInputLayout) findViewById(R.id.input_email_reg);
        inputResidence = (TextInputLayout) findViewById(R.id.input_residence_reg);
        inputPass = (TextInputLayout) findViewById(R.id.input_pass_reg);
        inputConfirm = (TextInputLayout) findViewById(R.id.input_passconfirm_reg);
        email = (EditText) findViewById(R.id.email_reg);
        name = (EditText) findViewById(R.id.name_reg);
        residence = (EditText) findViewById(R.id.residence_reg);
        pass = (EditText) findViewById(R.id.pass_reg);
        passConfirm = (EditText) findViewById(R.id.passconfirm_reg);

        extra_info = (TextView) findViewById(R.id.additional_info);
        distance_txt = (TextView) findViewById(R.id.distance_txt);
        distance_short = (TextView) findViewById(R.id.distance_short);
        distance_long = (TextView) findViewById(R.id.distance_long);
        profile_pic_txt = (TextView) findViewById(R.id.profile_pic_txt);
        title_reg = (TextView) findViewById(R.id.title_reg);

        distance = (SeekBar) findViewById(R.id.distance);
        optionS = (CheckBox) findViewById(R.id.option_suburb);
        optionC = (CheckBox) findViewById(R.id.option_comercial);
        optionA = (CheckBox) findViewById(R.id.option_street);
        optionP = (CheckBox) findViewById(R.id.option_people);
        optionCar = (CheckBox) findViewById(R.id.option_car);

        btnReg = (Button) findViewById(R.id.btn_reg);

        inputName.setTypeface(Typo.getInstance().getContent());
        inputEmail.setTypeface(Typo.getInstance().getContent());
        inputResidence.setTypeface(Typo.getInstance().getContent());
        inputPass.setTypeface(Typo.getInstance().getContent());
        inputConfirm.setTypeface(Typo.getInstance().getContent());
        email.setTypeface(Typo.getInstance().getContent());
        name.setTypeface(Typo.getInstance().getContent());
        residence.setTypeface(Typo.getInstance().getContent());
        pass.setTypeface(Typo.getInstance().getContent());
        passConfirm.setTypeface(Typo.getInstance().getContent());

        extra_info.setTypeface(Typo.getInstance().getTitle());
        distance_txt.setTypeface(Typo.getInstance().getTitle());
        distance_short.setTypeface(Typo.getInstance().getContent());
        distance_long.setTypeface(Typo.getInstance().getContent());
        profile_pic_txt.setTypeface(Typo.getInstance().getContent());
        title_reg.setTypeface(Typo.getInstance().getTitle());

        optionS.setTypeface(Typo.getInstance().getContent());
        optionC.setTypeface(Typo.getInstance().getContent());
        optionA.setTypeface(Typo.getInstance().getContent());
        optionP.setTypeface(Typo.getInstance().getContent());
        optionCar.setTypeface(Typo.getInstance().getContent());

        btnReg.setTypeface(Typo.getInstance().getTitle());

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

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        backReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aviso("Cambiar foto de perfil");
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailReg = email.getText().toString();
                final String passReg = pass.getText().toString();
                final String nameReg = name.getText().toString();
                final String residenceReg = residence.getText().toString();

                final boolean optionSReg = optionS.isChecked();
                final boolean optionCReg = optionC.isChecked();
                final boolean optionAReg = optionA.isChecked();
                final boolean optionPReg = optionP.isChecked();
                final boolean optionCarReg = optionCar.isChecked();

                if (!emailReg.equals("") && !passReg.equals("") && !nameReg.equals("") && !residenceReg.equals("")) {
                    if (emailReg.contains("@")) {
                        mAuth.createUserWithEmailAndPassword(emailReg, passReg)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            aviso("No se pudo registrar usuario");
                                        } else {
                                            aviso(emailReg + " registrado con exito!");
                                            mAuth.signInWithEmailAndPassword(emailReg,passReg);
                                            ref.child("users").child(mAuth.getCurrentUser().getUid()).setValue(new User(nameReg, emailReg, residenceReg, distance.getProgress()+1, optionSReg, optionCReg, optionAReg, optionPReg, optionCarReg));
                                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                            finish();
                                        }
                                    }
                                });
                    } else {
                        aviso("Escribe un correo valido");
                    }
                } else {
                    aviso("Completa todos los campos");
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

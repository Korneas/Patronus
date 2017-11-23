package com.example.camilomontoya.patronus;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
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

import com.example.camilomontoya.patronus.Utils.CurrentUser;
import com.example.camilomontoya.patronus.Utils.Typo;
import com.example.camilomontoya.patronus.Utils.UniversalLoaderImage;
import com.example.camilomontoya.patronus.Utils.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private TextInputLayout inputName, inputEmail, inputResidence, inputPass, inputConfirm;
    private EditText name, email, residence, pass, passConfirm;
    private TextView distance_short, distance_long, distance_txt, extra_info,profile_pic_txt, title_reg;
    private ImageButton profilePic,backReg;
    private Bitmap profileBitmap;
    private SeekBar distance;
    private CheckBox optionS, optionC, optionA, optionP, optionCar;
    private Button btnReg;
    private ProgressDialog progress;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private StorageReference mStorageRef;

    private static final int SELECT_SINGLE_PICTURE = 101;
    public static  final String IMAGE_TYPE = "image/*";
    public static final String FIREBASE_PIC_ROUTE = "profile_photos/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setStatusBarColor(Color.parseColor("#1C2335"));

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

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
                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Seleccione una imagen"), SELECT_SINGLE_PICTURE);
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
                                            ref.child("users").child(mAuth.getCurrentUser().getUid()).setValue(new User(nameReg,
                                                    emailReg, residenceReg, distance.getProgress()+1, optionSReg, optionCReg,
                                                    optionAReg, optionPReg, optionCarReg, new ArrayList<User>(),""));
                                            CurrentUser.getRef().setValues(nameReg, emailReg, residenceReg, distance.getProgress()+1, optionSReg, optionCReg, optionAReg, optionPReg, optionCarReg, new ArrayList<User>(),"");
                                            StorageReference storageReference = mStorageRef.child(FIREBASE_PIC_ROUTE + "/" + mAuth.getCurrentUser().getUid());
                                            byte[] profileBytes = getBytesFromBitmap(profileBitmap,100);

                                            UploadTask uploadTask = null;
                                            uploadTask = storageReference.putBytes(profileBytes);

                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    Uri getDownloadUrl = taskSnapshot.getDownloadUrl();
                                                    CurrentUser.getRef().setProfilePic(getDownloadUrl.toString());
                                                    ref.child("users").child(mAuth.getCurrentUser().getUid()).child("profile_pic").setValue(getDownloadUrl.toString());
                                                    //aviso("Se subio foto de perfil a: " + getDownloadUrl.toString());
                                                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    aviso("La imagen no se cargo correctamente");
                                                }
                                            });
                                        }
                                    }
                                });
                    } else {
                        aviso("Escribe un correo valido");
                    }
                } else {
                    aviso("Llena todos los campos");
                }
            }
        });
    }

    private void aviso(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }

    /**
     * Metodo para obtener la imagen que se seleccion desde la galería
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_SINGLE_PICTURE) {

                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    profileBitmap = bitmap;
                    profilePic.setImageBitmap(bitmap);
                } catch (IOException e) {
                    System.out.println("Error al cargar imagen");
                }
            }
        } else {
            Toast.makeText(this, "No se cargo ninguna imagen", Toast.LENGTH_LONG).show();
        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
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
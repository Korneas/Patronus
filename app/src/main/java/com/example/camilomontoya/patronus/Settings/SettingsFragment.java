package com.example.camilomontoya.patronus.Settings;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camilomontoya.patronus.HomeActivity;
import com.example.camilomontoya.patronus.R;
import com.example.camilomontoya.patronus.RegisterActivity;
import com.example.camilomontoya.patronus.StartActivity;
import com.example.camilomontoya.patronus.Utils.CurrentUser;
import com.example.camilomontoya.patronus.Utils.Typo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Camilo Montoya on 11/19/2017.
 */

public class SettingsFragment extends Fragment {
    private static final String TAG = "MapFragment";

    private CircleImageView profilePic;
    private TextView titleToolbar, settingsName, settingsEmail;
    private Button logOut;
    private Bitmap profileBitmap;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private StorageReference mStorageRef;

    private static final int SELECT_SINGLE_PICTURE = 101;
    public static  final String IMAGE_TYPE = "image/*";
    public static final String FIREBASE_PIC_ROUTE = "profile_photos/";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        titleToolbar = view.findViewById(R.id.toolbar_settings_title);
        titleToolbar.setTypeface(Typo.getInstance().getTitle());

        settingsName = view.findViewById(R.id.settings_name);
        settingsEmail = view.findViewById(R.id.settings_email);
        settingsName.setTypeface(Typo.getInstance().getTitle());
        settingsEmail.setTypeface(Typo.getInstance().getContent());

        settingsName.setText(CurrentUser.getRef().getName());
        settingsEmail.setText(CurrentUser.getRef().getEmail());

        profilePic = view.findViewById(R.id.settings_profile_pic);
        ImageLoader.getInstance().displayImage(CurrentUser.getRef().getProfilePic(),profilePic);

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

        logOut = (Button) view.findViewById(R.id.logOut);
        logOut.setTypeface(Typo.getInstance().getTitle());

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), StartActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }

    /**
     * Metodo para obtener la imagen que se seleccion desde la galería
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == SELECT_SINGLE_PICTURE) {

                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    profileBitmap = bitmap;
                    profilePic.setImageBitmap(bitmap);

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
                            //Toast.makeText(getContext(), "Se subio foto de perfil a: " + getDownloadUrl.toString(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "No se pudo cambiar la foto de perfil", Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (IOException e) {
                    System.out.println("Error al cargar imagen");
                }
            }
        } else {
            Toast.makeText(getContext(), "No se cargo ninguna imagen", Toast.LENGTH_LONG).show();
        }
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return stream.toByteArray();
    }
}

package com.example.camilomontoya.patronus;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    private EditText email,pass;
    private Button btnLogin;
    private TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        Typo.getInstance().setTitle(Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Bold.ttf"));
        Typo.getInstance().setSpecial(Typeface.createFromAsset(getAssets(),"fonts/Raleway-BoldItalic.ttf"));
        Typo.getInstance().setContent(Typeface.createFromAsset(getAssets(),"fonts/Quicksand-Regular.ttf"));

        email = (EditText) findViewById(R.id.email_login);
        pass = (EditText) findViewById(R.id.pass_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
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
    }
}

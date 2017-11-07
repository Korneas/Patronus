package com.example.camilomontoya.patronus;

import android.graphics.Typeface;

/**
 * Created by Camilo Montoya on 11/6/2017.
 */

public class Typo {

    public static Typo ref;

    Typeface title;
    Typeface special;
    Typeface content;

    private Typo(){

    }

    public static Typo getInstance(){
        if(ref==null){
            ref = new Typo();
        }
        return ref;
    }

    public Typeface getTitle() {
        return title;
    }

    public void setTitle(Typeface title) {
        this.title = title;
    }

    public Typeface getSpecial() {
        return special;
    }

    public void setSpecial(Typeface special) {
        this.special = special;
    }

    public Typeface getContent() {
        return content;
    }

    public void setContent(Typeface content) {
        this.content = content;
    }
}

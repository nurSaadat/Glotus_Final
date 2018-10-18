package com.example.usk.glotus_final.Catalog;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Test {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void aa() throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        System.out.println("111111111111111111111111111111111");
        System.out.println(Adress.preferences.getAll().toString());


    }
}

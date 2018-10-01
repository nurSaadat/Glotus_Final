package com.example.usk.glotus_final.SuperviserListFiles;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.usk.glotus_final.Encryption.AES;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class aa {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String args[]){
        int m=0;
        int n=1;
        for(int i=0;i<=15;i++){
            System.out.println(m);
            m=m+n;
            n=m-n;
        }

    }
}

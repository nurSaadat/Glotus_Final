package com.example.usk.glotus_final.System.Catalog;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.usk.glotus_final.System.Encryption.AES;
import com.example.usk.glotus_final.System.connection.Server;
import com.example.usk.glotus_final.System.loginFiles.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Update {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void update() throws NoSuchPaddingException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        getCatalogs();


    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshAdress() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String data;
        data = process("http://89.219.32.202/glotus/odata/standard.odata/Catalog_%D0%90%D0%B4%D1%80%D0%B5%D1%81%D0%B0%D1%82%D1%8B?$format=json", "GET", User.getCredential(), "{}");
        System.out.println(data);
        JSONArray array = null;
        JSONObject jsonObj = null;
        Adress.adresspreferences.edit().putString("00000000-0000-0000-0000-000000000000", "").commit();
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                Adress.adresspreferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"), array.getJSONObject(i).getString("Description")).commit();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshKontragent() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String data;
        data = process("http://89.219.32.202/glotus/odata/standard.odata/Catalog_%D0%9A%D0%BE%D0%BD%D1%82%D1%80%D0%B0%D0%B3%D0%B5%D0%BD%D1%82%D1%8B?$format=json", "GET", User.getCredential(), "{}");
        System.out.println(data);
        JSONArray array = null;
        JSONObject jsonObj = null;

        Kontragent.kontrpreferences.edit().putString("00000000-0000-0000-0000-000000000000","").commit();
        array = null;
        jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < array.length(); i++) {
            try {
                Kontragent.kontrpreferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"), array.getJSONObject(i).getString("Description")).commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshKontragentnum() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String data;
        data = process("http://89.219.32.202/glotus/odata/standard.odata/Catalog_%D0%9A%D0%BE%D0%BD%D1%82%D1%80%D0%B0%D0%B3%D0%B5%D0%BD%D1%82%D1%8B?$format=json", "GET", User.getCredential(), "{}");
        System.out.println(data);
        JSONArray array = null;
        JSONObject jsonObj = null;


        array = null;
        jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        KontragentNum.kontrnumpreferences.edit().putString("00000000-0000-0000-0000-000000000000","").commit();
        for (int i = 0; i < array.length(); i++) {
            try {
                KontragentNum.kontrnumpreferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"), array.getJSONObject(i).getString("ТелефонКонтактногоЛица")).commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshMdnames() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String data;
        data = process("http://89.219.32.202/glotus/odata/standard.odata/Catalog_%D0%9F%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D0%B8?$format=json", "GET", User.getCredential(), "{}");
        System.out.println(data);
        JSONArray array = null;
        JSONObject jsonObj = null;


        array = null;
        jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Mdnames.mdpreferences.edit().putString("00000000-0000-0000-0000-000000000000","").commit();

        for (int i = 0; i < array.length(); i++) {
            try {
                Mdnames.mdpreferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"), array.getJSONObject(i).getString("Description")).commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshPodrazd() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String data;
        data = process("http://89.219.32.202/glotus/odata/standard.odata/Catalog_%D0%9F%D0%BE%D0%B4%D1%80%D0%B0%D0%B7%D0%B4%D0%B5%D0%BB%D0%B5%D0%BD%D0%B8%D1%8F?$format=json", "GET", User.getCredential(), "{}");

        System.out.println(data);
        JSONArray array = null;
        JSONObject jsonObj = null;


        array = null;
        jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Podrazd.pdpreferences.edit().putString("00000000-0000-0000-0000-000000000000","").commit();
        for (int i = 0; i < array.length(); i++) {
            try {
                Podrazd.pdpreferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"), array.getJSONObject(i).getString("Description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshPochtaKontr() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String data;
        data = process("http://89.219.32.202/glotus/odata/standard.odata/Catalog_%D0%9A%D0%BE%D0%BD%D1%82%D1%80%D0%B0%D0%B3%D0%B5%D0%BD%D1%82%D1%8B?$format=json", "GET", User.getCredential(), "{}");
        System.out.println(data);
        JSONArray array = null;
        JSONObject jsonObj = null;


        array = null;
        jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Pochta.pochtakontr.edit().putString("00000000-0000-0000-0000-000000000000","").commit();
        for (int i = 0; i < array.length(); i++) {
            try {
                Pochta.pochtakontr.edit().putString(array.getJSONObject(i).getString("Ref_Key"), array.getJSONObject(i).getString("Почта")).commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshTransport() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        String data;
        data = process("http://89.219.32.202/glotus/odata/standard.odata/Catalog_%D0%A2%D1%80%D0%B0%D0%BD%D1%81%D0%BF%D0%BE%D1%80%D1%82?$format=json", "GET", User.getCredential(), "{}");
        System.out.println(data);
        JSONArray array = null;
        JSONObject jsonObj = null;


        array = null;
        jsonObj = null;
        try {
            jsonObj = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            array = jsonObj.getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Transport.trpreferences.edit().putString("00000000-0000-0000-0000-000000000000","").commit();
        for (int i = 0; i < array.length(); i++) {
            try {
                Transport.trpreferences.edit().putString(array.getJSONObject(i).getString("Ref_Key"), array.getJSONObject(i).getString("Description")).commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getCatalogs() throws NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshAdress();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshKontragent();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshKontragentnum();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshMdnames();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshPodrazd();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshPodrazd();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshTransport();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    refreshPochtaKontr();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String process(String url, String way, String cred, String data) throws NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Server server;
        String body = url + "," + way + "," + cred + "*---*" + data;
        System.out.println(body);
        String string = AES.aesEncryptString(body, "1234567890123456");
        body = "data=" + string;
        System.out.println(body);
        server = new Server("http://89.219.32.202/odata/demoaes.php", null, body);
        return server.post().toString();
    }






    /*
   new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.textView.setText("Скачивание каталог Адресаты");
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.progressBar2.setProgress(30);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.textView.setText("Скачивание каталог Менеджер");
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.progressBar2.setProgress(40);
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.progressBar2.setProgress(40);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.textView.setText("Скачивание каталог Контрагенты");
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.textView.setText("Скачивание каталог Транспорты");
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.progressBar2.setProgress(70);
            }
        });



        new Thread(new Runnable() {
            @Override
            public void run() {
                SignInActivity.progressBar2.setProgress(100);
            }
        });


   */
}

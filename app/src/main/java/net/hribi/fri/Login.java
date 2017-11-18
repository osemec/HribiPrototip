package net.hribi.fri;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.IOException;
import java.net.CookieStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class Login extends AppCompatActivity {
    private EditText username, pass;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.pass);
        sharedPref = getApplicationContext().getSharedPreferences("hribi", 0);

    }

    public void enter(View v) {
        switch (v.getId()) {
            case R.id.login:
                String usr = username.getText().toString();
                String password = pass.getText().toString();
                if (usr.length() != 0 && password.length() != 0) {
                    try {
                        run(usr, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.signup:
                Intent i = new Intent(this, CreateAccount.class);
                startActivity(i);
                break;
        }
    }

    public void run(String usr, String pass) throws Exception {
        //ClearableCookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getBaseContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url, cookies);
                        if (url.toString().compareTo("http://hribi.net/") == 0) {
                            editor = sharedPref.edit();
                            editor.putBoolean("logged", true);
                            String cookie = cookies.toString();
                            int pos = cookie.indexOf(";");
                            cookie = cookie.substring(1, pos);
                            editor.putString("cookie", cookie);
                            editor.apply();
                            Intent i = new Intent(Login.this, Intro.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url);
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                    }
                })
                //.cookieJar(cookieJar)
                .build();

        RequestBody formBody = new FormBody.Builder()
                .add("UporabniskoIme", usr)
                .add("Geslo", pass)
                .add("avtoprijava", "on")
                .build();

     /*   RequestBody formBody = new FormBody.Builder()
                .add("danrojstva", "0")
                .add("eposta", "oskar.semec1@gmail.com")
                .add("geslo1", "")
                .add("geslo2", "")
                .add("ime", "abc")
                .add("letorojstva", "0")
                .add("mesecrojstva", "0")
                .add("omeni", "")
                .add("priimek", "")
                .build();

        Request request = new Request.Builder()
                .url("https://www.hribi.net/profil.asp?spremeni2=1&id=21474")
                .addHeader("Cookie", "ASPSESSIONIDQSTBDCSS=PMBFDGLAEAMAMLFOCMNBIXIZ")
                .post(formBody)
                .build();*/
        Request request = new Request.Builder()
                .url("https://www.hribi.net/prijava.asp")
                .addHeader("Referer", "http://hribi.net/")
                .post(formBody)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        String res = response.body().string();
                        Log.i("debug", "data: " + call.request());
                    }
                });
    }
}
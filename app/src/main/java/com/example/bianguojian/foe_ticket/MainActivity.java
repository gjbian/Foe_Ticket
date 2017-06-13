package com.example.bianguojian.foe_ticket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private  EditText etUsername;
    private  EditText etPassword;

    private static String LoginURL = "http://192.168.110.1:8080/FoeServlet/Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences userSharePreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        if (!("".equals(userSharePreferences.getString("username", "")))) {
            Intent intent = new Intent(MainActivity.this, FilmListActivity.class);
            startActivity(intent);
        }

        etUsername = (EditText)findViewById(R.id.EditTextRUsername);
        etPassword = (EditText)findViewById(R.id.EditTextPassword);

        Button btnLogin = (Button)findViewById(R.id.ButtonLogin);
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String usernmae = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (usernmae.isEmpty()) {
                    Toast.makeText(MainActivity.this, "用户名为空", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "密码为空", Toast.LENGTH_SHORT).show();
                } else {
                    Login(usernmae, password);
                }
            }
        });

        Button btnForgetPassword = (Button)findViewById(R.id.ButtonForgetPassword);
        btnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "该功能暂时未开放,敬请期待", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnSignup = (Button)findViewById(R.id.ButtonSignUp);
        btnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void Login(String username, String password) {
        String loginUrl = LoginURL + "?Username=" + username + "&Password=" + password;
        new MyAsyncTask(MainActivity.this, username).execute(loginUrl);
    }

    public static class MyAsyncTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private String uName;

        public  MyAsyncTask(Context context, String username) {
            this.context = context;
            this.uName = username;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected  String doInBackground(String... params) {
            HttpURLConnection connection = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected  void  onProgressUpdate(Integer... Values) {

        }

        @Override
        protected void onPostExecute(String s) {
            String text = s.substring(s.indexOf("resMsg=")+7);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            if ("登录成功}".equals(text)) {
                SharedPreferences userSharePreferences = context.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = userSharePreferences.edit();
                editor.putString("username", uName);
                editor.commit();
                Intent intent = new Intent(context, FilmListActivity.class);
                context.startActivity(intent);
            }
        }
    }
}



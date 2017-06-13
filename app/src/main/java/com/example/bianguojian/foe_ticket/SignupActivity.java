package com.example.bianguojian.foe_ticket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class SignupActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private EditText etPhone;

    private static String RegisterURL = "http://192.168.110.1:8080/FoeServlet/Regist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = (EditText)findViewById(R.id.EditTextRUsername);
        etPassword = (EditText)findViewById(R.id.EditTextRPassword);
        etPhone = (EditText)findViewById(R.id.EditTextRPhone);

        Button btnSignup = (Button)findViewById(R.id.ButtonRSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernmae = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String phone = etPhone.getText().toString();

                if (usernmae.isEmpty()) Toast.makeText(SignupActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                else if (password.isEmpty()) Toast.makeText(SignupActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                else if (phone.isEmpty()) Toast.makeText(SignupActivity.this, "电话不能为空", Toast.LENGTH_SHORT).show();
                else {
                    SignUp(usernmae, password, phone);
                }
            }
        });

        Button btnReturn = (Button)findViewById(R.id.ButtonRReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void SignUp(String username, String password, String phone) {
        String RegisterUrl = RegisterURL + "?Username=" + username + "&Password=" + password + "&Phone=" + phone;
        new MyAsyncTask(SignupActivity.this).execute(RegisterUrl);
    }

    public static class MyAsyncTask extends AsyncTask<String, Integer, String> {
        private Context context;

        public  MyAsyncTask(Context context) {
            this.context = context;
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
            int codeIndex = s.indexOf("resCode=")+8;
            int msgIndex = s.indexOf("resMsg=");
            String code = s.substring(codeIndex, codeIndex+3);
            Toast.makeText(context, s.substring(msgIndex+7), Toast.LENGTH_SHORT).show();
            if ("200".equals(code)) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        }
    }

}

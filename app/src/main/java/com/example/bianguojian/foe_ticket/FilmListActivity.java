package com.example.bianguojian.foe_ticket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class FilmListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_list);

        ListView listView = (ListView)findViewById(R.id.ListViewFilmList);
        final List<Film> list = new ArrayList<>();
        FilmListAdapter filmListAdapter = new FilmListAdapter(FilmListActivity.this, list);
        listView.setAdapter(filmListAdapter);

        String FilmListUrl = "http://192.168.110.1:8080/FoeServlet/FilmList";
        new MyAsyncTask(FilmListActivity.this, list, filmListAdapter).execute(FilmListUrl);
    }

    public static class MyAsyncTask extends AsyncTask<String, Integer, ArrayList<String>> {
        private Context context;
        private List<Film> list;
        private FilmListAdapter filmListAdapter;
        private byte[] picByte;

        public  MyAsyncTask(Context context, List<Film> list, FilmListAdapter filmListAdapter) {
            this.context = context;
            this.list = list;
            this.filmListAdapter = filmListAdapter;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected  ArrayList<String> doInBackground(String... params) {
            HttpURLConnection connection = null;
            ArrayList<String> result = new ArrayList<String>();
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
                    result.add(line);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected  void  onProgressUpdate(Integer... Values) {

        }

        @Override
        protected void onPostExecute(ArrayList<String> filmList) {
            for (int i = 0; i < filmList.size()-1; i++) {
                String filmData = filmList.get(i);
                String filmName = filmData.substring(filmData.indexOf("name=")+5, filmData.indexOf("introduce="));
                String filmIntroduce = filmData.substring(filmData.indexOf("introduce=")+10, filmData.indexOf("actor="));
                String filmActor = filmData.substring(filmData.indexOf("actor=")+6, filmData.indexOf("filmPic="));
                String filmPicURL = "http://192.168.110.1:8080/FoeServlet/FilmPic?PicID=" + filmData.substring(filmData.indexOf("filmPic=")+8);
                Bitmap filmPic = null;
                list.add(new Film(filmPic, filmName, filmIntroduce, filmActor));
                new MyPicLoad(list, i, filmListAdapter).execute(filmPicURL);
            }
            filmListAdapter.notifyDataSetChanged();
        }
    }

    public static class MyPicLoad extends AsyncTask<String, Integer, byte[]> {
        private List<Film> list;
        private int i;
        private FilmListAdapter filmListAdapter;

        public MyPicLoad(List<Film> list, int i, FilmListAdapter filmListAdapter) {
            this.list = list;
            this.i = i;
            this.filmListAdapter = filmListAdapter;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected  byte[] doInBackground(String... params) {
            HttpURLConnection connection = null;
            byte[] picByte = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(8000);

                if (connection.getResponseCode() == 200) {
                    InputStream fis = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while ((length = fis.read(bytes)) != -1) {
                        bos.write(bytes, 0, length);
                    }
                    picByte = bos.toByteArray();
                    bos.close();
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return picByte;
        }

        @Override
        protected  void  onProgressUpdate(Integer... Values) {

        }

        @Override
        protected void onPostExecute(byte[] filmPic) {
            if (filmPic != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(filmPic, 0, filmPic.length);
                list.get(i).setFilmBitmap(bitmap);
                filmListAdapter.notifyDataSetChanged();
            }
        }
    }
}

package com.example.hp.imageloadingdataconection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    Button button;
    ConnectivityManager cnm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=findViewById(R.id.iv);
        button=findViewById(R.id.btn);
        cnm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String imagepath="http://placeimg.com/640/360";
                NetworkInfo networkInfo=cnm.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected())
                {
                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        Toast.makeText(MainActivity.this, "WI FI", Toast.LENGTH_SHORT).show();
                    }

                    if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        Toast.makeText(MainActivity.this, "Mobile", Toast.LENGTH_SHORT).show();
                        new MyImageTask().execute(imagepath);
                    }
                }

            }
        });
    }

    class MyImageTask extends AsyncTask<String,Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }

        }

        Bitmap downloadImage(String path){
            Bitmap bitmap=null;
            try {
                URL url = new URL(path);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);

                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);

                httpURLConnection.connect();
                int code = httpURLConnection.getResponseCode();

                if (code == HttpURLConnection.HTTP_OK) {
                    InputStream stream = httpURLConnection.getInputStream();
                    if (stream != null) {
                        bitmap = BitmapFactory.decodeStream(stream);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }
    }
}

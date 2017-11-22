package com.example.heechang.attendence;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by heechang on 2017-11-15.
 */

public class InsertData extends AsyncTask<String, Void, String> {

    Context context;
    private static String TAG = "InsertData";

    public AsyncResponse delegate = null;//Call back interface

    //결과값 반환하는 인터페이스
    //받는 엑티비티에서 구현해서 사용
    public interface AsyncResponse{
        void getResult(String result);
    }

    //엑티비티 context와 인터페이스가 필요함
    InsertData(Context context, AsyncResponse asyncResponse){
        delegate = asyncResponse;
        this.context = context;
    }

    //엑티비티 context와 인터페이스가 필요함
    InsertData(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "POST response  - " + result);
        if (result == null){
            delegate.getResult("GetData에서 결과값이 null이다");
        }
        else {
            delegate.getResult(result);
        }
    }


    @Override
    protected String doInBackground(String... params) {

        String serverURL = (String) params[0];
        String postParameters = (String) params[1];

//        String serverURL = "http://220.230.117.98/se/example3.php";
//        String postParameters = "name=" + "1"+"&author="+"2";


        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.setRequestProperty("content-type", "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "POST response code - " + responseStatusCode);

            InputStream inputStream;
            if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            } else {
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            return sb.toString();

        } catch (Exception e) {

            Log.d(TAG, "InsertData: Error ", e);

            return new String("Error: " + e.getMessage());
        }

    }
}
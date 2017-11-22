package com.example.heechang.attendence;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by heechang on 2017-11-14.
 */

public class GetData extends AsyncTask<String, Void, String> {

    private static String TAG = "GetData";

    public String result;
    ProgressDialog progressDialog;
    String errorString = null;

    Context context;

    public AsyncResponse delegate = null;//Call back interface

    //결과값 반환하는 인터페이스
    //받는 엑티비티에서 구현해서 사용
    public interface AsyncResponse{
        void getResult(String result);
    }

    //엑티비티 context와 인터페이스가 필요함
    GetData(Context context, AsyncResponse asyncResponse){
        delegate = asyncResponse;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "response  - " + result);

        if (result == null){
            delegate.getResult("GetData에서 결과값이 null이다");
        }
        else {
            delegate.getResult(result);
        }
    }

    //url페이지에서 스티링값 가져오는 함수
    //return값은 onPostExecute의 <string>으로 들어간다
    @Override
    protected String doInBackground(String... params) {

        String serverURL = params[0];


        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.connect();


            int responseStatusCode = httpURLConnection.getResponseCode();
            Log.d(TAG, "response code - " + responseStatusCode);

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }


            bufferedReader.close();

            return sb.toString().trim();


        } catch (Exception e) {

            Log.d(TAG, "InsertData: Error ", e);
            errorString = e.toString();

            return null;
        }

    }

}

// JSON파싱하는 코드
//    private void showResult(){
//        try {
//
//            JSONObject jsonObject = new JSONObject(mJsonString);
//            JSONArray jsonArray = jsonObject.getJSONArray("member");
//
//            for(int i=0;i<jsonArray.length();i++){
//
//                JSONObject item = jsonArray.getJSONObject(i);
//
//                String num = item.getString("num");
//                //String password = item.getString("password");
//                String name = item.getString("name");
//                //String sex = item.getString("sex");
//                String department = item.getString("department");
//                //String status = item.getString("status");
//
//                HashMap<String,String> hashMap = new HashMap<>();
//
//                hashMap.put("num", num);
//                hashMap.put("name", name);
//                hashMap.put("department", department);
//
//                mArrayList.add(hashMap);
//            }
//
//            ListAdapter adapter = new SimpleAdapter(
//                    MainActivity.this, mArrayList, R.layout.listviewitem,
//                    new String[]{"num","password", "name"},
//                    new int[]{R.id.listview_subject_num, R.id.listview_subject_day, R.id.listview_subject_name}
//            );
//
//            mlistView.setAdapter(adapter);
//
//        } catch (JSONException e) {
//
//            Log.d(TAG, "showResult : ", e);
//        }


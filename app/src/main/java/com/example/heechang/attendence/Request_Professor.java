package com.example.heechang.attendence;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.heechang.attendence.Login.P;

/**
 * Created by heechang on 2017-11-04.
 */

public class Request_Professor extends AppCompatActivity {

    private static String TAG = "Request_Professor";

    private TextView rp_professor_num_textview;
    private TextView rp_professor_name_textview;
    private TextView rp_professor_department_textview;
    private ImageView rp_professor_image_imageview;
    private Bitmap bitmap;

    private ListView rp_subject_listview;
    private ListviewAdapter adapter;
    private ArrayList<HashMap<String, String>> hArrayList;

    private Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_professor);

        rp_professor_image_imageview = (ImageView)findViewById(R.id.rp_professor_image_imageview);

        // 페어링 목록 출력하는 ListView
        rp_subject_listview = (ListView)findViewById(R.id.rp_subject_listview);
        adapter = new ListviewAdapter();
        rp_subject_listview.setAdapter(adapter);

        context = this;

        //전역변수 P를 사용해서 교수정보 출력하기
        //학번
//        rp_professor_num_textview = (TextView)findViewById(R.id.rp_professor_num_textview);
//        rp_professor_num_textview.setText(P.num.toString());
//        //이름
//        rp_professor_name_textview = (TextView)findViewById(R.id.rp_professor_name_textview);
//        rp_professor_name_textview.setText(P.name.toString());
//        //부서
//        rp_professor_department_textview = (TextView)findViewById(R.id.rp_professor_department_textview);
//        rp_professor_department_textview.setText(P.department.toString());
        //사진가져와서 사진출력
        //rp_professor_image_imageview.setBackground();
        Thread mThread = new Thread(){
            @Override
            public void run(){
                try {
                    URL url = new URL("220.230.117.98/se/6.jpg");
                    HttpURLConnection conn = null;
                    try {
                        conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        };
        mThread.start();

        try{
            mThread.join();
            rp_professor_image_imageview.setImageBitmap(bitmap);
        }catch(InterruptedException e)
        {

        }

        //교수넘버를 이용해서 교수가 강의하고 있는 수업정보 가져오기
//        while (true)
//        {
//            //디비에서 수업정보 가져와서 additem
//            //강의번호, 강의이름, 강의요일, 교수번호, gps좌표
//            //adapter.addItem(device.getName(), device.getAddress());  //리스트뷰에 데이터 추가
//        }

        //다이얼로그 보여주고 --> 다이얼로그 승인시 Attendence_Live_Professor 로 실시간 출석정보 제공
//        rp_subject_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View view, int position, long id) {
//
//            }
//        });

    }//end onCreate

    @Override
    protected void onStart() {
        super.onStart();
        //수업정보 가져오기
        GetData task = new GetData(this, new GetData.AsyncResponse() {
            @Override
            public void getResult(String mJsonString) {
                Log.i(TAG, mJsonString);

//                String[]token=null;
//                token = mJsonString.split(" ");
//                int i = 0;
//                while(token[i]!= null)
//                {
//                    adapter.addItem(token[i], token[i+1], token[i+2], token[i+3]);
//                    i+=4;
//                }
                //json파싱
                try {
                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("lecture");

                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject item = jsonArray.getJSONObject(i);

                        String Lecnum = item.getString("Lecnum");
                        String Lecname = item.getString("Lecname");
                        String Lecday = item.getString("Lecday");
                        String LecPid = item.getString("LecPid");
//                        HashMap<String,String> hashMap = new HashMap<>();
//                        hashMap.put("Lecnum", Lecnum);
//                        hashMap.put("LecName", LecName);
//                        hashMap.put("Lecday", Lecday);
//                        hashMap.put("LecPid", LecPid);

                        //hArrayList.add(hashMap);

                        //리스트뷰에 어뎁터 통해서 추가
                        adapter.addItem(Lecnum, Lecname, Lecday, LecPid);
                    }

                } catch (JSONException e) {

                    Log.d(TAG, "showResult : ", e);
                }
            }
        });
        //Request_Professor.php ==> select * from lecture;
        task.execute("http://220.230.117.98/se/Request_Professor.php");
    }

    public class ListviewAdapter extends BaseAdapter{

        private ArrayList<listviewitem> subjectlist = new ArrayList<listviewitem>();

        ListviewAdapter() {
        }

        @Override
        public int getCount(){return subjectlist.size();}
        @Override
        public Object getItem(int position){return subjectlist.get(position);}
        @Override
        public long getItemId(int position){return position;}
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final Context context = parent.getContext();

            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listviewitem, parent, false);
            }

            //수업번호, 수업이름, 수업일, 출석주차, 수강인원, 교수명
            TextView listview_subject_num = (TextView) convertView.findViewById(R.id.listview_subject_num);
            TextView listview_subject_name = (TextView) convertView.findViewById(R.id.listview_subject_name);
            TextView listview_subject_day = (TextView) convertView.findViewById(R.id.listview_subject_day);
            TextView listview_subject_professor = (TextView) convertView.findViewById(R.id.listview_subject_professor);

//임시 final
            final listviewitem listviewitem = subjectlist.get(position);

            listview_subject_num.setText(listviewitem.Lecnum);
            listview_subject_name.setText(listviewitem.Lecname);
            listview_subject_day.setText(listviewitem.Lecday);
            listview_subject_professor.setText(listviewitem.professor);

            //여기서 해당 수업에 대한 출석을 시작하면된다
            //수업에 대한 정보를 넘겨줄까?
            //--->Attendence_Live_Professor로 가자
            Button listview_BT_start = (Button) convertView.findViewById(R.id.listview_BT_start);
            listview_BT_start.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Log.i(TAG, listviewitem.Lecnum);
                    new AlertDialog.Builder(context)
                            .setTitle("출석을 시작할까요?")
                            .setMessage(listviewitem.Lecnum+"\n"+listviewitem.Lecname)
                            .setPositiveButton("네",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dlg, int sumthin) {
                                    InsertData task = new InsertData(context, new InsertData.AsyncResponse() {
                                        @Override
                                        public void getResult(String mJsonString) {
                                            if(mJsonString.equals("success"))
                                                Log.i(TAG, "success");
                                        }
                                    });
                                    //lecnum에 해당하는 수업의 ongoing을 1로 바꿔준다.
                                    task.execute("http://220.230.117.98/se/ongoing.php", "Lecnum="+listviewitem.Lecnum+"&ongoing=1");

                                    Intent i = new Intent(context, Attendance_Live_Professor.class);
                                    i.putExtra("listviewitem", listviewitem);
                                    startActivity(i);
                                }
                            })
                            .setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dlg, int sumthin){

                                }
                            }).show();
                }
            });

            return convertView;
        }

        public void addItem(String Lecnum, String Lecname,String Lecday,String LecPid) {
            listviewitem item = new listviewitem();
            item.Lecnum = Lecnum;
            item.Lecname = Lecname;
            item.Lecday = Lecday;
            item.professor = LecPid;

            subjectlist.add(item);
        }

    }



}


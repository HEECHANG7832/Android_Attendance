package com.example.heechang.attendence;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ycdoh on 2017-11-12.
 */

public class Attendance_Live_Professor extends AppCompatActivity {

    private static String TAG = "Attendance_LP";

    private ListView AL_listview;
    private ListviewAdapter adapter;
    private Calendar calendar;
    private String episode;
    private String startdate;
    private String currentdate;
    private TimerTask timerTask;
    private Timer timer;

    private listviewitem Listviewitem;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_live_professor);

        context = this;

        calendar = new GregorianCalendar(Locale.KOREA);
        currentdate = Integer.toString(calendar.get(Calendar.YEAR)) + "-" + Integer.toString(calendar.get(Calendar.MONTH)) + "-" + Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        startdate = "2017-09-06";

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = formatter.parse(startdate);
            Date endDate = formatter.parse(currentdate);

            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            long diff = endDate.getTime() - beginDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if(diffDays%7 == 0)
            {
                episode = Integer.toString((int)diffDays/7 * 2 + 1);
            }
            else
            {
                episode = Integer.toString((int)diffDays/7 * 2 + 2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AL_listview = (ListView) findViewById(R.id.rp_subject_listview);
        adapter = new ListviewAdapter();
        AL_listview.setAdapter(adapter);

        //쿼리로 수업에 포함되어있는 학생리스트 모두 가져온다음에
        //ArrayList들에 학생정보 입력
        //그 정보들을 ListView adapter에 넣는다

        //include 에 수업정보 보여주기
        Intent i = getIntent();
        Listviewitem = (listviewitem)i.getSerializableExtra("listviewitem");
        View test1View = findViewById(R.id.listitem_live);

        TextView include_lecnum = (TextView) test1View.findViewById(R.id.listview_subject_num);
        include_lecnum.setText(Listviewitem.Lecnum);
        TextView include_lecname = (TextView) test1View.findViewById(R.id.listview_subject_name);
        include_lecname.setText(Listviewitem.Lecname);
        TextView include_lecday = (TextView) test1View.findViewById(R.id.listview_subject_day);
        include_lecday.setText(Listviewitem.Lecday);
        TextView include_lecweek = (TextView) test1View.findViewById(R.id.listview_subject_week);
        include_lecweek.setText(Listviewitem.weekcount);
        TextView include_professor = (TextView) test1View.findViewById(R.id.listview_subject_professor);
        include_professor.setText(Listviewitem.professor);

        Button cancel = (Button) test1View.findViewById(R.id.listview_BT_start);
        cancel.setText("출석 종료");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //출석체크 종료
                //ongoing --> 0;
                //finish();
                timerTask.cancel();
                InsertData task = new InsertData(context, new InsertData.AsyncResponse() {
                    @Override
                    public void getResult(String mJsonString) {
                        if(mJsonString.equals("success"))
                            Log.i(TAG, "ongoing=0 success");
                    }
                });
                //lecnum에 해당하는 수업의 ongoing을 0로 바꿔준다.
                task.execute("http://220.230.117.98/se/ongoing.php", "Lecnum="+Listviewitem.Lecnum+"&ongoing=0");
            }
        });

        //attendance 테이블을 완료하고
        //학생 이름과 출석상태를 가져와서 listview를 채우는 코드작성
        InsertData task = new InsertData(context, new InsertData.AsyncResponse() {
            @Override
            public void getResult(String mJsonString) {
                Log.i(TAG, mJsonString);
                //json파싱
                try {
                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("episode");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject item = jsonArray.getJSONObject(i);

                        String studentid = item.getString("studentid");
                        String studentname = item.getString("studentname");
                        String state = item.getString("state");

//                        HashMap<String,String> hashMap = new HashMap<>();
//                        hashMap.put("Lecnum", Lecnum);
                        //hArrayList.add(hashMap);

                        //리스트뷰에 어뎁터 통해서 추가
                        adapter.addItem(studentid, studentname, state);
                    }

                } catch (JSONException e) {

                    Log.d(TAG, "showResult : ", e);
                }
            }
        });
        task.execute("http://220.230.117.98/se/bring_attendance_info.php", "tableInfo=" + Listviewitem.Lecnum + "&episode=1");

        timerTask = new TimerTask() {
            @Override
            public void run() {
                InsertData task = new InsertData(context, new InsertData.AsyncResponse() {
                    @Override
                    public void getResult(String mJsonString) {
                        Log.i(TAG, mJsonString);
                        //json파싱
                        try {
                            JSONObject jsonObject = new JSONObject(mJsonString);
                            JSONArray jsonArray = jsonObject.getJSONArray("episode");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject item = jsonArray.getJSONObject(i);

                                String studentid = item.getString("studentid");
                                String studentname = item.getString("studentname");
                                String state = item.getString("state");

//                        HashMap<String,String> hashMap = new HashMap<>();
//                        hashMap.put("Lecnum", Lecnum);
                                //hArrayList.add(hashMap);

                                //리스트뷰에 어뎁터 통해서 추가
                                adapter.compareItem(studentid,state);
                                adapter.notifyDataSetChanged();

                            }

                        } catch (JSONException e) {

                            Log.d(TAG, "showResult : ", e);
                        }
                    }
                });
                task.execute("http://220.230.117.98/se/bring_attendance_info.php", "tableInfo=" + Listviewitem.Lecnum + "&episode=1");

            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,3000);

    }//end onCreate

    public class ListviewAdapter extends BaseAdapter {

        private ArrayList<listview_student> studentlist = new ArrayList<listview_student>();

        ListviewAdapter() {
        }

        @Override
        public int getCount(){return studentlist.size();}
        @Override
        public Object getItem(int position){return studentlist.get(position);}
        @Override
        public long getItemId(int position){return position;}
        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final Context context = parent.getContext();

            if(convertView==null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_student_item, parent, false);
            }

            //수업번호, 수업이름, 수업일, 출석주차, 수강인원, 교수명
            TextView listview_student_name = (TextView) convertView.findViewById(R.id.ls_student_name);
            final TextView listview_student_status = (TextView) convertView.findViewById(R.id.ls_student_status);

            //임시 final
            final listview_student Listview_student = studentlist.get(position);

            listview_student_name.setText(Listview_student.name);
            listview_student_status.setText(Listview_student.state);


            //여기서 해당 수업에 대한 출석을 시작하면된다
            //수업에 대한 정보를 넘겨줄까?
            //--->Attendence_Live_Professor로 가자
            Button ls_BT_absence = (Button) convertView.findViewById(R.id.ls_BT_absence);
            ls_BT_absence.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    //state --> x
                    InsertData task = new InsertData(context, new InsertData.AsyncResponse() {
                        @Override
                        public void getResult(String mJsonString) {
                            Log.i(TAG, mJsonString); //success of failure
                            if(mJsonString.equals("success"))
                                listview_student_status.setText("x");
                        }
                    });
                    //lecnum에 해당하는 수업의 ongoing을 0로 바꿔준다.
                    task.execute("http://220.230.117.98/se/set_state_prof.php", "lecture="+Listviewitem.Lecnum+"&state=x&studentid="+Listview_student.name+"&episode=1");
                }
            });
            Button ls_BT_late = (Button) convertView.findViewById(R.id.ls_BT_late);
            ls_BT_late.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    //state --> a
                }
            });
            Button ls_BT_attendance = (Button) convertView.findViewById(R.id.ls_BT_attendance);
            ls_BT_attendance.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    //state --> o
                }
            });

            return convertView;
        }

        public void addItem(String studentid, String studentname, String state) {
            listview_student item = new listview_student();
            item.id = studentid;
            item.name = studentname;
            item.state = state;

            studentlist.add(item);
        }

        public void compareItem(String studentname, String state) {

            for(int i = 0; i < studentlist.size(); i++)
            {
                if(studentlist.get(i).id.equals(studentname) && !studentlist.get(i).state.equals(state))
                    studentlist.get(i).state = state;
            }

        }

    }

}
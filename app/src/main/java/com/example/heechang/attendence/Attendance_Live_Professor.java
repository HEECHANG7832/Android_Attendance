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

import java.util.ArrayList;

/**
 * Created by ycdoh on 2017-11-12.
 */

public class Attendance_Live_Professor extends AppCompatActivity {

    private static String TAG = "Attendance_LP";

    private ListView AL_listview;
    private ListviewAdapter adapter;

    private listviewitem Listviewitem;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_live_professor);

        context = this;

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
                InsertData task = new InsertData(context, new InsertData.AsyncResponse() {
                    @Override
                    public void getResult(String mJsonString) {
                        if(mJsonString.equals("success"))
                            Log.i(TAG, "success");
                    }
                });
                //lecnum에 해당하는 수업의 ongoing을 0로 바꿔준다.
                task.execute("http://220.230.117.98/se/ongoing.php", "Lecnum="+Listviewitem.Lecnum+"&ongoing=0");
            }
        });

    }//end onCreate

    @Override
    protected void onStart() {
        super.onStart();
        //attendance 테이블을 완료하고
        //학생 이름과 출석상태를 가져와서 listview를 채우는 코드작성

        InsertData task = new InsertData(context, new InsertData.AsyncResponse() {
            @Override
            public void getResult(String mJsonString) {
                if(mJsonString.equals("success"))
                    Log.i(TAG, "success");
            }
        });
        //lecnum에 해당하는 수업의 ongoing을 0로 바꿔준다.
        task.execute("http://220.230.117.98/se/bring_.php", "Lecnum="+Listviewitem.Lecnum+"&ongoing=0");

    }

    public class ListviewAdapter extends BaseAdapter {

        private ArrayList<listview_student> subjectlist = new ArrayList<listview_student>();

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
                convertView = inflater.inflate(R.layout.listview_student_item, parent, false);
            }

            //수업번호, 수업이름, 수업일, 출석주차, 수강인원, 교수명
            TextView listview_student_name = (TextView) convertView.findViewById(R.id.ls_student_name);
            TextView listview_student_status = (TextView) convertView.findViewById(R.id.ls_student_status);

            //임시 final
            final listview_student Listview_student = subjectlist.get(position);

            listview_student_name.setText(Listview_student.name);
            listview_student_status.setText(Listview_student.status);


            //여기서 해당 수업에 대한 출석을 시작하면된다
            //수업에 대한 정보를 넘겨줄까?
            //--->Attendence_Live_Professor로 가자
            Button ls_BT_absence = (Button) convertView.findViewById(R.id.ls_BT_absence);
            ls_BT_absence.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                }
            });
            Button ls_BT_late = (Button) convertView.findViewById(R.id.ls_BT_late);
            ls_BT_late.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                }
            });
            Button ls_BT_attendance = (Button) convertView.findViewById(R.id.ls_BT_attendance);
            ls_BT_attendance.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {

                }
            });

            return convertView;
        }

        public void addItem(String name, String status) {
            listview_student item = new listview_student();
            item.name = name;
            item.status = status;

            subjectlist.add(item);
        }

    }

}

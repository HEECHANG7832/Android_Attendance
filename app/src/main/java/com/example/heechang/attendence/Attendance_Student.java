package com.example.heechang.attendence;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Locale;


import static com.example.heechang.attendence.Login.P;

/**
 * Created by heechang on 2017-11-04.
 */

public class Attendance_Student extends AppCompatActivity implements Locationinformation{

    private static String TAG = "Attendance_Student";

    private TextView as_student_num_textview;
    private TextView as_student_name_textview;
    private TextView as_student_department_textview;
    private ImageView as_student_image_imageview;
    private ListView as_subject_listview;
    private ListviewAdapter adapter;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private Context context;
    private int current_location;
    private Bitmap bitmap;

    private double latPoint;
    private double lngPoint;
    private double endlatitude;
    private double endlongtitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence_student);

//        //전역변수 P를 사용해서 학생정보 출력하기
//        //학번
//        as_student_num_textview = (TextView)findViewById(R.id.as_student_num_textview);
//        as_student_num_textview.setText(P.num.toString());
//        //이름
//        as_student_name_textview = (TextView)findViewById(R.id.as_student_name_textview);
//        as_student_name_textview.setText(P.name.toString());
//        //부서
//        as_student_department_textview = (TextView)findViewById(R.id.as_student_department_textview);
//        as_student_department_textview.setText(P.department.toString());

        //사진가져와서 사진출력
        as_student_image_imageview = (ImageView)findViewById(R.id.as_student_image_imageview);
        Thread mThread = new Thread(){
            @Override
            public void run(){
                try {
                    URL url = new URL("220.230.117.98/se/"+P.picturepath);
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
            as_student_image_imageview.setImageBitmap(bitmap);
        }catch(InterruptedException e)
        {

        }
        //as_student_image_imageview.setBackground();


        //페어링 목록 출력하는 ListView
        as_subject_listview = (ListView) findViewById(R.id.as_subject_listview);
        adapter = new ListviewAdapter();
        as_subject_listview.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        context = this;

//        //학생넘버를 이용해서
//        while (true)
//        {
//            //현재 출석중이 true인 수업만 가져온다
//            //디비에서 수업정보 가져와서 additem
//            //강의번호, 강의이름, 강의요일, 교수번호, gps좌표
//            //adapter.addItem(device.getName(), device.getAddress());  //리스트뷰에 데이터 추가
//        }

//       // listview click리스너 클릭하면 출석체크 화면으로 넘어간다
//
//        as_subject_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//          Intent intent=new Intent(getApplicationContext(), Attendence_Live_Student.class);
//        startActivity(intent);
//
//            }
//        }) ;

    }//end onCreate

    @Override
    protected void onStart() {
        super.onStart();
        //수업정보 가져오기
        //ongoing이 1인 수업만 가져온다
        GetData task = new GetData(this, new GetData.AsyncResponse() {
            @Override
            public void getResult(String mJsonString) {
                Log.i(TAG, mJsonString);

                //json파싱
                try {
                    JSONObject jsonObject = new JSONObject(mJsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("lecture");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject item = jsonArray.getJSONObject(i);

                        String Lecnum = item.getString("Lecnum");
                        String Lecname = item.getString("Lecname");
                        String Lecday = item.getString("Lecday");
                        String LecPid = item.getString("LecPid");
                        String Lecloc = item.getString("Lecloc");
                        if(Integer.parseInt(Lecloc) == ENGINEERINGBLD)
                        {
                            endlatitude = LATITUDE_ENGINEERING;
                            endlongtitude = LONGTITUDE_ENGINEERING;
                        }
                        else if(Integer.parseInt(Lecloc) == HYEHWA)
                        {
                            endlatitude = LATITUDE_HYEHWA;
                            endlongtitude = LONGTITUDE_HYEHWA;
                        }
                        String ongoing = item.getString("ongoing");

//                        HashMap<String,String> hashMap = new HashMap<>();
//                        hashMap.put("Lecnum", Lecnum);
//                        hashMap.put("LecName", LecName);
//                        hashMap.put("Lecday", Lecday);
//                        hashMap.put("LecPid", LecPid);

                        //hArrayList.add(hashMap);

                        //리스트뷰에 어뎁터 통해서 추가
                        if (Integer.parseInt(ongoing) == 1) {
                            adapter.addItem(Lecnum, Lecname, Lecday, LecPid);
                        }
                    }

                } catch (JSONException e) {

                    Log.d(TAG, "showResult : ", e);
                }
            }
        });
        //Request_Professor.php ==> select * from lecture;
        task.execute("http://220.230.117.98/se/Request_Professor.php");
    }

    public class ListviewAdapter extends BaseAdapter {

        private ArrayList<listviewitem> subjectlist = new ArrayList<listviewitem>();

        ListviewAdapter() {
        }

        @Override
        public int getCount() {
            return subjectlist.size();
        }

        @Override
        public Object getItem(int position) {
            return subjectlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Context context = parent.getContext();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    createDialog(listviewitem);
                }
            });

            return convertView;
        }

        public void addItem(String Lecnum, String Lecname, String Lecday, String LecPid) {
            listviewitem item = new listviewitem();
            item.Lecnum = Lecnum;
            item.Lecname = Lecname;
            item.Lecday = Lecday;
            item.professor = LecPid;

            subjectlist.add(item);
        }

    }


    //리스트뷰에서 특정 수업을 선택하면 이 메서드로 다이얼로그를 표시하자
    //수업정보를 전달받고 gps위치를 확인해야 한다
    public void createDialog(listviewitem listviewitem) {
        CustomDialog mDialog = new CustomDialog(this, listviewitem);
        mDialog.show();

        // 디스플레이 해상도 가져오기
        Window window = mDialog.getWindow();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int x = (int) (size.x * 0.8f);
        int y = (int) (size.y * 0.8f);

        window.setLayout(x, y);
    }

    public class CustomDialog extends Dialog {

        protected TextView dialog_TV_title;
        protected TextView dialog_TV_lecnum;
        protected TextView dialog_TV_professor;
        protected TextView dialog_TV_lecweek;
        protected TextView dialog_TV_lecgps;

        private Button dialog_button_submit;

        private listviewitem listviewitem;

        public CustomDialog(Context context, listviewitem listviewitem) {
            super(context);
            this.listviewitem = listviewitem;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
            setContentView(R.layout.dialog);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); //gps와 network 확인


            //클릭된 리스트뷰로부터 정보를 받아 각 dialog의 각 textview에 출력
            dialog_TV_title = (TextView) findViewById(R.id.dialog_TV_title);
            dialog_TV_title.setText(listviewitem.Lecname);
            dialog_TV_lecnum = (TextView) findViewById(R.id.dialog_TV_lecnum);
            dialog_TV_lecnum.setText(listviewitem.Lecnum);
            dialog_TV_professor = (TextView) findViewById(R.id.dialog_TV_professor);
            dialog_TV_professor.setText(listviewitem.professor);

            dialog_TV_lecgps = (TextView) findViewById(R.id.dialog_TV_lecgps);
            dialog_TV_lecgps.setText("gps 확인로직 여기에 추가");
            dialog_button_submit = (Button) findViewById(R.id.dialog_button_submit);


            dialog_button_submit.setEnabled(false);
            dialog_button_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //출석 승인 쿼리 날리기
                }
            });


            if (isGPSEnabled && isNetworkEnabled) {
                final List<String> m_lstProviders = locationManager.getProviders(false);
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.e("location", "[" + location.getProvider() + "] (" + location.getLatitude() + "," + location.getLongitude() + ")");
                        latPoint = location.getLatitude();
                        lngPoint = location.getLongitude();

                        locationManager.removeUpdates(locationListener);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.e("onStatusChanged", "onStatusChanged");
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.e("onProviderEnabled", "onProviderEnabled");
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.e("onProviderDisabled", "onProviderDisabled");
                    }
                };

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getContext(),"Don't have permission",Toast.LENGTH_LONG).show();
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);

                if(meterDistanceBetweenPoints(latPoint,lngPoint,endlatitude,endlongtitude) < 1000 )//gps허용한계치
                {
                    dialog_button_submit.setEnabled(true);
                }
                else
                {
                    Toast.makeText(getContext(),"GPS 위치 허용범위 초과",Toast.LENGTH_LONG).show();
                }

            }
            else
            {
                Toast.makeText(getContext(),"GPS를 키고 다시 시도해주세요",Toast.LENGTH_LONG).show();
            }





        }
    }

    private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        float pk = (float) (180.f/Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    private void loadimage(String url)
    {

    }

}

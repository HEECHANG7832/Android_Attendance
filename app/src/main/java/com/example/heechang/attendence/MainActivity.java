package com.example.heechang.attendence;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/*
    로딩화면 페이지
 */
public class MainActivity extends AppCompatActivity {

    private static String TAG = "phptest_MainActivity";


    private Handler timeHandler;
    private Runnable myRunner;


    private TextView mTextViewResult;

    private Context context;

    public String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mTextViewResult = (TextView) findViewById(R.id.main_textview);
        context = this;

        /*
        php 에서 db쿼리 실행시켜서 결과를 json형식으로 가져오는 sample코드
         */
//        Button button2 = (Button) findViewById(R.id.button2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GetData task = new GetData(context, new GetData.AsyncResponse() {
//                    @Override
//                    public void getResult(String mJsonString) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(mJsonString);
//                            JSONArray jsonArray = jsonObject.getJSONArray("JSON 태그");
//
//                            for(int i=0;i<jsonArray.length();i++){
//
//                                JSONObject item = jsonArray.getJSONObject(i);
//
//                                //가져오고싶은 값에따라 커스텀
//                                String id = item.getString("ID");
//                                //기타등등
//                                //String name = item.getString(TAG_NAME);
//                                //String address = item.getString(TAG_ADDRESS);
//
//                                HashMap<String,String> hashMap = new HashMap<>();
//
//                                hashMap.put("ID", id);
//                                //기타등등
//                                //hashMap.put(TAG_NAME, name);
//                                //hashMap.put(TAG_ADDRESS, address);
//                            }
//                            //UI수정하고싶은코드 넣기
//                            runOnUiThread(new Runnable(){
//                                @Override
//                                public void run() {
//
//                                }
//                            });
//
//                        } catch (JSONException e) {
//
//                            Log.d(TAG, "showResult : ", e);
//                        }
//                    }
//                });
//                task.execute("http://220.230.117.98/se/example.php");
//            }
//        });

        /*
        android에서 php로 POST방식으로 값을 전달하는 sample
         */
//        Button button2 = (Button) findViewById(R.id.button2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InsertData task = new InsertData(context);
//                task.execute("http://220.230.117.98/se/example3.php", "name=" + "1" + "&author=" + "fuck");
//            }
//        });


        /*
            3초후 Login 페이지로
         */
        timeHandler = new Handler(); //Handler 클래스 추가
        timeHandler.postDelayed(myRunner=new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
            }
        }, 3000); // 3초
    }
}




//public class Main extends ServiceActivity{
//
//    private static final String TAG = "Main";
//
//    //로컬 블루투스 어댑터(블루투스 송수신 장치)를 나타냅니다.
//    // BluetoothAdapter는 블루투스 상호작용에 대한 진입점입니다.
//    // 이를 사용하여 다른 블루투스 기기를 검색하고
//    // 연결된(페어링된) 기기 목록을 쿼리하고 알려진 MAC 주소로 BluetoothDevice를 인스턴스화하고,
//    // 다른 기기로부터 통신을 수신 대기하는 BluetoothServerSocket을 만들 수 있습니다.
//    BluetoothAdapter mBluetoothAdapter = null;
//
//    private String Name;
//    private String Address;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //페어링 목록 출력하는 ListView
//        final ListView listView = (ListView)findViewById(R.id.main_listview);
//        ListviewAdapter adapter = new ListviewAdapter();
//        listView.setAdapter(adapter);
//
//        //bluetooth
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        // 블루투스 장치가 없다면, null이 return되어 종료.
//        if (mBluetoothAdapter == null)
//        {
//            Toast.makeText(this, "블루투스 기능을 이용할 수 없습니다.", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        } // end if
//        else
//        {
//            // 이미 페어링된 device 얻어오기.
//            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
//            // 페어링된 디바이스가 존재하면, ArrayAdapter에 추가.
//            if (pairedDevices.size() > 0)
//            {2
//                for (BluetoothDevice device : pairedDevices)
//                {
//                    adapter.addItem(device.getName(), device.getAddress());
//                } // end for
//            } // end if
//            else
//            {
//                adapter.addItem("There are no paired device", " ");
//                Log.i(TAG, "페어링된 장비가 없습니다.");
//            } // end else
//        }
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//                // get item
//                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;
//
//                Name = item.getName();
//                Address = item.getAddress();
//
//                // TODO : use item data.
//                // Handle navigation view item clicks here.
////                if(item.toString().equals("페어링된 장비가 없습니다."))
////                {
////                    NavigationView navigationView = (NavigationView) findViewById(R.id.main_NV_navview);
////                    Menu mn = navigationView.getMenu();
////                    mn.clear();
////                    return true;
////                }
//
////                if(item.toString().equals("새로고침"))
////                {
////                    NavigationView navigationView = (NavigationView) findViewById(R.id.main_NV_navview);
////                    Menu mn = navigationView.getMenu();
////                    mn.clear();
////                    if(!mBluetoothAdapter.isEnabled()) mBluetoothAdapter.enable();
////                    setupBluetooth(); return true;
////                }
//
//                // deviceInfo [0] : DeviceName, [1] : Device MAC.
////
//                v.setSelected(true);
//
//            }
//        }) ;
//
//
//        Button connect_button = (Button)findViewById(R.id.connect_button);
//        connect_button.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                if(Address != null)
//                {
//                    Toast.makeText(getApplicationContext(),"connecting "+ Name + "...",Toast.LENGTH_LONG).show();
//                    connectDevice(Address);
//                }else{
//                    Toast.makeText(getApplicationContext(), "Select Pairing device", Toast.LENGTH_LONG).show();
//                }
//
//            }
//        });
//
//        Button next_activity = (Button)findViewById(R.id.next_activity);
//        next_activity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), A1.class);
//                startActivity(intent);
//                String Message = Integer.toString(mService.MESSAGE_MAIN_TO_A1);
//                mService.write(Message);
//            }
//        });
//
//    }//end onCreate
//
//    public class ListviewAdapter extends BaseAdapter {
//
//        private ArrayList<ListViewItem> bluetoothlist = new ArrayList<ListViewItem>();
//
//        public ListviewAdapter(){
//        }
//
//        @Override
//        public int getCount(){return bluetoothlist.size();}
//        @Override
//        public Object getItem(int position){return bluetoothlist.get(position);}
//        @Override
//        public long getItemId(int position){return position;}
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent){
//            final Context context = parent.getContext();
//
//            if(convertView==null){
//                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = inflater.inflate(R.layout.item, parent, false);
//            }
//
//            TextView nameTextView = (TextView) convertView.findViewById(R.id.item_name);
//            TextView addressTextView = (TextView) convertView.findViewById(R.id.item_Address);
//
//            ListViewItem listviewitem = bluetoothlist.get(position);
//
//            nameTextView.setText(listviewitem.getName());
//            addressTextView.setText(listviewitem.getAddress());
//
//            return convertView;
//        }
//
//        public void addItem(String Name, String Address) {
//            ListViewItem item = new ListViewItem();
//
//            item.setName(Name);
//            item.setAddress(Address);
//
//            bluetoothlist.add(item);
//        }
//    }
//
//    private void connectDevice(String Device_address)
//    {
//        // Mac 주소 가져오기.
//        String address = Device_address;
//        // 연결시도.
//        try
//        {
//            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//            mService.connect(device);
//        }
//        catch(Exception e)
//        {
//            Toast.makeText(this, "Connect Fail", Toast.LENGTH_SHORT).show();
//            e.getStackTrace();
//        }
//    } // end connectDevice
//
//} //end class Main
//UI수정하고싶은코드 넣기
//runOnUiThread(new Runnable(){
//    @Override
//    public void run() {
//
//    }
//});

/*******************************************************************/
//custom dialog
/*******************************************************************/

//    // 추가한 부분
//    public void createDialog() {
//        CustomDialog mDialog = new CustomDialog(this);
//        mDialog.setCancelable(false); //뒤로가기 막기
//        mDialog.show();
//
//        // 디스플레이 해상도 가져오기
//        Display display = getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//
//        Window window = getWindow();
//
//        int x = (int) (size.x * 0.8f);
//        int y = (int) (size.y * 0.7f);
//
//        window.setLayout(x, y);
//    }
//
//public class CustomDialog extends Dialog {
//
//    protected TextView dialog_title;
//    protected TextView dialog_message;
//    private Button button_dialog_submit;
//    private Button button_dialog_cancel;
//
//    public CustomDialog(Context context) {
//        super(context);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
//        setContentView(R.layout.dialog);
//
//        dialog_title = (TextView) findViewById(R.id.dialog_title);
//        dialog_title.setText("손님요청");
//
//        dialog_message = (TextView) findViewById(R.id.dialog_message);
//        dialog_message.setText("여기서 내릴게요");
//
//        button_dialog_submit = (Button) findViewById(R.id.dialog_button_submit);
//        button_dialog_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                mService.write(BluetoothService.MESSAGE_DRIVER_ONFINISH_CLICK);
//                Intent i = new Intent(getApplicationContext(), Driver.class);
//                startActivity(i);
//                finish();
//            }
//        });
//
//        button_dialog_cancel = (Button) findViewById(R.id.dialog_button_submit);
//        button_dialog_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//                //승객 뒤쪽꺼에 전달되는 블루투스 메시지 필요
//                mService.write(BluetoothService.MESSAGE_DRIVER_ONFINISH_CLICK);
//                Intent i = new Intent(getApplicationContext(), Driver.class);
//                startActivity(i);
//                finish();
//            }
//        });
//    }
//}
//
////'네', '아니오' 버튼이 있는 dialog
//        new AlertDialog.Builder(this)
//                .setTitle("알림창")
//                .setMessage("여기서 내리시겠습니까?")
//                .setPositiveButton("네",new DialogInterface.OnClickListener() {
//public void onClick(DialogInterface dlg, int sumthin) {
//        //택시 도착 Log
//        Log_Service log = new Log_Service("Taxi has arrived");
//        log.addLog();
//
//                        /*
//                        *  도중에 dialog를 나가는 버튼을 누를 시
//                        *  "여기서 내리시겠습니까?" 음성 종료
//                        * */
//        if(mediaPlayer.isPlaying()) {
//        mediaPlayer.stop();
//        }
//
//                        /*
//                        * 택시 운전기사 태블릿에서 작동하게 하는 message
//                        * ActionAdOnArrivedClick()
//                        * Driver 클래스로 이동
//                        * */
//        mService.write(BluetoothService.MESSAGE_ARRIVE_CLICK_CONFIRM);
//        //mService.write(BluetoothService.MESSAGE_AD_ONARRIVED_CLICK);
//
//                        /*
//                        * 이후, Driver가 확인을 누른 경우,
//                        * "목적지에 도착하였습니다 저희 고요한 택시를 이용해주셔서 감사합니다"
//                        * activity로 이동
//                        * */
//
//        }
//        })
//        .setNegativeButton("아니오", new DialogInterface.OnClickListener(){
//public void onClick(DialogInterface dlg, int sumthin){
//                        /*
//                        *  도중에 dialog를 나가는 버튼을 누를 시
//                        *  "여기서 내리시겠습니까?" 음성 종료
//                        * */
//        if(mediaPlayer.isPlaying()) {
//        mediaPlayer.stop();
//        }
//
//        //광고 다시 시작
//        reStartAd(currentPosition);
//        }
//        }).show();
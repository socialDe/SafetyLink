package com.example.customermobile.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;
import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.customermobile.activity.LoginActivity.ip;

public class CarManagementActivity extends AppCompatActivity {

    SharedPreferences sp;
    UsersVO user;
    // 차량 정보 리스트 받아오기
    CarVO car;
    ArrayList<CarVO> carlist;
    LinearLayout container;
    ListView listView_carList;

    CarAsync carAsync;
    CarAdapter carAdapter;

    ImageButton imageButton_back, imageButton_addCar;

    public static CarManagementActivity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_management);
        activity = this;

        listView_carList = findViewById(R.id.listView_carList);

        carlist = new ArrayList<>();
        container = findViewById(R.id.container_car);

//        뒤로가기 버튼
        imageButton_back = findViewById(R.id.imageButton_back);
        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageButton_addCar = findViewById(R.id.imageButton_addCar);
        imageButton_addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CarAddActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        // 회원정보를 intent로 가져오기
        Intent intent = getIntent();
        user = (UsersVO) intent.getSerializableExtra("user");
//        String userid = getintent.getStringExtra("userId");
        Log.d("[Server]", user.toString());
        sp = getSharedPreferences("user", MODE_PRIVATE);

        // intent 정보가 없을 경우, sp로 회원정보 가져오기
        if (user == null) {
            String userid = sp.getString("userid", "");

            // 자동로그인 정보가 있으면 회원정보 계속 가져오기
            String userpwd = sp.getString("userpwd", "");
            String username = sp.getString("username", "");
            String userphone = sp.getString("userphone", "");
            String strbirth = sp.getString("userbirth", "");
            String usersex = sp.getString("usersex", "");
            String strregdate = sp.getString("userregdate", "");
            String userstate = sp.getString("userstate", "");
            String usersubject = sp.getString("usersubject", "");
            String babypushcheck = sp.getString("babypushcheck", "");
            String accpushcheck = sp.getString("accpushcheck", "");
            String mobiletoken = sp.getString("mobiletoken", "");


            // String 변수를 Date로 변환
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date userbirth = null;
            Date userregdate = null;
            try {
                userbirth = sdf.parse(strbirth);
                userregdate = sdf.parse(strregdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // sp 정보로 회원 객체 생성
            user = new UsersVO(userid, userpwd, username, userphone, userbirth, usersex, userregdate, userstate, usersubject, babypushcheck, accpushcheck, mobiletoken);
        }

        listView_carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), CarDetailActivity.class);
                int carid = carlist.get(position).getCarid();
                String carname = carlist.get(position).getCarname();
                String carnum = carlist.get(position).getCarnum();
                String cartype = carlist.get(position).getCartype();
                String carmodel = carlist.get(position).getCarmodel();
                int caryear = carlist.get(position).getCaryear();
                String carimg = carlist.get(position).getCarimg();
                String fueltype = carlist.get(position).getCaroiltype();
                car = new CarVO(carid, carname, carnum, cartype, carmodel, caryear, carimg, fueltype);
                intent.putExtra("car", car);
                intent.putExtra("user",user);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });
        getCarData();
        carAdapter = new CarAdapter();
        carAdapter.notifyDataSetChanged();

    }// end oncreate

    /*
      차량 정보 가져옴
    * */
    public void getCarData() {
        // URL 설정.
        String carUrl = "http://" + ip + "/webServer/cardata.mc?userid=" + user.getUserid();

        // AsyncTask를 통해 HttpURLConnection 수행.
        carAsync = new CarAsync();
        carAsync.execute(carUrl);
    }

    class CarAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = HttpConnect.getString(url); //result는 JSON
            Log.d("[TAG]", "result = "+result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d("[TEST]", "s:" + s);

            // userid가 car를 갖고있지 않으면 차량을 등록하는 화면으로 넘긴다
            // 차별 차정보를 저장한다
                JSONArray ja = null;
                try {
                    ja = new JSONArray(s);
                    carlist = new ArrayList<>();

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);

                        int carid = jo.getInt("carid");
                        String userid = jo.getString("userid");
                        String carnum = jo.getString("carnum");
                        String carname = jo.getString("carname");
                        String cartype = jo.getString("cartype");
                        String carmodel = jo.getString("carmodel");
                        int caryear = jo.getInt("caryear");
                        String carimg = jo.getString("carimg");
                        String caroiltype = jo.getString("caroiltype");
                        String tablettoken = jo.getString("tablettoken");

                        car = new CarVO(carid, userid, carnum, carname, cartype, carmodel, caryear, carimg, caroiltype, tablettoken);

                        carlist.add(car);
                        Log.d("[Server]", car.toString());
                        Log.d("[Server]", carlist.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            CarAdapter carAdapter = new CarAdapter();
            listView_carList.setAdapter(carAdapter);
            }
        }

    class CarAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return carlist.size();
        }

        @Override
        public Object getItem(int position) {
            Log.d("[Server]", "position:"+position);
            return carlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View carView = null;
            LayoutInflater inflater =
                    (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            carView =
                    inflater.inflate(R.layout.car, container, true);
            // 여기서 carView.findViewById로 정의해줘야 인식한다..
            TextView tx_carname = carView.findViewById(R.id.textView_carName);
            TextView tx_carnum = carView.findViewById(R.id.textView_carNum);
            TextView tx_carmodel = carView.findViewById(R.id.textView_carModel);
            int[] imglist = {R.drawable.car1, R.drawable.car2, R.drawable.car3};
            ImageView imageView_carImg = carView.findViewById(R.id.imageView_carImg);
                String carimg = carlist.get(position).getCarimg();
                if (carimg.equals("car1.jpg")) {
                    imageView_carImg.setImageResource(R.drawable.car1);
                } else if (carimg.equals("car2.jpg")) {
                    imageView_carImg.setImageResource(R.drawable.car2);
                } else if (carimg.equals("car3.jpg")) {
                    imageView_carImg.setImageResource(R.drawable.car3);
                }

                Log.d("[Server]", "carname :"+carlist.get(position).getCarname());
                Log.d("[Server]", "carmodel :"+carlist.get(position).getCarmodel());
                Log.d("[Server]", "carnum :"+carlist.get(position).getCarnum());
                tx_carname.setText(carlist.get(position).getCarname());
                tx_carmodel.setText(carlist.get(position).getCarmodel()); // int는 String으로 변경해주기
                tx_carnum.setText(carlist.get(position).getCarnum());
                // user 정보
                TextView textView_username = findViewById(R.id.textView_userName);
                textView_username.setText(user.getUsername());
                TextView textView_userphone = findViewById(R.id.textView_userPhone);
                textView_userphone.setText(user.getUserphone());

//            String i = list.get(position).getRank();
//            img1.setImageResource(imgs[Integer.parseInt(i)-1]);
            return carView;
        }
    } /* 차량 정보 END */

}
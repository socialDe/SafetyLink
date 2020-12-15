package com.example.customermobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;
import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;

import static com.example.customermobile.activity.LoginActivity.ip;

public class CarDetailActivity extends AppCompatActivity {
    CarVO car;
    UsersVO user;

    EditText editText_carName;
    TextView textView_carNum, textView_carModel, textView_carType, textView_carYear, textView_fuelType;
    Button button_modify, button_delete, button_modifyx;
    ImageView imgView_carimg;

    CarModifyAsync carModifyAsync;
    CarDeleteAsync carDeleteAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        Intent intent = getIntent();
        car = (CarVO) intent.getSerializableExtra("car");
        user = (UsersVO) intent.getSerializableExtra("user");
        Log.d("[Server]", user.toString());

        button_modify = findViewById(R.id.button_modify_car);

        button_modifyx = findViewById(R.id.button_modifyx_car);
        button_modifyx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_delete = findViewById(R.id.button_delete_car);

        editText_carName = findViewById(R.id.d_etx_carName);

        textView_carModel = findViewById(R.id.d_tx_carModel);
        textView_carNum = findViewById(R.id.d_tx_carNum);
        textView_carType = findViewById(R.id.d_tx_carType);
        textView_carYear = findViewById(R.id.d_tx_carYear);
        textView_fuelType = findViewById(R.id.d_tx_fuelType);

        imgView_carimg = findViewById(R.id.imgView_carImg);
        editText_carName.setText(car.getCarname());
        textView_carModel.setText(car.getCarmodel());
        textView_carNum.setText(car.getCarnum());
        if (car.getCartype().equals("p")) {
            textView_carType.setText("승용차");
        } else if (car.getCartype().equals("v")) {
            textView_carType.setText("승합차");
        } else if (car.getCartype().equals("t")) {
            textView_carType.setText("트럭");
        }
        textView_carYear.setText(car.getCaryear() + "");

        if (car.getCarimg().equals("car1.jpg")) {
            imgView_carimg.setImageResource(R.drawable.car1);
        } else if (car.getCarimg().equals("car2.jpg")) {
            imgView_carimg.setImageResource(R.drawable.car2);
        } else if (car.getCarimg().equals("car3.jpg")) {
            imgView_carimg.setImageResource(R.drawable.car3);
        }
        textView_fuelType.setText(car.getCaroiltype());

    } // onCreate;

    public void ModifyBt(View v) {
        String carname = editText_carName.getText().toString();

        String url = "http://" + ip + "/webServer/carmodifyimpl.mc";
        url += "?carid=" + car.getCarid() + "&carname=" + carname;

        carModifyAsync = new CarModifyAsync();
        carModifyAsync.execute(url);
    }

    class CarModifyAsync extends AsyncTask<String, Void, Void> {

        public Void result;

        @Override
        protected Void doInBackground(String... strings) {
            String url = strings[0];
            HttpConnect.getString(url); //result는 JSON
            return result;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            CarManagementActivity carManagementActivity = (CarManagementActivity) CarManagementActivity.activity;
            carManagementActivity.finish();

            Intent intent = new Intent(getApplicationContext(), CarManagementActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            finish();
        }
    }

    public void DeleteBt(View v) {
        String url = "http://" + ip + "/webServer/cardeleteimpl.mc";
        url += "?carid=" + car.getCarid();

        carDeleteAsync = new CarDeleteAsync();
        carDeleteAsync.execute(url);
    }

    class CarDeleteAsync extends AsyncTask<String, Void, Void> {

        public Void result;

        @Override
        protected Void doInBackground(String... strings) {
            String url = strings[0];
            HttpConnect.getString(url); //result는 JSON
            return result;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            CarManagementActivity carManagementActivity = (CarManagementActivity) CarManagementActivity.activity;
            carManagementActivity.finish();

            Intent intent = new Intent(getApplicationContext(), CarManagementActivity.class);
            intent.putExtra("user",user);
            startActivity(intent);
            finish();
        }
    }

}

package com.example.administrator.maplm;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class MainActivity extends AppCompatActivity {

    private TextView locationInfoTextView,text1_main = null;
    private Button startButton = null;
    private LocationClient locationClient = null;
    private static final int UPDATE_TIME = 1000;
    private static int LOCATION_COUTNS = 0;

    private  mybd mbd;
    public  static  int a=0;
    public Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationInfoTextView = (TextView) this.findViewById(R.id.tv_loc_info);
        locationInfoTextView.setText("bbbbbb");
        text1_main= (TextView) findViewById(R.id.text1_main);
        startButton = (Button) this.findViewById(R.id.btn_start);

        locationClient = new LocationClient(getApplicationContext());
        mbd=new mybd();
        Log.e("aaaaaaaaaaa", 555555+"");
        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        locationClient.registerLocationListener(mbd);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);		    //是否打开GPS
        option.setCoorType("gcj02");		//设置返回值的坐标类型。
        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒

        locationClient.setLocOption(option);


        handler=new android.os.Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String ss = (String) msg.obj;
                locationInfoTextView.setText(ss.toString());
            }
        };

        //注册位置监听器
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (a==0) {
                    startButton.setText("Start");
                    a=1;
                    locationClient.stop();
                }else {
                    startButton.setText("Stop");
                    a=0;
                    locationClient.start();

					/*
					 *当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。
					 *调用requestLocation( )后，每隔设定的时间，定位SDK就会进行一次定位。
					 *如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
					 *返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
					 *定时定位时，调用一次requestLocation，会定时监听到定位结果。
					 */
                    locationClient.requestLocation();
                }
            }
        });
    }

    //    定位监听的了类
public class mybd  implements BDLocationListener{
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            return;
        }
        StringBuffer sb;
        sb = new StringBuffer(256);
        sb.append("Time : ");
        sb.append(bdLocation.getTime());
        sb.append("\nError code : ");
        sb.append(bdLocation.getLocType());
        sb.append("\nLatitude 纬度: ");
        sb.append(bdLocation.getLatitude());
        sb.append("\nLontitude 经度: ");
        sb.append(bdLocation.getLongitude());
        sb.append("\nRadius : ");
        sb.append(bdLocation.getRadius());
        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation){
            sb.append("\nSpeed : ");
            sb.append(bdLocation.getSpeed());
            sb.append("\nSatellite : ");
            sb.append(bdLocation.getSatelliteNumber());
        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation){
            sb.append("\nAddress : ");
            sb.append(bdLocation.getAddrStr());

        }

        Message mg = new Message();
        String ss = sb.toString();
        Log.e("aaaaaaa", ss);
            mg.obj = ss;
            handler.sendMessage(mg);


    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }


}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null && locationClient.isStarted()) {
            locationClient.stop();
            locationClient = null;
        }
    }






}

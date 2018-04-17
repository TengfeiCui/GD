package com.bwie.gd_study;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;

public class MainActivity extends AppCompatActivity implements LocationSource {

    MyLocationStyle myLocationStyle;
    MapView mMapView = null;
    private Button show;
    private Button dw ,sn;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    aMapLocation.getLatitude();//获取纬度
                    aMapLocation.getLongitude();//获取经度
                    aMapLocation.getAccuracy();//获取精度信息
                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    String country = aMapLocation.getCountry();//国家信息
                    String province = aMapLocation.getProvince();//省信息
                    String city = aMapLocation.getCity();//城市信息
                    String district = aMapLocation.getDistrict();//城区信息
                    String street = aMapLocation.getStreet();//街道信息
                    aMapLocation.getStreetNum();//街道门牌号信息
                    aMapLocation.getCityCode();//城市编码
                    aMapLocation.getAdCode();//地区编码
                    aMapLocation.getAoiName();//获取当前定位点的AOI信息
                    aMapLocation.getBuildingId();//获取当前室内定位的建筑物Id
                    aMapLocation.getFloor();//获取当前室内定位的楼层
                    aMapLocation.getGpsAccuracyStatus();//获取GPS的当前状态

                    Toast.makeText(MainActivity.this, country+province+city+district+street+"", Toast.LENGTH_SHORT).show();


                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }

        }
    };
    AMap aMap;
    OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        show = findViewById(R.id.btn);
        dw = findViewById(R.id.btn2);
        sn = findViewById(R.id.btn3);
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle = new MyLocationStyle();

        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);



        //显示室内
        sn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化地图控制器对象
                if (aMap == null) {
                    aMap = mMapView.getMap();
                }
             /*   //true：显示室内地图；false：不显示；
                aMap.showIndoorMap(true);*/
                // 设置定位监听
                aMap.setLocationSource(MainActivity.this);
// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
                aMap.setMyLocationEnabled(true);
// 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);



            }
        });

        //显示
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
                mMapView.onCreate(savedInstanceState);
            }
        });

        //定位
        dw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//初始化定位
                mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
                mLocationClient.setLocationListener(mLocationListener);
//初始化AMapLocationClientOption对象
                mLocationOption = new AMapLocationClientOption();
                AMapLocationClientOption option = new AMapLocationClientOption();
                /**
                 * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
                 */
                option.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
                if(null != mLocationClient){
                    mLocationClient.setLocationOption(option);
                    //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
                    mLocationClient.stopLocation();
                    mLocationClient.startLocation();
                }

                //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
                //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);


                //获取一次定位结果：
//该方法默认为false。
                mLocationOption.setOnceLocation(true);

//获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
                mLocationOption.setOnceLocationLatest(true);

                //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
                mLocationOption.setInterval(1000);
//设置是否返回地址信息（默认返回地址信息）
                mLocationOption.setNeedAddress(true);
                //设置是否允许模拟位置,默认为true，允许模拟位置
                mLocationOption.setMockEnable(true);
                //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
                mLocationOption.setHttpTimeOut(20000);
                //关闭缓存机制
                mLocationOption.setLocationCacheEnable(false);
                //给定位客户端对象设置定位参数
                mLocationClient.setLocationOption(mLocationOption);
//启动定位
                mLocationClient.startLocation();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener((AMapLocationListener) this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

}

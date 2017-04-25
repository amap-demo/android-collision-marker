package apis.amap.com.android_marker_collision;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AMap.OnCameraChangeListener {

    private MapView mMapView;

    private AMap mAMap;


    private View mMarkerView;


    private RankerOverlay mRankerOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(null);
        mAMap = mMapView.getMap();


        mAMap.setOnCameraChangeListener(this);


        String[] titles = new String[]{
                "长沙臭豆腐(横二条)",
                "台湾手抓饼(西单北大街店)",
                "天下晓富(西单五店)",
                "幸福西饼蛋糕(西单店)",
                "麻辣印象(汉光百货店)",
                "将太无二(西单汉光商场店)",
                "许留山(汉光百货店)",
                "巴黎贝甜(汉光百货)",
                "麻辣诱惑(汉光店)",
                "夯番薯",
                "航美之家酒店(北京西单店)",
                "速8酒店(民航大厦西单地铁站店)",
                "国信苑宾馆",
                "北京市外事服务职业高中实习饭店",
                "华滨国际大酒店",
                "华威商务酒店",
                "广州大厦",
                "山水宾馆",
                "怡园商旅酒店",
                "大悦城酒店公寓"

        };

        double[][] latlngs = new double[][]{
                {116.376069, 39.908510},
                {116.376069, 39.908510},
                {116.376068, 39.9022},
                {116.375251, 39.909225},
                {116.375110, 39.909260},
                {116.374909, 39.909240},
                {116.376015, 39.909120},
                {116.375288, 39.909285},
                {116.375694, 39.909280},
                {116.375137, 39.909332},
                {116.377925, 39.908042},
                {116.378050, 39.9083},
                {116.376972, 39.910664},
                {116.373469, 39.905537},
                {116.375403, 39.904816},
                {116.374558, 39.911496},
                {116.376047, 39.911487},
                {116.371651, 39.910509},
                {116.377317, 39.911451},
                {116.372418, 39.911187}

        };

        Bitmap hotelBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.hotel);
        Bitmap dinnerBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.dinner);

        List<RankEntity> rankEntities = new ArrayList<RankEntity>();
        for (int i = 0; i < 20; i++) {

            RankEntity rankEntity = new RankEntity();
            rankEntity.setPoition(new LatLng(latlngs[i][1], latlngs[i][0]));


            rankEntity.setTitle(titles[i]);
            if (i < 10) {
                rankEntity.setType("dinner");
                rankEntity.setIcon(dinnerBitmap);
            } else {
                rankEntity.setType("hotel");
                rankEntity.setIcon(hotelBitmap);
            }
            rankEntities.add(rankEntity);
        }
        mRankerOverlay = new RankerOverlay(this, mAMap, rankEntities);
        mRankerOverlay.addToMap();

    }


    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mRankerOverlay.onDestroy();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        mRankerOverlay.onCameraChangeFinish(cameraPosition);
    }
}

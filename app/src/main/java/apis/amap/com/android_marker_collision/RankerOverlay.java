package apis.amap.com.android_marker_collision;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyi.qi on 2017/4/21.
 */

/**
 * 使用此功能，一般情况数据量不会太大，太大导致图面杂乱，反而影响显示效果，基于此，实现上未分离线程处理，如果数据量较大的需求的同学可以提下，可以调整相关结构，适配此类需求
 */
public class RankerOverlay {


    LruCache<String, BitmapDescriptor> mBitmapCache = new LruCache<String, BitmapDescriptor>(60) {
        protected void entryRemoved(boolean evicted, Integer key, BitmapDescriptor oldValue, BitmapDescriptor newValue) {
            oldValue.recycle();
        }
    };


    private List<RankEntity> mRankEntities = new ArrayList<RankEntity>();


    private AMap mAMap;


    private float mZoom;


    private float mScale;

    //根据字号，以及屏幕上显示文字marker的数量来修改设置boundary的范围
    private final static int DEFAULT_WIDTH = 60;

    private final  static int DEFAULT_HEIGHT = 35;

    private int mWidth = 0;
    private int mHeight = 0;


    private Context mContext;
    private View mMarkerView;
    private TextView mRightTextView;
    private TextView mLeftTextView;
    private ImageView mImageView;



/**
 * 构造函数
 *
 * */
    public RankerOverlay(Context context, AMap aMap, List<RankEntity> rankEntities) {
        mAMap = aMap;
        mContext = context;
        mRankEntities = rankEntities;
        init(context);
        mMarkerView = View.inflate(context, R.layout.marker_layout, null);
        mRightTextView = (TextView) mMarkerView.findViewById(R.id.text_right);
        mLeftTextView = (TextView) mMarkerView.findViewById(R.id.text_left);
        mImageView = (ImageView) mMarkerView.findViewById(R.id.marker_icon);

    }

    /**
     * 初始化添加到地图中
     */
    public void addToMap() {
        mZoom = mAMap.getCameraPosition().zoom;
        generateAll();
    }
    private void init(Context context) {
        mScale = context.getResources().getDisplayMetrics().density;
        //根据字号以及想要的容差效果，可以调整boundary大小，目前为了效率以及效果，未使用精确的外边框
        mWidth = (int) (DEFAULT_WIDTH * mScale + 0.5) + 10;
        mHeight = (int) (DEFAULT_HEIGHT * mScale + 0.5);
    }
    /**
     * 在不适用此Overlay功能时，以及onDestroy的生命周期里，调用此方法，回收相关资源，避免内存泄漏
     */
    public void onDestroy() {

        for (RankEntity rankEntity : mRankEntities) {

            Marker marker = rankEntity.getMarker();
            if (marker != null) {
                marker.remove();
                marker.destroy();
            }

        }
        mRankEntities.clear();
        mBitmapCache.evictAll();


    }
    /**
     *在onCameraChangeFinish中调用此方法，如果外部不需要监听OnCameraChangeListener，可以直接将接口在此overlay中实现
     *
     * */
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (mZoom != cameraPosition.zoom) {
            mZoom = cameraPosition.zoom;
            generateAll();
        }
    }


    public void resetRankEntities(List<RankEntity>rankEntities){
        for (RankEntity rankEntity : mRankEntities) {

            Marker marker = rankEntity.getMarker();
            if (marker != null) {
                marker.remove();
                marker.destroy();
            }

        }
        mRankEntities.clear();
        mRankEntities=rankEntities;
        generateAll();

    }
    private void generateBitmap() {

        for (final RankEntity rankEntity : mRankEntities) {
            mImageView.setImageBitmap(rankEntity.getIcon());
            if (rankEntity.getShowType() == RankEntity.SHOW_LEFT) {
                mLeftTextView.setText(rankEntity.getTitle());
                mLeftTextView.setVisibility(View.VISIBLE);
                mRightTextView.setVisibility(View.GONE);

            } else if (rankEntity.getShowType() == RankEntity.SHOW_RIGHT) {
                mRightTextView.setText(rankEntity.getTitle());
                mRightTextView.setVisibility(View.VISIBLE);
                mLeftTextView.setVisibility(View.GONE);

            } else {
                mRightTextView.setVisibility(View.GONE);
                mLeftTextView.setVisibility(View.GONE);

            }

            BitmapDescriptor bitmapDescriptor = mBitmapCache.get(rankEntity.toString());
            if (bitmapDescriptor == null) {
                mMarkerView.setDrawingCacheEnabled(true);
                mMarkerView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                mMarkerView.layout(0, 0, mMarkerView.getMeasuredWidth(), mMarkerView.getMeasuredHeight());

                mMarkerView.buildDrawingCache(true);

                Bitmap bitmapTemp = mMarkerView.getDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(bitmapTemp);
                bitmapTemp.recycle();
                mMarkerView.setDrawingCacheEnabled(false);
                bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);
                mBitmapCache.put(rankEntity.toString(), bitmapDescriptor);
            }



            float xAnchor = 0.f;
            if (rankEntity.getShowType() == RankEntity.NOT_SHOW) {
                xAnchor = 0.5f;
            } else if (rankEntity.getShowType() == RankEntity.SHOW_RIGHT) {
                xAnchor = 0.f;
            } else {
                xAnchor = 1.f;
            }

            Marker marker = rankEntity.getMarker();
            if (marker != null) {
                marker.setIcon(bitmapDescriptor);
                marker.setAnchor(xAnchor, 0.5f);
            } else {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.icon(bitmapDescriptor)
                        .position(rankEntity.getPoition()).anchor(xAnchor, 0.5f);

                marker = mAMap.addMarker(markerOptions);
                rankEntity.setMarker(marker);
            }
            if (rankEntity.getShowType() != RankEntity.NOT_SHOW) {
                marker.setZIndex(2.f);
            } else {
                marker.setZIndex(0.f);
            }

        }


    }


    private void generateAll() {
        Point rightPoint = new Point();
        Point leftPoint = new Point();
        Boundary leftBoundary = new Boundary();
        Boundary rightBoundary = new Boundary();
        List<Boundary> mShowBoundaries = new ArrayList<Boundary>();
        for (RankEntity rankEntity : mRankEntities) {
            Point point = mAMap.getProjection().toScreenLocation(rankEntity.getPoition());
            rightPoint.set(point.x + rankEntity.getIcon().getWidth(), point.y);
            leftPoint.set(point.x - rankEntity.getIcon().getWidth(), point.y);
            leftBoundary.initWithLeft(leftPoint, mWidth, mHeight);
            rightBoundary.initWithRight(rightPoint, mWidth, mHeight);
            boolean isLeftIntersect = false;
            boolean isRightIntersect = false;
            for (Boundary boundary : mShowBoundaries) {
                if (isLeftIntersect && isRightIntersect) {
                    break;
                }
                if (!isLeftIntersect) {
                    if (boundary.isIntersect(leftBoundary)) {
                        isLeftIntersect = true;
                    }
                }
                if (!isRightIntersect) {
                    if (boundary.isIntersect(rightBoundary)) {
                        isRightIntersect = true;

                    }
                }

            }
            if (!isRightIntersect) {
                Boundary showBoundary=rightBoundary.copy();
                rankEntity.setBoundary(showBoundary);
                rankEntity.setShowType(RankEntity.SHOW_RIGHT);
                mShowBoundaries.add(showBoundary);
            } else {

                if (!isLeftIntersect) {
                    Boundary showBoundary=leftBoundary.copy();
                    rankEntity.setBoundary(showBoundary);
                    mShowBoundaries.add(showBoundary);
                    rankEntity.setShowType(RankEntity.SHOW_LEFT);
                } else {
                    rankEntity.setShowType(RankEntity.NOT_SHOW);
                }
            }
        }
        generateBitmap();

    }



}

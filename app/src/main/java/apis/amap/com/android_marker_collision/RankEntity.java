package apis.amap.com.android_marker_collision;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;

/**
 * Created by yiyi.qi on 2017/4/21.
 */

/**
 * 进行碰撞的实体，需要设置位置，显示的Title，显示的小图标，以及小图标的type，同一个type的图标如果显示一致，只使用一张图片，减少内存使用
 */
public class RankEntity {


    static final int SHOW_LEFT = 2;

    static final int SHOW_RIGHT = 1;

    static final int NOT_SHOW = 0;


    private int mShowType;
    private int mRank;

    private String mTitle;

    private Bitmap mIcon;

    private LatLng mPosition;

    private Boundary mBoundary;

    private String mType;
    private Marker mMarker;
    private String mLeftString;

    private String mRightString;

    private String mNotString;


    /**
     * 小图标type
     */
    public void setType(String mType) {
        this.mType = mType;
    }

    /**
     * 显示位置
     */
    public void setPosition(LatLng mPosition) {
        this.mPosition = mPosition;
    }

    public String getTitle() {
        return mTitle;
    }

    /**
     * 显示的文字标注
     */
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }




    /**
     * 设置小图标
     */
    public void setIcon(Bitmap mIcon) {
        this.mIcon = mIcon;
    }

    public Bitmap getIcon() {
        return mIcon;
    }

    public String getType() {
        return mType;
    }

    public LatLng getPosition() {
        return mPosition;
    }


    public String toString() {

        if (mShowType == NOT_SHOW) {
            if (TextUtils.isEmpty(mNotString)) {
                mNotString = mTitle + " " + mType + " " + mShowType;

            }
            return mNotString;
        }

        if (mShowType == SHOW_LEFT) {
            if (TextUtils.isEmpty(mLeftString)) {
                mLeftString = mTitle + " " + mType + " " + mShowType;

            }
            return mLeftString;
        }
        if (mShowType == SHOW_RIGHT) {

            if (TextUtils.isEmpty(mRightString)) {
                mRightString = mTitle + " " + mType + " " + mShowType;

            }
            return mRightString;
        }

        return mTitle + " " + mType + " " + mShowType;
    }

    int getShowType() {
        return mShowType;
    }

    void setShowType(int mShowType) {
        this.mShowType = mShowType;
    }

    Boundary getBoundary() {
        return mBoundary;
    }

    void setBoundary(Boundary mBoundary) {
        this.mBoundary = mBoundary;
    }

    Marker getMarker() {
        return mMarker;
    }

    void setMarker(Marker mMarker) {
        this.mMarker = mMarker;
    }

}

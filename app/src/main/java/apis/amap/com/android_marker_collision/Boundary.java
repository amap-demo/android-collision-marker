package apis.amap.com.android_marker_collision;

import android.graphics.Point;

/**
 * Created by yiyi.qi on 2017/4/21.
 */

  class Boundary {

     int mLeft;
     int mTop;
     int mRight;
     int mBottom;


      void initWithCenterAndSize(Point center,int width,int height){
        int centerX=center.x;
        int centerY=center.y;
        mLeft=centerX-width/2;
        mTop=centerY-height/2;
        mRight=centerX+width/2;
        mBottom=centerY+height/2;

    }

      void initWithLeft(Point center,int width,int height){
        int centerX=center.x;
        int centerY=center.y;
        mLeft=centerX-width;
        mTop=centerY-height/2;
        mRight=centerX;
        mBottom=centerY+height/2;
    }


      void initWithRight(Point center,int width,int height){
        int centerX=center.x;
        int centerY=center.y;
        mLeft=centerX;
        mTop=centerY-height/2;
        mRight=centerX+width;
        mBottom=centerY+height/2;
    }

      Boundary copy(){
        Boundary boundary=new Boundary();
        boundary.mBottom=mBottom;
        boundary.mRight=mRight;
        boundary.mTop=mTop;
        boundary.mLeft=mLeft;
        return boundary;
    }


       boolean isIntersect(Boundary boundary){

        if(mRight<boundary.mLeft||mLeft>boundary.mRight||mTop>boundary.mBottom||mBottom<boundary.mTop) {
            return false;
        }



        return true;
    }

    public String toString(){
        return mLeft +"  "+mRight+"  "+mBottom+"  "+mTop;
    }





}

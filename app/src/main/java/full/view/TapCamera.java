package full.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import dji.common.error.DJIError;
import dji.common.gimbal.DJIGimbalAngleRotation;
import dji.common.gimbal.DJIGimbalRotateAngleMode;
import dji.common.gimbal.DJIGimbalRotateDirection;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.base.DJIBaseProduct;
import googlemap.gsdemo.dji.com.gsdemo.MainActivity;
import googlemap.gsdemo.dji.com.gsdemo.R;

/**
 * Created by george on 08/02/2017.
 */

public class TapCamera extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener, SensorEventListener{
    Activity c;

    public static boolean vertical=true,horizontal=true,left=true,right=true,up=true,down=true;


    private final GestureDetector gestureDetector;

    public static boolean changescreen=true;
    public  TapCamera(Activity c){

        this.c=c;

        gestureDetector = new GestureDetector(c, new GestureListener());
    }




    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        Log.d("Tap","Changed Accuracy");
        //Do nothing.
    }

    public static float Round(float Rval, int Rpl) {
        float p = (float)Math.pow(10,Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (float)tmp/p;
    }
    float [] history = new float[2];
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        //if sensor is unreliable, return void
        if (event.accuracy == Sensor.TYPE_ORIENTATION&&!MainActivity.tap) {

            DJIBaseProduct product = FPVDemoApplication.getProductInstance();

            DJIGimbalAngleRotation mYaw_relative = null, mYaw_relative1 = null;
          DJIGimbalAngleRotation mYaw_relative2 = null;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            float xChange = history[0] - event.values[0];
            float yChange = history[1] - event.values[1];

            history[0] = event.values[0];
            history[1] = event.values[1];

           if (product!=null&&xChange > 2&&horizontal){
                if (left) {
                    setResultToToast( "LEFT");
                    right=false;
                    mYaw_relative2 = new DJIGimbalAngleRotation(true, 2, DJIGimbalRotateDirection.Clockwise);
                }else
                left=true;

            }
            else if (product!=null&&xChange < -2&&horizontal){
                if (right){
                    setResultToToast( "RIGHT");

                    left=false;
                mYaw_relative2 = new DJIGimbalAngleRotation(true, -2, DJIGimbalRotateDirection.Clockwise);}
                else
                right=true;

            }

            if (product!=null&&yChange > 2&&vertical){
                if (down){
                    up=false;
                mYaw_relative = new DJIGimbalAngleRotation(true, 5, DJIGimbalRotateDirection.Clockwise);}
                else
                down=true;
                setResultToToast("DOWN");
            }
            else if (product!=null&&yChange < -2&&vertical){
                if (up){
                    down=false;
                mYaw_relative = new DJIGimbalAngleRotation(true, -5, DJIGimbalRotateDirection.Clockwise);}
                else
                up=true;
                setResultToToast( "UP");
            }

           // Log.d("Tap", "x: " + x);
            //Log.d("Tap", "y: " + y);

            double curTime = System.currentTimeMillis();



            if (product!=null)
                product.getGimbal().rotateGimbalByAngle(DJIGimbalRotateAngleMode.RelativeAngle, mYaw_relative, mYaw_relative1, mYaw_relative2, new DJICommonCallbacks.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError error) {
                        //setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
                    }
                });

                //getAccelerometer(event);
                return;


        }
    }



    @Override
    public boolean onTouch(View v, MotionEvent  event) {
        return gestureDetector.onTouchEvent(event);

    }

    private void setResultToToast(final String string) {

        Toast.makeText(c, string, Toast.LENGTH_SHORT).show();


    }


     private final class GestureListener extends GestureDetector.SimpleOnGestureListener {



        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        @Override
        public boolean onDoubleTap(MotionEvent ev) {
            Log.d("TapCamera","Couble tap");
           // Activity a = (Activity) c;

            if (changescreen){changescreen=false;

                WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int width = display.getWidth();  // deprecated
                int height = display.getHeight();
               // new MainActivity().uninitPreviewer();
              RelativeLayout relative = (RelativeLayout) c.findViewById(R.id.Video);

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                      0,
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        width
                );

                //avvglp.height=1000;
               // avvglp.width=1000;
              //  avvglp.width.
                relative.setLayoutParams(param);
                MainActivity.mVideoSurface1.destroyDrawingCache();



              //  new MainActivity().initPreviewer();


            }
            else
            {changescreen=true;
               // new MainActivity().uninitPreviewer();
                RelativeLayout relative = (RelativeLayout) c.findViewById(R.id.Video);
              //  android.view.ViewGroup.LayoutParams avvglp =   relative.getLayoutParams();
                //avvglp.height=1000;
              //  avvglp.width= 0;

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        1
                );

                relative.setLayoutParams(param);
               // TextureView   mVideoSurface1 = (TextureView) a.findViewById(R.id.video_previewer_surface);
              //  avvglp =   MainActivity.mVideoSurface1.getLayoutParams();
              //  //avvglp.height=1000;
               // avvglp.width=ViewGroup.LayoutParams.MATCH_PARENT;
              //  MainActivity.mVideoSurface1.setLayoutParams(param);


            }
           /* Intent i=new Intent(c, CameraView.class);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(i);
            return true;

            Intent i=new Intent(c, CameraView.class);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(i);*/
            return super.onDoubleTap(ev);
        }
        @Override
        public boolean onDown(MotionEvent e) {





            return true;
        }

      @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                DJIBaseProduct product = CameraDrone.DJIDemoApplication.getProductInstance();
                if (product.getGimbal()!=null)
                if (Math.abs(diffX) > Math.abs(diffY)&& MainActivity.tap&&horizontal) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD&& MainActivity.tap&&vertical) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                }
                result = true;


            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {

        if (horizontal) {


           // setResultToToast("right: "+horizontal);
            DJIBaseProduct product = CameraDrone.DJIDemoApplication.getProductInstance();
           DJIGimbalAngleRotation mYaw_relative = new DJIGimbalAngleRotation(true, 0, DJIGimbalRotateDirection.Clockwise);

            DJIGimbalAngleRotation mYaw_relative1 = new DJIGimbalAngleRotation(true, 0, DJIGimbalRotateDirection.Clockwise);

           DJIGimbalAngleRotation mYaw_relative2 = new DJIGimbalAngleRotation(true, -2, DJIGimbalRotateDirection.Clockwise);

            product.getGimbal().rotateGimbalByAngle(DJIGimbalRotateAngleMode.AbsoluteAngle, mYaw_relative, mYaw_relative1, mYaw_relative2, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    //setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
                }
            });
        }
        //onGimbalStateUpdate( product.getGimbal(),new DJIGimbalState(new DJIGimbalAttitude(20,20,20),0, DJIGimbalWorkMode.FpvMode ,false,false,false,false,false,false,null,null));
        //Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
    }
    public void onSwipeLeft() {


        if (horizontal) {
            //setResultToToast("left: "+horizontal);

            DJIBaseProduct product = FPVDemoApplication.getProductInstance();
           DJIGimbalAngleRotation mYaw_relative = new DJIGimbalAngleRotation(true, 0, DJIGimbalRotateDirection.Clockwise);

           DJIGimbalAngleRotation mYaw_relative1 = new DJIGimbalAngleRotation(true, 0, DJIGimbalRotateDirection.Clockwise);

            DJIGimbalAngleRotation mYaw_relative2 = new DJIGimbalAngleRotation(true, 2, DJIGimbalRotateDirection.Clockwise);

            product.getGimbal().rotateGimbalByAngle(DJIGimbalRotateAngleMode.RelativeAngle, mYaw_relative, mYaw_relative1, mYaw_relative2, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    //setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
                }
            });
        }
    }

    public void onSwipeTop() {

        if (vertical) {
          //  setResultToToast("Top: "+vertical);

            DJIBaseProduct product = FPVDemoApplication.getProductInstance();
            DJIGimbalAngleRotation mYaw_relative = new DJIGimbalAngleRotation(true, 2, DJIGimbalRotateDirection.Clockwise);

            DJIGimbalAngleRotation mYaw_relative1 = new DJIGimbalAngleRotation(true, 0, DJIGimbalRotateDirection.Clockwise);

            DJIGimbalAngleRotation mYaw_relative2 = new DJIGimbalAngleRotation(true, 0, DJIGimbalRotateDirection.Clockwise);

            product.getGimbal().rotateGimbalByAngle(DJIGimbalRotateAngleMode.RelativeAngle, mYaw_relative, mYaw_relative1, mYaw_relative2, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    //setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
                }
            });
        }

    }

    public void onSwipeBottom() {

        if (vertical) {
         //   setResultToToast("botom: "+vertical);

            DJIBaseProduct product = FPVDemoApplication.getProductInstance();
           DJIGimbalAngleRotation mYaw_relative = new DJIGimbalAngleRotation(true, -2, DJIGimbalRotateDirection.Clockwise);

            DJIGimbalAngleRotation mYaw_relative1 = new DJIGimbalAngleRotation(true, 0, DJIGimbalRotateDirection.Clockwise);

           DJIGimbalAngleRotation mYaw_relative2 = new DJIGimbalAngleRotation(true, 0, DJIGimbalRotateDirection.Clockwise);

            product.getGimbal().rotateGimbalByAngle(DJIGimbalRotateAngleMode.RelativeAngle, mYaw_relative, mYaw_relative1, mYaw_relative2, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    //setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
                }
            });
        }
    }





}
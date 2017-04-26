package googlemap.gsdemo.dji.com.gsdemo;


import android.app.Activity;
import android.app.ProgressDialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;

import android.os.Handler;
import android.os.Message;


import android.support.v7.app.AppCompatActivity;

import android.view.Display;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupMenu;


import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;


import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;

import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;

import android.view.MenuItem;
import android.view.MotionEvent;

import android.view.TextureView;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.graphics.Bitmap;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;

import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Polyline;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


import dji.common.camera.CameraSystemState;
import dji.common.error.DJICameraError;
import dji.common.flightcontroller.DJIFlightControllerCurrentState;
import dji.common.gimbal.DJIGimbalAngleRotation;
import dji.common.gimbal.DJIGimbalRotateAngleMode;
import dji.common.gimbal.DJIGimbalRotateDirection;
import dji.common.gimbal.DJIGimbalState;
import dji.common.product.Model;
import dji.common.util.DJICommonCallbacks;
import dji.log.DJILogHelper;
import dji.sdk.battery.DJIBattery;
import dji.sdk.camera.DJICamera;
import dji.common.camera.DJICameraSettingsDef;
import dji.sdk.camera.DJIPlaybackManager;
import dji.sdk.camera.DJIPlaybackManager.DJICameraPlaybackState;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.flightcontroller.DJIFlightController;


import dji.sdk.flightcontroller.DJIFlightControllerDelegate;
import dji.sdk.gimbal.DJIGimbal;
import dji.sdk.missionmanager.DJIMission;
import dji.sdk.missionmanager.DJIMissionManager;
import dji.sdk.missionmanager.DJIWaypoint;
import dji.sdk.missionmanager.DJIWaypoint.DJIWaypointActionType;
import dji.sdk.missionmanager.DJIWaypointMission;
import dji.sdk.missionmanager.DJIWaypointMission.DJIWaypointMissionStatus;
import dji.sdk.products.DJIAircraft;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIBaseProduct;
import dji.common.error.DJIError;

import dji.common.flightcontroller.DJIFlightControllerDataType;



import dji.common.battery.DJIBatteryState;
import dji.sdk.missionmanager.DJIMission.DJIMissionProgressStatus;
import full.view.CameraDrone;

import full.view.FPVDemoApplication;
import full.view.TapCamera;
import image.edit.opencv.ImageEdit;
import image.edit.opencv.Stitch;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import android.view.TextureView.SurfaceTextureListener;

import static googlemap.gsdemo.dji.com.gsdemo.TapMap.clearpoints;


public class MainActivity extends AppCompatActivity implements View.OnClickListener ,DJIMissionManager.MissionProgressStatusCallback, DJICommonCallbacks.DJICompletionCallback,DJIGimbal.GimbalStateUpdateCallback, SurfaceTextureListener {


    public static Bitmap image;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.d("opencv", "OpenCV loaded successfully");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
            }
                //break;
        }
    };





    protected DJICamera.CameraReceivedVideoDataCallback mReceivedVideoDataCallBack = null;

    DJIPlaybackManager.CameraFileDownloadCallback mFileDownloadCallBack;
    // public CameraDrone dc = new CameraDrone(MainActivity.this);
    static int second = 0, numbersOfSelected;
    LinearLayout wayPointSettings;
    // Codec for video live view
    protected DJICodecManager mCodecManager = null;
    protected static TextView mConnectStatusTextView, altitute, speed, speedy;
    public static int num_photos = 0,d=0;
    public static  TextureView mVideoSurface1 = null,mVideoSurfaceFull=null;
protected static int fov=94;

    protected TextView recordingTime;



    //Main Activity variables
    SensorManager sensorManager;

    public static boolean modec = false,clickd=false,chekcedopti=false,takephoto1=false,photocheck_stop=false,photoremote=false;
    private static boolean changescreen=true;
    public static int successphoto=1,flag1=0;
    protected static final String TAG = "Intelligent UAV";

    public static Context c1;
    public Button add, clear, alti_stay, download;
    public static Button add_waypoints;
    private ImageButton locate;

    private CheckBox photo;
    private ProgressBar mProgress, progressbattery;
    private ProgressDialog mDownloadDialog;
    public static boolean showimage=false,uploc=false,isAdd = false, flag = true, photocheck = false, change = false,tap=true,missionstart=false;

    public static MapView mapView;

    private double  droneLocationLat = 35.1445693, droneLocationLng = 33.4084526, ndroneLocationLat, ndroneLocationLng;
    //String STITCHING_SOURCE_IMAGES_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/smartdrone/"+"compress";
    protected static String STITCHING_SOURCE_IMAGES_DIRECTORY = null;
    //private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<Integer, Marker>();
    protected static Layer droneMarker = null;

    protected static float altitude_w[];
    protected float altitude = 100.0f;

    private float mSpeed = 10.0f;

    public static int dm = 0, corner_points = 0, type = 0;
    protected static int newalti;
    public Coordinates points[];

    public static Coordinates[] corner = new Coordinates[2];

    public static Coordinates[] drone_move;

    //MAP VARIABLES
    public  TapMap tm;

    public static List<Layer> marks, random_marks,calt,markspolyline;

    public static Layer[] diagonios;

//DJI VARIABLES

    protected static DJIWaypointMission mWaypointMission;

    private DJIMissionManager mMissionManager;
    private DJIFlightController mFlightController;
    DJIBattery.DJIBatteryStateUpdateCallback batterystatus;
   // DJIBatteryStateUpdateCallback
    private DJIWaypointMission.DJIWaypointMissionFinishedAction mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.NoAction;
    private DJIWaypointMission.DJIWaypointMissionHeadingMode mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.Auto;
    protected DJIPlaybackManager.DJICameraPlayBackStateCallBack mCameraPlayBackStateCallBack;  //to get currently selected pictures cou


    protected static Coordinates[] diagonii;

    public File mCascadeFile;
    static CascadeClassifier mJavaDetector;
    public static ImageView opencvView;

    public static int minWidth, minHeight;
    public static int imWidth = 800, imHeight = 600;

    Coordinates statusdrone;
    private final int STARTAUTODOWNLOAD = 1;
    private final int ENTERMULTIPLEPLAYBACK = 2;
    private final int ENTERMULTIPLEEDIT = 3;
    private final int DOWNLOADIT = 5;
    private final int SELECTALL = 6;




    private Handler handler = new Handler(new Handler.Callback() {


        @Override
        public boolean handleMessage(Message msg) {
            DJICamera camera = FPVDemoApplication.getCameraInstance();

            switch (msg.what) {
                case STARTAUTODOWNLOAD: {

                    if (camera != null)
                        camera.setCameraMode(DJICameraSettingsDef.CameraMode.Playback, new DJICommonCallbacks.DJICompletionCallback() {

                            @Override
                            public void onResult(DJIError mErr) {
                                // TODO Auto-generated method stub
                                if (mErr == null) {
                                    handler.sendEmptyMessageDelayed(ENTERMULTIPLEPLAYBACK, 2000);
                                }
                            }
                        });
                    break;
                }
                case ENTERMULTIPLEPLAYBACK: {
                    DJICameraError err = camera.getPlayback().enterMultiplePreviewMode();
                    if (err == null)
                        handler.sendEmptyMessageDelayed(ENTERMULTIPLEEDIT, 2000);
                    break;
                }

                case ENTERMULTIPLEEDIT: {
                    DJICameraError err = camera.getPlayback().enterMultipleEditMode();

                    if (err == null) {

                        //  if (num_photos1>=8)
                        handler.sendEmptyMessageDelayed(SELECTALL, 2000);
                        //  else if (num_photos1>0)
                        //  handler.sendEmptyMessageDelayed(SELECTFIRSTFILE, 2000);


                    }

                    break;
                }

                case 10: {
                    //  setResultToToast(" num" + j);
                    DJIError err = null;
                    camera.getPlayback().multiplePreviewPreviousPage();

                    handler.sendEmptyMessageDelayed(SELECTALL, 2000);

                    break;
                }
                case SELECTALL: {


                    DJIError err = null;
             /*       if (numbersOfSelected>0&& (num_photos-numbersOfSelected<8)){
                        int j=0;
                        int max=num_photos-numbersOfSelected;
                        for (int i=8;j<max+1;i--){
                    err = camera.getPlayback().toggleFileSelectionAtIndex(i);
                        j++;
                            try
                            {
                                Thread.sleep(2000);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessageDelayed(DOWNLOADIT, 2000);
                    }
                    else{*/
                    err = camera.getPlayback().selectAllFilesInPage();


                    if (err == null) {

                        new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (numbersOfSelected < num_photos) {
                                    //enter previous page
                                    handler.sendEmptyMessageDelayed(10, 2000);
                                } else {
                                    //download selected
                                    handler.sendEmptyMessageDelayed(DOWNLOADIT, 2000);
                                }
                            }
                        }.start();
                    //}

                        //showToast("downloades" +j);
                    }
                    break;
                }


                case DOWNLOADIT: {
                    Stitch s=new Stitch();

                    STITCHING_SOURCE_IMAGES_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/smartdrone/missions";
                    s.initDirectory(STITCHING_SOURCE_IMAGES_DIRECTORY);

                    File destDir = new File(STITCHING_SOURCE_IMAGES_DIRECTORY);
                    if (!destDir.exists()) {
                        destDir.mkdirs();
                    }

                    STITCHING_SOURCE_IMAGES_DIRECTORY+="/"+"mission"+destDir.listFiles().length;
                    File missions = new File(STITCHING_SOURCE_IMAGES_DIRECTORY);
                    if (!missions.exists()) {
                        missions.mkdirs();
                    }

                    /** The implementation of mFileDownloadCallBack could be found in the previous tutorial. **/
                    camera.getPlayback().downloadSelectedFiles(missions, mFileDownloadCallBack);

                    break;
                }
            }
            return false;
        }
    });


    @Override
    protected void onResume() {

        super.onResume();
        initFlightController();
        initMissionManager();
        initPreviewer();
        updateTitleBar();


        if (mVideoSurface1 == null) {
            setResultToToast("Empty surface");
            Log.e(TAG, "mVideoSurface is null");
        }
        Log.e(TAG, "onPause");
        uninitPreviewer();

        Log.d("functionCalls", "onResume");
        if (!OpenCVLoader.initDebug()) {
            Log.d("opencv", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);
        } else {
            Log.d("opencv", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {


        uninitPreviewer();

        super.onDestroy();

        unregisterReceiver(mReceiver);
    }

    /**
     * @Description : RETURN Button RESPONSE FUNCTION
     */
    public void onReturn(View view) {
        Log.d(TAG, "onReturn");
        this.finish();
    }

    private void setResultToToast(final String string) {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onCheckboxClicked(View v) {

        switch (v.getId()) {
            case R.id.photo1:{


                if (((CheckBox) v).isChecked()) {
                    Log.d(TAG,"phot checked");
                   // LinearLayout wayPointSettings = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_waypointsetting, null);
                    RadioGroup takephoto = (RadioGroup) wayPointSettings.findViewById(R.id.hi);
                    for (int i = 0; i < takephoto.getChildCount(); i++) {
                        takephoto.getChildAt(i).setEnabled(true);
                    }
                    takephoto1 = true;


                }else{

                    RadioGroup takephoto = (RadioGroup) wayPointSettings.findViewById(R.id.hi);
                    for (int i = 0; i < takephoto.getChildCount(); i++) {
                        takephoto.getChildAt(i).setEnabled(false);
                    }
                    takephoto1 = false;}
                }


                break;

            case R.id.optim_path:{
                if (((CheckBox) v).isChecked()) {

                    Log.d(TAG,"opti checked");
                    chekcedopti=true;
                }else
                    chekcedopti = false;

                break;
            }


            }

    }


    AlertDialog levelDialog;


    private void initUI() {

         Button config, prepare, start, stop;
        ImageButton fullview;

        LinearLayout wayPointSettings = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_waypointsetting, null);

        //new Coordinates(0,0).sealevel_altitute(drone_move);
        final TextView wpAltitude_TV = (TextView) wayPointSettings.findViewById(R.id.altitude);
        wpAltitude_TV.setEnabled(false);

        mVideoSurface1 = (TextureView) findViewById(R.id.video_previewer_surface);
       //

        if (null != mVideoSurface1) {

            mVideoSurface1.setSurfaceTextureListener(this);

        }

        initDownloadProgressDialog();
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar1);

        final TextView textView = (TextView) findViewById(R.id.textView1);


        // Initialize the textview with '0'.


        seekBar.setProgress(40);
        altitude = 40;
        textView.setText("Height: " + altitude);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {


                altitude = progresValue;
                if (progresValue<10){
                    seekBar.setProgress(10);
                    altitude =10;
                }

                // Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();

            }


            @Override

            public void onStartTrackingTouch(SeekBar seekBar) {
                mapView.setEnabled(false);
                // Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();

            }


            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {
                mapView.setEnabled(true);
                showLOG(marks.size()+" ");

                if (diagonios[0]!=null&&diagonios[1]!=null&&altitude>=10) {

                    if (mWaypointMission != null) {
                        mWaypointMission.removeAllWaypoints(); // Remove all the waypoints added to the task
                    }

                    for (int i = 1; i < marks.size(); i++) {
                        mapView.getLayerManager().getLayers().remove(marks.get(i));


                    }
                    for (int i = 1; i < markspolyline.size(); i++) {
                        mapView.getLayerManager().getLayers().remove(markspolyline.get(i));


                    }
                    markspolyline.clear();
                    for (int i = 0; i < calt.size(); i++)
                        mapView.getLayerManager().getLayers().remove(calt.get(i));
                    drone_move=null;
                    marks.clear();
                    if (calt!=null)
                    calt.clear();
                    final double[] lon = new double[2];
                    final double[] lan = new double[2];

                    lon[0] = corner[0].lon;

                    lan[0] = corner[0].lat;
                    lon[1] = corner[1].lon;
                    lan[1] = corner[1].lat;

                  if (type==2)
                        drone_move = Coordinates.create_grid(lon, lan, fov, altitude,chekcedopti);
                    else if (type==3)
                      drone_move = Coordinates.create_square(lon, lan, fov, altitude);
                    MainActivity m = new MainActivity();
                    if (drone_move != null) {
                        altitude_w=new float[drone_move.length];
                        Log.d("Demo ",  " " +drone_move.length);
                        m.addMarks(drone_move);
                    } else {
                        altitude_w=new float[4];
                        drone_move = new Coordinates[4];
                        drone_move[0] = corner[0];
                        drone_move[1] = new Coordinates(corner[1].lat, corner[0].lon);

                        drone_move[2] = corner[1];
                        drone_move[3] = new Coordinates(corner[0].lat, corner[1].lon);
                        m.addMarks(drone_move);
                    }
                }


                textView.setText("Height: " + altitude);

                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();

            }

        });


        final ImageButton showMenu = (ImageButton) findViewById(R.id.show_dropdown_menu);
        mProgress = (ProgressBar) findViewById(R.id.progressbar);
        progressbattery = (ProgressBar) findViewById(R.id.batterybar);


        mProgress.setVisibility(View.INVISIBLE);
        showMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), showMenu);
                dropDownMenu.getMenuInflater().inflate(R.menu.camera_menu, dropDownMenu.getMenu());



                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()) {
                            case R.id.resolution:
                            //    setResultToToast("0");

// Strings to Show In Dialog with Radio Buttons
                                final CharSequence[] items = {"160x120",
                                        "320x240",
                                        "640x480",
                                        "1024x600"};

                                // Creating and Building the Dialog
                                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                                builder.setTitle("Resolution");
                                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {


                                        switch (item) {
                                            case 0:
                                                builder.setTitle("160x120");
                                                imHeight = 120;
                                                imWidth = 160;
                                                break;
                                            case 1:
                                                imHeight = 240;
                                                imWidth = 320;
                                                // Your code when 2nd  option seletced
                                                builder.setTitle("320x240");
                                                break;
                                            case 2:
                                                imHeight = 480;
                                                imWidth = 640;
                                                // Your code when 3rd option seletced
                                                builder.setTitle("640x480");
                                                break;
                                            case 3:
                                                imHeight = 600;
                                                imWidth = 1400;
                                                builder.setTitle("1024x600");
                                                break;

                                        }
                                        levelDialog.dismiss();
                                    }
                                });

                                levelDialog = builder.create();
                                levelDialog.show();

                                break;


                            case R.id.opencv:

// Strings to Show In Dialog with Radio Buttons
                                final CharSequence[] items1 = {"Orginal mode",
                                        "Find top-view cars",
                                        "Find People",
                                        "Find Side cars"};

                                // Creatirsz_exit_full_screenng and Building the Dialog
                                final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);

                                builder1.setTitle("Camera mode");
                                builder1.setSingleChoiceItems(items1, -1, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {


                                        switch (item) {
                                            case 0:
                                                // stopService(new Intent(getBaseContext(), BackgroundService.class));

                                                mVideoSurface1.setVisibility(View.VISIBLE);
                                                opencvView.setVisibility(View.INVISIBLE);
                                                modec = false;
                                                // builder1.setTitle( "Orginal mode");
                                                //builder.wait(2000);
                                                break;
                                            case 1:
                                                opencvView.setImageDrawable(null);
                                                minWidth = 40;
                                                minHeight = 40;
                                                //mVideoSurface.setVisibility(View.INVISIBLE);
                                                opencvView.setVisibility(View.VISIBLE);

                                                modec = true;
                                                Log.d("modec", modec + " ");
                                                try {
                                                    // load cascade file from application resources
                                                    InputStream is = getResources().openRawResource(R.raw.cascade_good);
                                                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                                                    mCascadeFile = new File(cascadeDir, "cascade_good.xml");

                                                    FileOutputStream os = new FileOutputStream(mCascadeFile);

                                                    byte[] buffer = new byte[4096];
                                                    int bytesRead;
                                                    while ((bytesRead = is.read(buffer)) != -1) {
                                                        os.write(buffer, 0, bytesRead);
                                                    }
                                                    is.close();
                                                    os.close();

                                                    mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());

                                                    if (mJavaDetector.empty()) {
                                                        Log.e(TAG, "Failed to load cascade classifier");
                                                        mJavaDetector = null;
                                                    } else
                                                        Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                                                    cascadeDir.delete();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                //builder1.setTitle( "Find top-view cars");
                                                break;
                                            case 2:
                                                opencvView.setImageDrawable(null);
                                                minWidth = 48;
                                                minHeight = 96;
                                                opencvView.setVisibility(View.VISIBLE);

                                                modec = true;

                                                // mVideoSurface.setVisibility(View.INVISIBLE);

                                                try {
                                                    // load cascade file from application resources
                                                    InputStream is = getResources().openRawResource(R.raw.hogcascade_pedestrians);
                                                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                                                    mCascadeFile = new File(cascadeDir, "hogcascade_pedestrians.xml");

                                                    FileOutputStream os = new FileOutputStream(mCascadeFile);

                                                    byte[] buffer = new byte[4096];
                                                    int bytesRead;
                                                    while ((bytesRead = is.read(buffer)) != -1) {
                                                        os.write(buffer, 0, bytesRead);
                                                    }
                                                    is.close();
                                                    os.close();

                                                    mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());

                                                    if (mJavaDetector.empty()) {
                                                        Log.e(TAG, "Failed to load cascade classifier");
                                                        mJavaDetector = null;
                                                    } else
                                                        Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                                                    cascadeDir.delete();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                // builder1.setTitle("Find People");
                                                break;
                                            case 3:
                                                minWidth = 50;
                                                minHeight = 20;
                                                opencvView.setImageDrawable(null);
                                                //mVideoSurface.setVisibility(View.INVISIBLE);
                                                opencvView.setVisibility(View.VISIBLE);
                                                modec = true;

                                                try {
                                                    // load cascade file from application resources
                                                    InputStream is = getResources().openRawResource(R.raw.side_car);
                                                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                                                    mCascadeFile = new File(cascadeDir, "side_car.xml");

                                                    FileOutputStream os = new FileOutputStream(mCascadeFile);

                                                    byte[] buffer = new byte[4096];
                                                    int bytesRead;
                                                    while ((bytesRead = is.read(buffer)) != -1) {
                                                        os.write(buffer, 0, bytesRead);
                                                    }
                                                    is.close();
                                                    os.close();

                                                    mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());

                                                    if (mJavaDetector.empty()) {
                                                        Log.e(TAG, "Failed to load cascade classifier");
                                                        mJavaDetector = null;
                                                    } else
                                                        Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());

                                                    cascadeDir.delete();

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                // builder1.setTitle("Find Side cars");
                                                break;

                                        }
                                        levelDialog.dismiss();
                                    }
                                });

                                levelDialog = builder1.create();
                                levelDialog.show();
                                break;
                            case R.id.movecam:
                            {
                                 CharSequence[] items3 = {"Orientation move",
                                        "Tap move",
                                       };

                                // Creating and Building the Dialog
                                final AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);

                                builder2.setTitle("Resolution");
                                builder2.setSingleChoiceItems(items3, -1, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {


                                        switch (item) {
                                            case 0:
                                                tap=false;
                                               // builder2.setTitle("Orientation move");

                                                break;
                                            case 1:
                                                tap=true;
                                                //builder2.setTitle( "Tap move");
                                                break;

                                        }
                                        levelDialog.dismiss();
                                    }
                                });

                                levelDialog = builder2.create();
                                levelDialog.show();

                            }
                                break;

                        }


                        //  Toast.makeText(getApplicationContext(), "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                });
                dropDownMenu.show();
            }


        });
         Button mCaptureBtn, mShootPhotoModeBtn, mRecordVideoModeBtn, select;
         ToggleButton mRecordBtn;
        photo = (CheckBox) findViewById(R.id.photo1);
        alti_stay = (Button) findViewById(R.id.alt_stay);
        locate = (ImageButton) findViewById(R.id.locate);
        add = (Button) findViewById(R.id.add);
        add_waypoints = (Button) findViewById(R.id.add_w);
        clear = (Button) findViewById(R.id.clear);
        config = (Button) findViewById(R.id.config);
        prepare = (Button) findViewById(R.id.prepare);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        download = (Button) findViewById(R.id.download);
        select = (Button) findViewById(R.id.select);
        fullview = (ImageButton) findViewById(R.id.fullview);
       // add_waypoints.setEnabled(false);
       select.setOnClickListener(MainActivity.this);
        alti_stay.setOnClickListener(MainActivity.this);
        fullview.setOnClickListener(MainActivity.this);
        locate.setOnClickListener(MainActivity.this);
        add.setOnClickListener(MainActivity.this);
        add_waypoints.setOnClickListener(MainActivity.this);
        clear.setOnClickListener(MainActivity.this);
        config.setOnClickListener(MainActivity.this);
        prepare.setOnClickListener(MainActivity.this);
        start.setOnClickListener(MainActivity.this);
        stop.setOnClickListener(MainActivity.this);
        locate.setOnClickListener(MainActivity.this);
        add.setOnClickListener(MainActivity.this);
        download.setOnClickListener(MainActivity.this);
        clear.setOnClickListener(MainActivity.this);
        config.setOnClickListener(MainActivity.this);
        prepare.setOnClickListener(MainActivity.this);
        start.setOnClickListener(MainActivity.this);
        stop.setOnClickListener(MainActivity.this);
download.setEnabled(true);

        locate.setEnabled(false);
        alti_stay.setOnClickListener(MainActivity.this);

        locate.setEnabled(false);


        mConnectStatusTextView = (TextView) findViewById(R.id.ConnectStatusTextViewCamera);
        // init mVideoSurface


        altitute = (TextView) findViewById(R.id.altitude);
        speedy = (TextView) findViewById(R.id.speedy);
        opencvView = (ImageView) findViewById(R.id.ImageFeed);

        recordingTime = (TextView) findViewById(R.id.timer);
        mCaptureBtn = (Button) findViewById(R.id.btn_capture);
        mRecordBtn = (ToggleButton) findViewById(R.id.btn_record);
        mShootPhotoModeBtn = (Button) findViewById(R.id.btn_shoot_photo_mode);
        mRecordVideoModeBtn = (Button) findViewById(R.id.btn_record_video_mode);



        mCaptureBtn.setOnClickListener(this);
        mRecordBtn.setOnClickListener(this);
        mShootPhotoModeBtn.setOnClickListener(this);
        mRecordVideoModeBtn.setOnClickListener(this);

     /*   final GestureDetector detector1 = new GestureDetector(this, new TapCamera(getApplicationContext()));
        // TODO find your image view
        opencvView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector1.onTouchEvent(event);
                return true;
            }
        });*/

       // opencvView.setOnTouchListener(new TapCamera(MainActivity.this) {});

       // mVideoSurfaceFull.setOnTouchListener;
        TapCamera tp= new TapCamera(MainActivity.this);
        mVideoSurface1.setOnTouchListener(tp);
//        mVideoSurfaceFull.setOnTouchListener(new TapCamera(MainActivity.this));

//        recordingTime.setVisibility(View.INVISIBLE);

        mRecordBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new CameraDrone(MainActivity.this).startRecord();
                } else {
                    new CameraDrone(MainActivity.this).stopRecord();
                }
            }
        });
    }

    private void cameraUpdate() {


        this.mapView.setCenter(new LatLong(droneLocationLat, droneLocationLng));


        this.mapView.setZoomLevel((byte) 20);


    }

    private void showLOG(String str) {
        Log.e(TAG, str);
    }
static Activity a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        super.onCreate(savedInstanceState);

        // When the compile and target version is higher than 22, please request the
        // following permissions at runtime to ensure the
        // SDK work well.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.READ_PHONE_STATE,
                    }
                    , 1);
        }
        c1 = getApplicationContext();
        a = MainActivity.this;
        marks = new  LinkedList<>();
        calt = new LinkedList<>();
        diagonios = new Layer[2];
        markspolyline=new  LinkedList<>();
        // showLOG(testjni(STITCHING_SOURCE_IMAGES_DIRECTORY));
        // m=new Mark(getApplicationContext());
        //final GestureDetector gestureDetector = new GestureDetector(this, new SingleTDetector());

        TapMap tm = new TapMap(getApplicationContext(),MainActivity.this);

        TapMap.SingleTDetector s = tm.new SingleTDetector();
        final GestureDetector gestureDetector = new GestureDetector(this, s);


        AndroidGraphicFactory.createInstance(this.getApplication());
        // this.mapView = getMapView();
        // mapView = (MapView)findViewById(R.id.mapview);
        //this.mapView = new MapView(this);
        setContentView(R.layout.activity_main);



        //getCurrentCountry();


//MouseEventListener ms=new MouseEventListener(mapView);


        this.mapView = (MapView) findViewById(R.id.mapview);



        mapView.setClickable(true);
        mapView.getMapScaleBar().setVisible(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setZoomLevelMin((byte) 10);
        mapView.setZoomLevelMax((byte) 20);


new TapMap(getApplicationContext(),MainActivity.this).initialMap("cyprus.map");

        MainActivity.mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
              boolean result= new  TapMap().touch(event);
                gestureDetector.onTouchEvent(event);
                return(result );
            }
        });





       /* mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });*/
        IntentFilter filter = new IntentFilter();
        filter.addAction(FPVDemoApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);


        initUI();
        initCodecCam();
        mVideoSurface1.setVisibility(View.VISIBLE);
        opencvView.setVisibility(View.INVISIBLE);


// new CameraDrone(getApplicationContext()).intCamera();
        //Drone camera
        // The callback for receiving the raw H264 video data for camera live view
        // The callback for receiving the raw H264 video data for camera live view


new Stitch().initDirectory(Environment.getExternalStorageState()+"/smartdrone/missions");
        new Stitch().initDirectory(Environment.getExternalStorageState()+"/smartdrone/maps");

TapCamera tc=new TapCamera(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(tc, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        updateDroneLocation();

    }


        //TapCamera sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);








    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
Stitch s=new Stitch();
            if (  clickd==true && drone_move!=null&&drone_move.length>s.initDirectory(STITCHING_SOURCE_IMAGES_DIRECTORY)){

                clickd=false;
                hideDownloadProgressDialog();
                showToast("Not all photos downloaded. Try again");

            }



            onProductConnectionChange();

            updateTitleBar();
            onProductChange();
        }
    };

    private void onProductConnectionChange() {




        initMissionManager();
        initFlightController();
    }

    private void initMissionManager() {
        DJIBaseProduct product = FPVDemoApplication.getProductInstance();
        if (product == null || !product.isConnected()) {
            TapCamera.horizontal=true;
            TapCamera.vertical=true;
            // setResultToToast("Disconnected drone");
            mMissionManager = null;
            return;
        } else {

            setResultToToast("Product connected");
            mMissionManager = product.getMissionManager();
            // mMissionManager.se


            mMissionManager.setMissionProgressStatusCallback(this);


            mMissionManager.setMissionExecutionFinishedCallback(this);
        }

//mMissionManager.getMissionProgressStatusCallback().missionProgressStatus(this);
        //DJIWaypointMission.DJIWaypointMissionExecuteState a;


        mWaypointMission = new DJIWaypointMission();




    }

    @Override
    public void onGimbalStateUpdate(DJIGimbal controller,  DJIGimbalState gimbalState){
       // setResultToToast(gimbalState.isCalibrating()+" ");
       // gimbalState.setCalibrating(true);
        float p=gimbalState.getAttitudeInDegrees().pitch;
        float r=gimbalState.getAttitudeInDegrees().roll;
        float y=gimbalState.getAttitudeInDegrees().yaw;
        TapCamera.horizontal=false;
        TapCamera.vertical=false;
        if (r < 180||r > -180) {
            TapCamera.horizontal=true;
        }

        if (y < 90||y>-90) {
            TapCamera.vertical=true;
        }

        //setResultToToast(gimbalState.getRollFineTuneInDegrees()+" ");


    }
    private void initFlightController() {

        DJIBaseProduct product = FPVDemoApplication.getProductInstance();

        if (product != null && product.isConnected())
            if (product instanceof DJIAircraft) {
              //  product.getGimbal().rotateGimbalByAngle();
//                product.getGimbal().setGimbalAdvancedSettingsStateUpdateCallback(this);//setGimbalStateUpdateCallback(this);
                mFlightController = ((DJIAircraft) product).getFlightController();
            }




       // mGimbalUpdateAttitudeCallBack=new DJIGimbal();


        if (product != null && product.isConnected()) {
            if (product!=null&&product.getModel().equals(Model.Matrice_100))
                product.getBattery().setBatteryStateUpdateCallback(batterystatus);
            else if (product!=null&&product.getModel().equals(Model.M600))

                product.getBatteries().get(0).setBatteryStateUpdateCallback(batterystatus);
            if ( product.getGimbal()!=null){
                product.getGimbal().setGimbalStateUpdateCallback(this);
            product.getGimbal().resetGimbal(this);}



        }

        /*progressbattery.getProgressDrawable().setColorFilter(
                android.graphics.Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);*/
        if (mFlightController != null) {

            batterystatus = new DJIBattery.DJIBatteryStateUpdateCallback() {


                @Override
                public void onResult(final DJIBatteryState state) {
                    final TextView batteryPercent = (TextView) findViewById(R.id.battery);
                    final int battery = state.getBatteryEnergyRemainingPercent();
                    runOnUiThread(new Runnable() {
                        public void run() {

                            batteryPercent.setText(battery + "%");

                            progressbattery.setProgress(battery);
                        }
                    });
                }
            };





            locate.setEnabled(true);
            mFlightController.setUpdateSystemStateCallback(new DJIFlightControllerDelegate.FlightControllerUpdateSystemStateCallback() {
                @Override
                public void onResult(DJIFlightControllerCurrentState state) {





                    final TextView speedx = (TextView) findViewById(R.id.speedx);
                    final TextView speedu = (TextView) findViewById(R.id.speed);


                    final double uy = (state.getVelocityY());

                    final double ux = (state.getVelocityX());
                    final double u = Math.sqrt(Math.pow(uy, 2) + Math.pow(ux, 2));
                    final double alt = (state.getAircraftLocation().getAltitude());
                    statusdrone= new Coordinates((int)(Math.round(Math.sqrt(Math.pow(uy, 2) + Math.pow(ux, 2)))),(int)alt);

                    // setResultToToast(alt+" ");

if (state.isFlying()==true) {


    uploc = true;
}else
    uploc=false;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!uploc)
                                download.setEnabled(false);
                            else
                                download.setEnabled(true);
                            altitute.setText("Altitute: " + Math.round(alt) + " m");

                            speedu.setText("u: " + Math.round(Math.sqrt(Math.pow(uy, 2) + Math.pow(ux, 2))) + "m/s");
                            speedx.setText("Ux: " + Math.round(ux) + " m/s");
                            speedy.setText("Uy: " + Math.round(uy) + " m/s");
                        }
                    });



                    droneLocationLat = state.getAircraftLocation().getLatitude();
                   droneLocationLng = state.getAircraftLocation().getLongitude();

                    //  droneLocationLat = 35.036500;
                     // droneLocationLng = 33.060123;


                        if (state.areMotorsOn()&&state.isFlying()){

                           // setResultToToast("update location");
                            updateDroneLocation();}





                    // }


                }
            });
        }
    }

    /**
     * DJIMissionManager Delegate Methods
     */
    @Override
    public void
    missionProgressStatus(DJIMissionProgressStatus progressStatus) {

        if (progressStatus instanceof DJIWaypointMissionStatus) {
            DJIWaypointMissionStatus pointingStatus = (DJIWaypointMissionStatus) progressStatus;


            if (pointingStatus.isWaypointReached() ) {

                successphoto=0;

                if (photocheck){

                    successphoto=   ImageEdit.saveToInternalStorage(mVideoSurface1.getBitmap(),"DJI_"+   Coordinates.point+".JPG",STITCHING_SOURCE_IMAGES_DIRECTORY+"/photos");

                    //  if (m!=null)
                   // num_photos++;
                 //   mapView.getLayerManager().getLayers().remove(marks.get(Coordinates.point));
                    if (Coordinates.point<drone_move.length)
                    ReadWrite.write(STITCHING_SOURCE_IMAGES_DIRECTORY,false,drone_move[Coordinates.point],statusdrone.speed,statusdrone.height1);
                    //setResultToToast("hiiiiiiiiiii "+m);
                    Coordinates.point++;

                }


if (photoremote){
    Coordinates.point++;
    //for (int i=0;i<5;i++)
    successphoto= new CameraDrone(MainActivity.this).captureAction();
}

                Coordinates.point++;




            }


            if ((photoremote||photocheck)&&successphoto==0&& Coordinates.point>0){

                if (photoremote)

                successphoto=  new CameraDrone(MainActivity.this).captureAction();
            else {
                    successphoto = ImageEdit.saveToInternalStorage(mVideoSurface1.getBitmap(), "DJI_" + Coordinates.point + ".JPG", STITCHING_SOURCE_IMAGES_DIRECTORY + "/photos");
                    setResultToToast("hiiiiiiiiiii "+successphoto);
                }
            }

        }
    }

    /**
     * DJIMissionManager Delegate Methods
     */
    @Override
    public void onResult(DJIError error) {
        if (error==null&&drone_move!=null&&STITCHING_SOURCE_IMAGES_DIRECTORY!=null) {
            uninitPreviewer();
            new Coordinates(getApplicationContext(), this).addphotos(drone_move, STITCHING_SOURCE_IMAGES_DIRECTORY+"/photos");
            initPreviewer();
        }
        if (error==null)
            missionstart=true;
        photocheck = false;
        setResultToToast("Execution finished: " + (error == null ? "Success drone!" : error.getDescription()));
    }


    public void addMarks(Coordinates[] drone_move) {
        //mapView.getLayerManager().getLayers().remove(marks.get(0));

        //mapView.getLayerManager().getLayers().remove(marks.get(1));
        if (mWaypointMission != null) {
            mWaypointMission.removeAllWaypoints(); // Remove all the waypoints added to the task
        }
        altitude_w = new float[drone_move.length];
        Arrays.fill(altitude_w, 0);


        LatLong LatLong = new LatLong(drone_move[0].lat, drone_move[0].lon);


        DJIWaypoint mWaypoint = new DJIWaypoint(LatLong.latitude, LatLong.longitude, altitude);


        Run r;


        Mark k;

        LatLong LatLong2 = new LatLong(drone_move[0].lat, drone_move[0].lon);
        k = new Mark(LatLong2, null, 0, 0);
        marks.add(k);
        //Add Waypoints to Waypoint arraylist;
        if (mWaypointMission != null) {
            mWaypointMission.addWaypoint(mWaypoint);
        }
        Drawable drawable = c1.getResources().getDrawable(R.drawable.red_mark);
        org.mapsforge.core.graphics.Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.incrementRefCount();

        for (int i = 1; i < drone_move.length; i++) {


            //  Log.d("diagonios",i+" "+drone_move[i].toString());


            Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
            paint.setColor(Color.BLUE);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(8);

            if (i + 1 < drone_move.length) {
                Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);

                if (i == 1) {
                    List<LatLong> latLongs = polyline.getLatLongs();
                    latLongs.add(new LatLong(drone_move[0].lat, drone_move[0].lon));

                    latLongs.add(new LatLong(drone_move[1].lat, drone_move[1].lon));
                    latLongs.add(new LatLong(drone_move[2].lat, drone_move[2].lon));
                    markspolyline.add(polyline);

                } else {
                    List<LatLong> latLongs = polyline.getLatLongs();
                    latLongs.add(new LatLong(drone_move[i].lat, drone_move[i].lon));
                    latLongs.add(new LatLong(drone_move[i + 1].lat, drone_move[i + 1].lon));
                }
                mapView.getLayerManager().getLayers().add(polyline);
                markspolyline.add(polyline);

            }


            LatLong2 = new LatLong(drone_move[i].lat, drone_move[i].lon);

            k = new Mark(LatLong2, bitmap, 0, -bitmap.getHeight() / 2);

            //Log.d("diagonios",drone_move[i].toString());


//k.requestRedraw();


            mWaypoint = new DJIWaypoint(LatLong2.latitude, LatLong2.longitude, altitude);
            mWaypoint.addAction(new DJIWaypoint.DJIWaypointAction(DJIWaypointActionType.Stay,-1));
            //Add Waypoints to Waypoint arraylist;
            if (mWaypointMission != null) {
                mWaypointMission.addWaypoint(mWaypoint);
            }


            if (Double.compare(corner[1].lat, drone_move[i].lat) == 0 && Double.compare(corner[1].lon, drone_move[i].lon) == 0) {
               d=i;
                Log.d(TAG, "DIAGONIOS " +drone_move[i].toString());
               // continue;


            }
            else{
                k.setOnTabAction(r = new Run(i, c1));
            }


            mapView.getLayerManager().getLayers().add(k);
            marks.add(k);
        }
        LatLong = new LatLong(drone_move[0].lat, drone_move[0].lon);

        drawable = c1.getResources().getDrawable(R.drawable.start1);
        bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.incrementRefCount();



        k = new Mark(LatLong, bitmap, 0, -bitmap.getHeight() / 2);

        k.setOnTabAction( new Run(0, c1));
        marks.add(k);



        if (diagonios[1].getPosition().equals(k)==false)
        mapView.getLayerManager().getLayers().add(k);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                LinearLayout chooseAction = (LinearLayout) getLayoutInflater().inflate(R.layout.select_map, null);
                RadioGroup Raction = (RadioGroup) chooseAction.findViewById(R.id.choose);

                Raction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.cyprus) {
                            new TapMap(getApplicationContext(),MainActivity.this).initialMap("cyprus.map");
                        } else
                            new TapMap(getApplicationContext(),MainActivity.this).initialMap("greece.map");

                    }

                });
                new AlertDialog.Builder(this)
                        .setTitle("Choose map")
                        .setView(chooseAction)
                        .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }

                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }

                        })
                        .create()
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public static boolean checkGpsCoordination(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }


    // Update the drone location based on states from MCU.
    public void updateDroneLocation() {
        //
        //this.mapView.setZoomLevel((byte) 12);


       final Drawable drawable = getResources().getDrawable(R.drawable.drone1);
        final org.mapsforge.core.graphics.Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.incrementRefCount();

       // if (Double.compare(plat,droneLocationLat)!=0||Double.compare(plon,droneLocationLng)!=0)

            runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // showToast("droneeeee");
         uploc=false;
                if (droneMarker != null) {
                    mapView.invalidate();
                    mapView.getLayerManager().getLayers().remove(droneMarker);
                   // mapView.getLayerManager().redrawLayers();
                }

                if (checkGpsCoordination(droneLocationLat, droneLocationLng)) {
                    final LatLong pos = new LatLong(droneLocationLat, droneLocationLng);
            // plat=droneLocationLat;
//plon=droneLocationLng;


                    droneMarker = new Mark(pos, bitmap, 0, -bitmap.getHeight() / 2);



                                mapView.getLayerManager().getLayers().add(droneMarker);
                    mapView.getLayerManager().redrawLayers();

                    //marks.add(droneMarker);
                    //cameraUpdate();

                }

            }
        });


    }


    private void markWaypoint(LatLong point) {
       /* //Create MarkerOptions object
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Marker marker = gMap.addMarker(markerOptions);
        mMarkers.put(mMarkers.size(), marker);*/

        /*LinearLayout wayPointSettings = (LinearLayout) getLayoutInflater().inflate(R.layout.altitute_waypoints, null);

        final TextView Altitude_TV = (TextView) wayPointSettings.findViewById(R.id.altitude);

        i++;

        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(wayPointSettings)
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        altitutes_w[x] = Altitude_TV.getText().toString();

                        altitude_w[x] = Integer.parseInt(nulltoIntegerDefalt(altitutes_w[x]));

                        x++;
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                })
                .create()
                .show();*/


    }

    public void changeflag() {

        flag = false;
    }



    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.select:{
              //  STITCHING_SOURCE_IMAGES_DIRECTORY=null;
                STITCHING_SOURCE_IMAGES_DIRECTORY=Environment.getExternalStorageDirectory()+"/smartdrone/missions/mission55/photos";
                if (drone_move!=null&&STITCHING_SOURCE_IMAGES_DIRECTORY!=null) {

                    uninitPreviewer();
                    new Coordinates(getApplicationContext(), this).addphotos(drone_move, STITCHING_SOURCE_IMAGES_DIRECTORY);
                    initPreviewer();
                }
                break;
            }
            case R.id.download: {

                DJIBaseProduct product = FPVDemoApplication.getProductInstance();
                if (product!=null&&product.isConnected())
                mFileDownloadCallBack = new DJIPlaybackManager.CameraFileDownloadCallback() {

                    @Override
                    public void onStart() {
                        clickd=true;
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                showDownloadProgressDialog();
                            }
                        });

                    }

                    @Override
                    public void onError(Exception exception) {
                        // TODO Auto-generated method stub

                        hideDownloadProgressDialog();
                        showToast("Error try again");


                        DJILogHelper.getInstance().LOGD("", "download OnError :" + exception.toString(), true, false);
                    }

                    @Override
                    public void onEnd() {
                        Stitch s=new Stitch();
                        hideDownloadProgressDialog();
                        // TODO Auto-generated method stub
                        if (   drone_move!=null&&drone_move.length>s.initDirectory(STITCHING_SOURCE_IMAGES_DIRECTORY)){
                           hideDownloadProgressDialog();
                            showToast("Not all photos downloaded. Try again");

                        }
                        else
                        deletepPicslist();
                        //new CameraDrone(MainActivity.this).switchCameraMode(DJICameraSettingsDef.CameraMode.ShootPhoto);

                        DJILogHelper.getInstance().LOGD("", "download OnEnd", true, false);

                       // setResultToToast("previous " + num_photos);


                    }

                    @Override
                    public void onProgressUpdate(final int progress) {
                        // TODO Auto-generated method stub
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if(mDownloadDialog!=null)
                                {
                                    mDownloadDialog.setProgress(progress);
                                }
                                if(progress>=100)
                                {
                                    hideDownloadProgressDialog();
                                }
                            }
                        });
                        DJILogHelper.getInstance().LOGD("", "download OnProgressUpdate progress=" + progress, true, false);
                    }

                };
                if (product!=null&&product.getCamera()!=null&&!product.getModel().equals(Model.UnknownAircraft)) {
                    DJICamera camera = product.getCamera();
                    if (camera != null) {
                        // Set the callback
                        mCameraPlayBackStateCallBack = new DJIPlaybackManager.DJICameraPlayBackStateCallBack() {
                            @Override
                            public void onResult(DJICameraPlaybackState mState) {
                                numbersOfSelected = mState.numberOfSelected;
                            }
                        };

                        camera.getPlayback().setDJICameraPlayBackStateCallBack(mCameraPlayBackStateCallBack);
                        camera.setDJICameraReceivedVideoDataCallback(mReceivedVideoDataCallBack);

                    }
                }
                handler.sendEmptyMessage(STARTAUTODOWNLOAD);
                if (num_photos>0&&drone_move!=null){

                    handler.sendEmptyMessage(STARTAUTODOWNLOAD);


                }
                else if  (drone_move!=null&&num_photos>0&&num_photos<drone_move.length) {

                    handler.sendEmptyMessage(STARTAUTODOWNLOAD);
                    setResultToToast("Missing photos from mission");



                }
                else{

                    setResultToToast("Not photos taken "+num_photos);
                }

            /*    STITCHING_SOURCE_IMAGES_DIRECTORY=Environment.getExternalStorageDirectory()+"/smartdrone/missions/mission55/photos";
                if (drone_move!=null&&STITCHING_SOURCE_IMAGES_DIRECTORY!=null) {

                    uninitPreviewer();
                    new Coordinates(getApplicationContext(), this).addphotos(drone_move, STITCHING_SOURCE_IMAGES_DIRECTORY);
                    initPreviewer();
                }*/

                break;
            }
            case R.id.alt_stay: {

                if (change == false) {
                    window_change_altitute();
                    change = true;
                    //setResultToToast(drone_move.length+"");
                    alti_stay.setText("Finish");

                } else {

                    change = false;
                    alti_stay.setText("Points settings");

                }

                break;
            }

            case R.id.locate: {


                if (checkGpsCoordination(droneLocationLat, droneLocationLng)) {
                    showToast(droneLocationLat+" "+droneLocationLng);
                    //uploc=true;
                    updateDroneLocation();
                    cameraUpdate();

                }


                break;
            }
            case R.id.add: {

                if (!isAdd) {

                    LinearLayout chooseAction = (LinearLayout) getLayoutInflater().inflate(R.layout.choose_action, null);
                    RadioGroup Raction = (RadioGroup) chooseAction.findViewById(R.id.choose);

                    Raction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (checkedId == R.id.drone) {
                                clearpoints();
                                type = 1;
                                enableDisableAdd();
                            } else if (checkedId == R.id.grid) {
                                clearpoints();
                                diagonii = new Coordinates[2];
                                type = 2;
                            } else if (checkedId == R.id.square) {

                                diagonii = new Coordinates[2];
                                clearpoints();
                                //changeflag();
                                type = 3;
                            } else if (checkedId == R.id.fromefile) {
                                clearpoints();
                               type=0;
                            }

                        }

                    });
                    new AlertDialog.Builder(this)
                            .setTitle("")
                            .setView(chooseAction)
                            .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                   if (type==0)
                                       readfile();

                                }

                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }

                            })
                            .create()
                            .show();
                } else
                    enableDisableAdd();

                break;
            }

            case R.id.add_w: {


                init_waypoints_settings();


                break;
            }

            case R.id.clear: {
                STITCHING_SOURCE_IMAGES_DIRECTORY=null;
               drone_move=null;
               // for (int i = 1; i <   mapView.getLayerManager().getLayers().size(); i++)
                 // mapView.getLayerManager().getLayers().remove(i);
if (!calt.isEmpty()){

    for (int i = 0; i < calt.size(); i++)
        mapView.getLayerManager().getLayers().remove(calt.get(i));

}
                second = 0;
                //add_waypoints.setEnabled(false);
                corner_points = 0;
                if (!TapMap.random_points.isEmpty())
                    TapMap.random_points.clear();

                for (int i = 0; i < marks.size(); i++)
                    mapView.getLayerManager().getLayers().remove(marks.get(i));
                for (int i = 0; i < markspolyline.size(); i++)
                    mapView.getLayerManager().getLayers().remove(markspolyline.get(i));
                if (!marks.isEmpty())
                    marks.clear();
                markspolyline.clear();
                if (diagonios[0] != null)
                    for (int i = 0; i < diagonios.length&&diagonios[i]!=null; i++)

                        mapView.getLayerManager().getLayers().remove(diagonios[i]);

                if (diagonios[1]!=null)
                mapView.getLayerManager().getLayers().remove(diagonios[1]);
                diagonios = new Layer[2];

                if (random_marks != null)
                    for (int i = 0; i < random_marks.size(); i++)
                        mapView.getLayerManager().getLayers().remove(random_marks.get(i));
                if (mWaypointMission != null) {
                    mWaypointMission.removeAllWaypoints(); // Remove all the waypoints added to the task
                }
                break;
            }


            case R.id.config: {
                setResultToToast("hiiiiiiiiiiiiiiiiiiiiii ");
                if (mWaypointMission!=null)
                setResultToToast(mWaypointMission.waypointsList.size()+" ");
                showSettingDialog();
                break;
            }
            case R.id.prepare: {
                prepareWayPointMission();
                break;
            }
            case R.id.start: {
                startWaypointMission();
                break;
            }
            case R.id.stop: {
                stopWaypointMission();
                break;
            }
            case R.id.btn_capture: {
                opencvView.setVisibility(View.INVISIBLE);
                new CameraDrone(MainActivity.this).captureAction();
                break;
            }
            case R.id.btn_shoot_photo_mode: {
                opencvView.setVisibility(View.INVISIBLE);
                new CameraDrone(MainActivity.this).switchCameraMode(DJICameraSettingsDef.CameraMode.ShootPhoto);
                break;
            }
            case R.id.btn_record_video_mode: {
                opencvView.setVisibility(View.INVISIBLE);
                new CameraDrone(MainActivity.this).switchCameraMode(DJICameraSettingsDef.CameraMode.RecordVideo);
                break;
            }
            case R.id.fullview:{
                if (changescreen){changescreen=false;


                    ImageButton b=(ImageButton)findViewById(R.id.fullview);
                    b.setImageResource(R.drawable.rsz_exit_full_screen);
                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    int width = display.getWidth();  // deprecated

                    // new MainActivity().uninitPreviewer();
                    RelativeLayout relative = (RelativeLayout) findViewById(R.id.map);

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
                    ImageButton b=(ImageButton)findViewById(R.id.fullview);
                    b.setImageResource(R.drawable.rsz_full);
                    // new MainActivity().uninitPreviewer();
                    RelativeLayout relative = (RelativeLayout) a.findViewById(R.id.map);
                    //  android.view.ViewGroup.LayoutParams avvglp =   relative.getLayoutParams();
                    //avvglp.height=1000;
                    //  avvglp.width= 0;

                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                            0,
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            1
                    );

                    relative.setLayoutParams(param);



                }
                break;
            }

            default:
                break;
        }
    }


    private void enableDisableAdd() {
        if (isAdd == false) {

            isAdd = true;
            add.setText("Exit");
        } else {
            if (!TapMap.random_points.isEmpty()) {
                altitude_w = new float[TapMap.random_points.size()];
                Arrays.fill(altitude_w, 0);
            }
            isAdd = false;
            add.setText("Add");
        }
    }

    private void showSettingDialog() {
        takephoto1=false;
         wayPointSettings = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_waypointsetting, null);

        final TextView wpAltitude_TV = (TextView) wayPointSettings.findViewById(R.id.altitude);


       // photo.setonisetOnClickListener(this);
       // photo.setChecked(takephoto1);
        RadioGroup speed_RG = (RadioGroup) wayPointSettings.findViewById(R.id.speed);
        RadioGroup actionAfterFinished_RG = (RadioGroup) wayPointSettings.findViewById(R.id.actionAfterFinished);
        RadioGroup heading_RG = (RadioGroup) wayPointSettings.findViewById(R.id.heading);
        RadioGroup takephoto = (RadioGroup) wayPointSettings.findViewById(R.id.hi);
        wpAltitude_TV.setText(altitude + "");
        speed_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.lowSpeed) {
                    mSpeed = 3.0f;
                } else if (checkedId == R.id.MidSpeed) {
                    mSpeed = 5.0f;
                } else if (checkedId == R.id.HighSpeed) {
                    mSpeed = 10.0f;
                }
            }

        });

        actionAfterFinished_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Log.d(TAG, "Select finish action");
                if (checkedId == R.id.finishNone) {
                    mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.NoAction;
                } else if (checkedId == R.id.finishGoHome) {
                    mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.GoHome;
                } else if (checkedId == R.id.finishAutoLanding) {
                    mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.AutoLand;
                } else if (checkedId == R.id.finishToFirst) {
                    mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.GoFirstWaypoint;
                }
            }
        });

        heading_RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d(TAG, "Select heading");

                if (checkedId == R.id.headingNext) {
                    mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.Auto;
                } else if (checkedId == R.id.headingInitDirec) {
                    mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.UsingInitialDirection;
                } else if (checkedId == R.id.headingRC) {
                    mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.ControlByRemoteController;
                } else if (checkedId == R.id.headingWP) {
                    mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.UsingWaypointHeading;
                }
            }
        });


        Stitch s=new Stitch();

        STITCHING_SOURCE_IMAGES_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/smartdrone/missions";
        s.initDirectory(STITCHING_SOURCE_IMAGES_DIRECTORY);

        File destDir = new File(STITCHING_SOURCE_IMAGES_DIRECTORY);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }




        STITCHING_SOURCE_IMAGES_DIRECTORY+="/"+"mission"+destDir.listFiles().length;
        File missions = new File(STITCHING_SOURCE_IMAGES_DIRECTORY);
        if (!missions.exists()) {
            missions.mkdirs();
        }

        ReadWrite.write(STITCHING_SOURCE_IMAGES_DIRECTORY,true,new Coordinates(0,0),0,0);



        missions = new File(STITCHING_SOURCE_IMAGES_DIRECTORY+"/photos");
        if (!missions.exists()) {
            missions.mkdirs();
        }


        photocheck=false;
        photocheck_stop=false;

        photoremote=false;

        for (int i = 0; i < takephoto.getChildCount(); i++) {
            takephoto.getChildAt(i).setEnabled(false);
        }



            takephoto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    Log.d(TAG, "Select heading");

                    if (checkedId == R.id.photo_continue) {


                        photocheck = true;
                    } else if (checkedId == R.id.photo_stop) {

                        photocheck_stop = true;
                    } else if (checkedId == R.id.shoot_photo_distance_ihnterval) {

                        //photointerval=true;
                    } else if (checkedId == R.id.continue_remote) {
                        // new CameraDrone(MainActivity.this).switchCameraMode(DJICameraSettingsDef.CameraMode.ShootPhoto);
                        photoremote = true;
                    }

                }
            });
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setView(wayPointSettings)
                    .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            String altitudeString = wpAltitude_TV.getText().toString();
                            // altitude = Integer.parseInt(nulltoIntegerDefalt(altitudeString));
                            Log.e(TAG, "altitude " + altitude);
                            Log.e(TAG, "speed " + mSpeed);
                            Log.e(TAG, "mFinishedAction " + mFinishedAction);
                            Log.e(TAG, "mHeadingMode " + mHeadingMode);
                            configWayPointMission();
                        }

                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }

                    })
                    .create()
                    .show();

    }


    String nulltoIntegerDefalt(String value) {
        if (!isIntValue(value)) value = "0";
        return value;
    }

    boolean isIntValue(String val) {
        try {
            val = val.replace(" ", "");
            Integer.parseInt(val);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void configWayPointMission() {

        DJIBaseProduct product = FPVDemoApplication.getProductInstance();
        if ( product!=null&&product.getGimbal()!=null&&(takephoto1)) {
            DJIGimbalAngleRotation mYaw_relative = new DJIGimbalAngleRotation(true, -90, DJIGimbalRotateDirection.Clockwise);


            DJIGimbalAngleRotation mYaw_relative1 = new DJIGimbalAngleRotation(true, 0, DJIGimbalRotateDirection.Clockwise);

           DJIGimbalAngleRotation mYaw_relative2 = new DJIGimbalAngleRotation(true, 0, DJIGimbalRotateDirection.Clockwise);

            product.getGimbal().rotateGimbalByAngle(DJIGimbalRotateAngleMode.RelativeAngle, mYaw_relative, mYaw_relative1, mYaw_relative2, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    //setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
                }
            });
        }
        //DJIGimbalRotation mYaw_relative = new DJIGimbalRotation(true,false,false, 1000);
        //product.getDjiGimbal().updateGimbalAttitude(null,null,mYaw_relative);

        LinearLayout wayPointSettings = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_waypointsetting, null);


        final TextView wpAltitude_TV = (TextView) wayPointSettings.findViewById(R.id.altitude);
        wpAltitude_TV.setText(altitude+"");
        //new Coordinates(0,0).sealevel_altitute(drone_move);
       // final TextView wpAltitude_TV = (TextView) wayPointSettings.findViewById(R.id.altitude);
        //wpAltitude_TV.setEnabled(false);
        //altitude = Integer.parseInt(nulltoIntegerDefalt(altitudeString));

        setResultToToast("photo take " + photocheck);
        if (mWaypointMission != null) {
            mWaypointMission.flightPathMode = DJIWaypointMission.DJIWaypointMissionFlightPathMode.Curved;
            mWaypointMission.goFirstWaypointMode= DJIWaypointMission.DJIWaypointMissionGotoWaypointMode.PointToPoint;
            mWaypointMission.finishedAction = mFinishedAction;
            mWaypointMission.headingMode = mHeadingMode;
            mWaypointMission.autoFlightSpeed = mSpeed;
            //setResultToToast("total "+ mWaypointMission.waypointsList.size());
           // DJIWaypoint.DJIWaypointAction mstay = new DJIWaypoint.DJIWaypointAction(DJIWaypointActionType.Stay, 0);
           //  DJIWaypoint.DJIWaypointAction tphoto = new DJIWaypoint.DJIWaypointAction(DJIWaypointActionType.StartTakePhoto, 1);

            if (drone_move!=null)
            num_photos=0;
            if (mWaypointMission.waypointsList.size() > 0) {
                for (int i = 0; i < mWaypointMission.waypointsList.size(); i++) {
                    //setResultToToast("Add point ");


                    if (photocheck_stop){ setResultToToast("stop take photo ");
                    mWaypointMission.getWaypointAtIndex(i).addAction(new DJIWaypoint.DJIWaypointAction(DJIWaypointActionType.StartTakePhoto, i));}
               /*   else if  (photointerval){   setResultToToast("photo interval ");
                      //  mWaypointMission.getWaypointAtIndex(i).shootPhotoDistanceInterval=Coordinates.r-1;
                        mWaypointMission.getWaypointAtIndex(i).shootPhotoTimeInterval=0;
                        mWaypointMission.getWaypointAtIndex(i).shootPhotoDistanceInterval=25;

                    }*/

                    if (altitude_w != null && altitude_w[i] > 0) {
                       // setResultToToast("Change altitute "+ altitude_w[i]);
                        mWaypointMission.getWaypointAtIndex(i).altitude = altitude_w[i];
                    } else{
                        mWaypointMission.getWaypointAtIndex(i).altitude = altitude;
                    //mWaypointMission.getWaypointAtIndex(i).altitude = altitude_w[i];
                    // Log.d(TAG, "altitute "+mWaypointMission.getWaypointAtIndex(i).altitude);
                   // showToast(mWaypointMission.getWaypointAtIndex(i).altitude+" ");

                    }
                  //  mWaypointMission.getWaypointAtIndex(i).addAction(tphoto);

                   // mWaypointMission.getWaypointAtIndex(i).insertAction(new DJIWaypoint.DJIWaypointAction(DJIWaypointActionType.Stay,0), i);
                   // mWaypointMission.getWaypointAtIndex(i).addAction());


                }
            }
        }

    }

    private void prepareWayPointMission() {
       /* if (mWaypointMission!=null&&mWaypointMission.waypointsList.size() > 0)
            for (int i = 0; i < mWaypointMission.waypointsList.size(); i++) {

                setResultToToast(mWaypointMission.waypointsList.get(i).altitude + "");

            }*/
            if (mMissionManager != null && mWaypointMission != null) {

                DJIMission.DJIMissionProgressHandler progressHandler = new DJIMission.DJIMissionProgressHandler() {
                    @Override
                    public void onProgress(DJIMission.DJIProgressType type, float progress) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgress.setVisibility(View.VISIBLE);
//stuff that updates ui

                            }
                        });


                        mProgress.setProgress((int) (progress * 100));
                    }
                };

                mMissionManager.prepareMission(mWaypointMission, progressHandler, new DJICommonCallbacks.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError error) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                        mProgress.setVisibility(View.INVISIBLE);

                            }
                        });
                        setResultToToast(error == null ? "Mission Prepare Successfully" : error.getDescription());
                    }
                });
            }



    }

    protected void warningAltitute(){

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        num_photos=0;
                        if (mMissionManager != null) {

                            mMissionManager.startMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {
                                @Override
                                public void onResult(DJIError error) {
                                    setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
                                }
                            });

                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Height is less than 15 meters. Are you sure to continue?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }

    private void startWaypointMission(){
        Coordinates.point=0;
if (altitude<=15)
    warningAltitute();
        else{

    num_photos=0;
    if (mMissionManager != null) {

        mMissionManager.startMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {
            @Override
            public void onResult(DJIError error) {

                if (error==null)
                    missionstart=true;

                setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
            }
        });

    }

        }



    }
    private void stopWaypointMission(){

        if (mMissionManager != null) {
            mMissionManager.stopMissionExecution(new DJICommonCallbacks.DJICompletionCallback() {

                @Override
                public void onResult(DJIError error) {
                    if (error==null)
                        missionstart=false;
                    setResultToToast("Mission Stop: " + (error == null ? "Successfully" : error.getDescription()));
                }
            });

            if (mWaypointMission != null){
                mWaypointMission.removeAllWaypoints();
            }
        }
    }

    public int mid = 0, dr = 0;


    //Drone Camera


    private void updateTitleBar() {
        if (mConnectStatusTextView == null) return;
        boolean ret = false;
        DJIBaseProduct product = FPVDemoApplication.getProductInstance();
        if (product != null) {
            if (product.isConnected()) {
                //The product is connected
                mConnectStatusTextView.setText(product + " Connected");
                //mConnectStatusTextView.setText(FPVDemoApplication.getProductInstance().getModel() + " Connected");
                ret = true;
            } else {
                if (product instanceof DJIAircraft) {
                    DJIAircraft aircraft = (DJIAircraft) product;
                    if (aircraft.getRemoteController() != null && aircraft.getRemoteController().isConnected()) {
                        // The product is not connected, but the remote controller is connected
                        mConnectStatusTextView.setText("only RC Connected");
                        ret = true;
                    }
                }
            }
        }

        if (!ret) {
            // The product or the remote controller are not connected.
           // mConnectStatusTextView.setText("Disconnected");
        }
    }

    protected void onProductChange() {
        initPreviewer();
    }



    public void initPreviewer() {

        DJIBaseProduct product = FPVDemoApplication.getProductInstance();

        if (product == null || !product.isConnected()) {
            //showToast(getString(R.string.disconnected));
        } else {



            if (!product.getModel().equals(Model.UnknownAircraft)) {
                DJICamera camera = product.getCamera();
                if (camera != null){
                    // Set the callback
                    camera.setDJICameraReceivedVideoDataCallback(mReceivedVideoDataCallBack);
                }
            }
        }
    }

    public void uninitPreviewer() {

        DJICamera camera = FPVDemoApplication.getCameraInstance();
        if (camera != null) {

            // Reset the callback
            FPVDemoApplication.getCameraInstance().setDJICameraReceivedVideoDataCallback(null);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.v(TAG, "onSurfaceTextureAvailable");

        //DJICamera camera = FPVDemoApplication.getCameraInstance();
         if (mCodecManager == null ) {
        //Normal init for the surface
        mCodecManager = new DJICodecManager(this, surface, width, height);
        Log.v(TAG, "Initialized CodecManager");
         }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if (mCodecManager != null) {
            mCodecManager.cleanSurface();
            mCodecManager = null;
        }
        if (mCodecManager == null ) {
            //Normal init for the surface
            mCodecManager = new DJICodecManager(this, surface, width, height);
            Log.v(TAG, "Initialized CodecManager");
        }

        initCodecCam();
       // mVideoSurface1.setSurfaceTextureListener(this);
        Log.e(TAG, "onSurfaceTextureSizeChanged");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {


        Log.e(TAG, "onSurfaceTextureDestroyed");
        if (mCodecManager != null) {
            mCodecManager.cleanSurface();
            mCodecManager = null;
        }

        return false;
    }

     android.graphics.Bitmap bm;
    int log = 0;

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {


        if (modec) {
          //  showToast("onSurfaceTextureDestroyed");
            image = mVideoSurface1.getBitmap();
            if (log == 1) {
                return;

            }


            bm = image.copy(image.getConfig(), true);
            new AsychronousCamera(opencvView).execute(bm);


        }




    }

    private class AsychronousCamera extends AsyncTask<android.graphics.Bitmap, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;


        public AsychronousCamera(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        protected void onPreExecute() {


        }


        protected Bitmap doInBackground(android.graphics.Bitmap... image) {
            log = 1;

            if (image[0] == null)
                return null;
            Mat rgba = new Mat();
            Mat gray = new Mat();
            Mat resizedImg = Mat.zeros(new Size(imWidth, imHeight), rgba.type());

            Utils.bitmapToMat(image[0], rgba);

            Imgproc.resize(rgba, resizedImg, new Size(imWidth, imHeight));
            //Toast.makeText(this.getApplicationContext(), String.format("%d - %d", resizedImg.width(), resizedImg.height()), Toast.LENGTH_SHORT).show();
            Imgproc.cvtColor(resizedImg, gray, Imgproc.COLOR_RGBA2GRAY);
            Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0);

//
//            try {
//                // load cascade file from application resources
//                InputStream is = getResources().openRawResource(R.raw.haarcascade_fullbody);
//                File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
//                mCascadeFile = new File(cascadeDir, "haarcascade_fullbody.xml");
//
//                FileOutputStream os = new FileOutputStream(mCascadeFile);
//
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = is.read(buffer)) != -1) {
//                    os.write(buffer, 0, bytesRead);
//                }
//                is.close();
//                os.close();
//
//                mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
//
//                if (mJavaDetector.empty()) {
//                    Log.e(TAG, "Failed to load cascade classifier");
//                    mJavaDetector = null;
//                } else
//                    Log.i(TAG, "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
//
//                cascadeDir.delete();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            if (mJavaDetector != null) {

                MatOfRect objects = new MatOfRect();
                mJavaDetector.detectMultiScale(gray, objects, 1.1, 2, Objdetect.CASCADE_SCALE_IMAGE, new Size(minWidth, minHeight), new Size());
               // mJavaDetector.detectMultiScale(gray, objects, 1.01, 1, Objdetect.CASCADE_SCALE_IMAGE, new Size(minWidth, minHeight), new Size());
                // Each rectangle in the faces array is a face
                // Draw a rectangle around each face
                Rect[] objArray = objects.toArray();
                for (int i = 0; i < objArray.length; i++)
                    Core.rectangle(resizedImg, objArray[i].tl(), objArray[i].br(), new Scalar(0, 255, 0, 255), 3);

            }

            Imgproc.resize(resizedImg, rgba, rgba.size());
            Utils.matToBitmap(rgba, image[0]);

            Canvas imCanvas = new Canvas(image[0]);
            imCanvas.drawBitmap(image[0], 0, 0, null);

            return image[0];

        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Bitmap result) {

            //opencvView.setImageDrawable(new BitmapDrawable(getResources(), result));

            if (imageViewReference != null && result != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(result);
                }
            }
            log = 0;
        }
    }


    public void showToast(final String msg) {

        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    static boolean changed = false;

    public void init_waypoints_settings() {
        LinearLayout wayPointSettings = (LinearLayout) getLayoutInflater().inflate(R.layout.labyrinth_settings, null);
        final double[] lon = new double[2];
        final double[] lan = new double[2];

        final EditText a = (EditText) wayPointSettings.findViewById(R.id.ang);
        //final EditText h = (EditText) wayPointSettings.findViewById(R.id.h);


        new AlertDialog.Builder(this)
                .setTitle("Field of view")
                .setView(wayPointSettings)
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        fov = Integer.parseInt((a.getText().toString()));

                    }

                })
                .create()
                .show();
    }

    public void window_change_altitute() {

        final LinearLayout chooseAction = (LinearLayout) getLayoutInflater().inflate(R.layout.change_point_altitute, null);


        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(chooseAction)
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        EditText e = (EditText) (chooseAction.findViewById(R.id.alti_change));

                        newalti = Integer.parseInt((e.getText().toString()));

                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }

                })
                .create()
                .show();
    }


    /**
     * Async task class to get json by making HTTP call
     */

    public void readfile() {


        List<Coordinates> distance = new LinkedList<Coordinates>();
        String line = null;
        Drawable drawable = getResources().getDrawable(R.drawable.red_mark);
        org.mapsforge.core.graphics.Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.incrementRefCount();

        try {

            File f = new File(Environment.getExternalStorageDirectory().getPath() + "/smartdrone", "passaloi(ucy)");
            // FileInputStream fileInputStream = new FileInputStream (f);

            Scanner scan = new Scanner(f);


            //InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            //  BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            scan.nextLine();


            while ((scan.hasNext())) {
                scan.next();
                float alt = scan.nextInt();
                double lat = scan.nextDouble();
                double lon = scan.nextDouble();


                //Mark k = new Mark(new LatLong(lon,lat), bitmap, 0, -bitmap.getHeight() / 2);
                if (checkGpsCoordination(lat, lon))

                    distance.add(new Coordinates(new LatLong(lon, lat), Coordinates.distFrom(droneLocationLat, droneLocationLng, lon, lat), alt,MainActivity.this));


            }
            //fileInputStream.close();


        } catch (FileNotFoundException ex) {

            Log.d(TAG, ex.getMessage());
            return;
        } catch (IOException ex) {

            Log.d(TAG, ex.getMessage());
            return;
        }

        //drawable = getResources().getDrawable(R.drawable.aircraft);
        // bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        //bitmap.incrementRefCount();


        Collections.sort(distance, new Comparator<Coordinates>() {
            @Override
            public int compare(Coordinates o1, Coordinates o2) {
                if (o1.distance > o2.distance)
                    return 1;
                else if (o1.distance < o2.distance)
                    return -1;
                return 0;
            }
        });
        // mapView.getLayerManager().getLayers().add(k);
        //distance=bubbleSort(distance);
        Log.d("Distance ", " " + distance.size());
        for (int i = 0; i < distance.size(); i++) {
            Log.d("Distance ", " " + distance.get(i).distance);
        }
        //Log.d("Distance "," "+distance.get(0).distance);
        Mark k;

        // k = new Mark(new LatLong(droneLocationLat,droneLocationLng), bitmap, 0, -bitmap.getHeight() / 2);

       //  mapView.getLayerManager().getLayers().add(k);
         //this.mapView.setCenter(k.getLatLong());
        if (distance.get(0).distance < 2) {
            drawable = getResources().getDrawable(R.drawable.start1);
            bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
            bitmap.incrementRefCount();
            //Log.d("Location ",distance.get(0).l.+" "+distance.get(0).lon);
            k = new Mark(distance.get(0).l, bitmap, 0, -bitmap.getHeight() / 2);
            this.mapView.setCenter(distance.get(0).l);


            this.mapView.setZoomLevel((byte) 20);
            marks.add(k);
            mapView.getLayerManager().getLayers().add(k);
            DJIWaypoint mWaypoint = new DJIWaypoint(distance.get(0).l.latitude, distance.get(0).l.longitude, altitude);
            //Add Waypoints to Waypoint arraylist;
            if (mWaypointMission != null) {

                mWaypointMission.addWaypoint(mWaypoint);
            }
        }


        drawable = getResources().getDrawable(R.drawable.red_mark);
        bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.incrementRefCount();
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(8);
        Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);
        List<LatLong> latLongs;
        latLongs = polyline.getLatLongs();
        float max = distance.get(0).altitute;


        for (int i = 1; i < distance.size(); i++) {


            if (distance.get(i).altitute > max)
                max = distance.get(i).altitute;


            //latLongs.add(distance.get(i - 1).l);
            // Log.d("Lat Long : ",i+" "+distance.get(i).l.getLatitude() +" "+distance.get(i).l.getLongitude());
            Log.d("Lat Long: ", (i - 1) + " " + distance.get(i - 1).l.getLatitude() + " " + distance.get(i - 1).l.getLongitude());
            //Log.d("Waypoints passaloi: ", Coordinates.distFrom(distance.get(i - 1).l.getLatitude(), distance.get(i - 1).l.getLongitude(), distance.get(i).l.getLatitude(), distance.get(i).l.getLongitude()) + " ");
            if (Coordinates.distFrom(distance.get(i - 1).l.getLatitude(), distance.get(i - 1).l.getLongitude(), distance.get(i).l.getLatitude(), distance.get(i).l.getLongitude()) < 2&&Coordinates.distFrom(distance.get(i).l.getLatitude(), distance.get(i).l.getLongitude(), droneLocationLat, droneLocationLng)<5) {
                k = new Mark(distance.get(i).l, bitmap, 0, -bitmap.getHeight() / 2);

                latLongs.add(distance.get(i - 1).l);
                mapView.getLayerManager().getLayers().add(k);
                marks.add(k);
                //Log.d("Waypoints passaloi: ", Coordinates.distFrom(distance.get(i - 1).l.latitude, distance.get(i - 1).l.longitude, distance.get(i).l.latitude, distance.get(i).l.longitude) + " ");
                DJIWaypoint mWaypoint = new DJIWaypoint(distance.get(i).l.latitude, distance.get(i).l.longitude, altitude);
                //Add Waypoints to Waypoint arraylist;
                if (mWaypointMission != null) {

                    mWaypointMission.addWaypoint(mWaypoint);
                }
            } else break;


        }
        if (distance.size() > 0)
            latLongs.add(distance.get(distance.size() - 1).l);
        if (polyline != null) {
            mapView.getLayerManager().getLayers().add(polyline);
            marks.add(polyline);
        }
        altitude_w = new float[distance.size()];
        float sealevel = new Coordinates(MainActivity.this,null).sealevel_check(distance, droneLocationLat, droneLocationLng, altitude_w);
        altitude = max + sealevel + 3;


        for (int i = 0; i < altitude_w.length; i++)

            Log.e(TAG, "altitude: " + altitude_w[i]);

    }


    public String getCurrentCountry() {

// create class object
        GPSTracker gps = new GPSTracker(MainActivity.this);
        String countryname = null;
        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            setResultToToast("Your Location is - Lat: " + latitude + "\nLong: " + longitude);
            // \n is for new line
            countryname = gps.getAddress(latitude, longitude);
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        return countryname;

    }

    private void deletepPicslist(){
        File sourceDirectory = new File(STITCHING_SOURCE_IMAGES_DIRECTORY);
        File[] paths=sourceDirectory.listFiles();
        int delete=paths.length-num_photos;
        setResultToToast("photos to delete "+ delete);

        for (int i=paths.length-1;i>num_photos;i--) {

            if (!paths[i].isDirectory())
                paths[i].delete();

            showLOG("picture deleted");


        }


    }

    //get directory filelist
    protected String[] getDirectoryFilelist(String directory,int i) {


        String[] filelist;
        File sourceDirectory = new File(directory);
        int index = i;
        int folderCount = 0;
        //except folders
        for (File file : sourceDirectory.listFiles()) {
            if (file.isDirectory()) {
                folderCount++;
            }
        }
        filelist = new String[sourceDirectory.listFiles().length - folderCount];



        for (File file : sourceDirectory.listFiles()) {

            if ((index<i+4&&index<sourceDirectory.listFiles().length)){


                if (!file.isDirectory() && !(file.getPath().endsWith(".tmp"))) {
                    //  showLOG("getFilelist file:" + file.getPath());
                    filelist[index] = file.getPath();
                    index++;
                }
            }
            else break;

        }

        return filelist;
    }

    //get current datetime
    protected String getCurrentDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return df.format(c.getTime());
    }


    //init download progress dialog
    private void initDownloadProgressDialog()
    {
        mDownloadDialog = new ProgressDialog(MainActivity.this);
        mDownloadDialog.setTitle("Downloading photos");
        mDownloadDialog.setIcon(android.R.drawable.ic_dialog_info);
        mDownloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownloadDialog.setCanceledOnTouchOutside(true);
        mDownloadDialog.setCancelable(true);
    }

    //show download progress dialog
    private void showDownloadProgressDialog()
    {
        if(mDownloadDialog != null)
        {
            mDownloadDialog.show();
            mDownloadDialog.setProgress(0);

        }
    }

    //hide download progress dialog
    private void hideDownloadProgressDialog() {
        if (null != mDownloadDialog && mDownloadDialog.isShowing())
        {
            mDownloadDialog.dismiss();
        }
    }




    private  void initCodecCam(){

        mReceivedVideoDataCallBack = new DJICamera.CameraReceivedVideoDataCallback() {

            @Override
            public void onResult(byte[] videoBuffer, int size) {
                if(mCodecManager != null){
                    // Send the raw H264 video data to codec manager for decoding
                    mCodecManager.sendDataToDecoder(videoBuffer, size);
                }else {
                    Log.e(TAG, "mCodecManager is null");
                }
            }
        };

        DJICamera camera = FPVDemoApplication.getCameraInstance();

        if (camera != null) {

            camera.setDJICameraUpdatedSystemStateCallback(new DJICamera.CameraUpdatedSystemStateCallback()  {
                @Override
                public void onResult(CameraSystemState cameraSystemState) {
                    if (null != cameraSystemState) {

                        int recordTime = cameraSystemState.getCurrentVideoRecordingTimeInSeconds();
                        int minutes = (recordTime % 3600) / 60;
                        int seconds = recordTime % 60;

                        final String timeString = String.format("%02d:%02d", minutes, seconds);
                        final boolean isVideoRecording = cameraSystemState.isRecording();

                        MainActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                recordingTime.setText(timeString);

                                /*
                                 * Update recordingTime TextView visibility and mRecordBtn's check state
                                 */
                                if (isVideoRecording){
                                    recordingTime.setVisibility(View.VISIBLE);
                                }else
                                {
                                    recordingTime.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                }
            });

        }

    }
    class Stitch1 extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... arg0) {
            String STITCHING_RESULT_IMAGES_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/smartdrone/stitch";

            //  initStitchingImageDirectory(STITCHING_RESULT_IMAGES_DIRECTORY);
            //String result = STITCHING_RESULT_IMAGES_DIRECTORY + getCurrentDateTime() + "result.jpg";

            int index=0;
            File[] paths;
            File[] current;
            // do {
            String result = STITCHING_RESULT_IMAGES_DIRECTORY +"/"+getCurrentDateTime() + "result.jpg";
            String[] source = getDirectoryFilelist(STITCHING_SOURCE_IMAGES_DIRECTORY,index);

            // File sourceDirectory = new File(STITCHING_SOURCE_IMAGES_DIRECTORY);
            // paths=sourceDirectory.listFiles();
          /*  if (jnistitching(source, result, STITCH_IMAGE_SCALE) == 0) {
                Log.d("Stitch", "success stitch");
                index+=8;

                //setResultToToast("Stitching success");

            } else {

                // setResultToToast("Stitching error");
                Log.d("stitch failed", "failed");
                //   break;
                // handler.sendMessage(handler.obtainMessage(HANDLER_SET_STITCHING_BUTTON_TEXT,"Stitching error"));
            }
             handler.sendMessage(handler.obtainMessage(HANDLER_ENABLE_STITCHING_BUTTON,""));

                             sourceDirectory = new File(STITCHING_RESULT_IMAGES_DIRECTORY);
                            //current=sourceDirectory.listFiles();

                            if (index>=paths.length&& current.length>1){
                                index=0;
                                sourceDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/smartdrone/mission1");
                                current=sourceDirectory.listFiles();

                                sourceDirectory = new File(STITCHING_RESULT_IMAGES_DIRECTORY);
                                paths=sourceDirectory.listFiles();

                                STITCHING_RESULT_IMAGES_DIRECTORY=Environment.getExternalStorageDirectory().getPath() + "/smartdrone/missions/stitch"+current.length;
                                initStitchingImageDirectory(STITCHING_RESULT_IMAGES_DIRECTORY);


                            }*/


            //    }

            //   while (index<paths.length);
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(float[] result) {


            //delegate.processFinish(result);

            //opencvView.setImageDrawable(new BitmapDrawable(getResources(), result));


        }


    }

}

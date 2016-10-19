package googlemap.gsdemo.dji.com.gsdemo;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
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

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.GestureDetector;

import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.ToggleButton;



import com.caverock.androidsvg.IntegerParser;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;

import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.datastore.MapDataStore;

import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;

import org.mapsforge.core.model.LatLong;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import dji.midware.media.a;
import dji.sdk.Camera.DJICamera;
import dji.sdk.Camera.DJICameraSettingsDef;
import dji.sdk.Codec.DJICodecManager;
import dji.sdk.FlightController.DJIFlightController;
import dji.sdk.FlightController.DJIFlightControllerDataType;
import dji.sdk.FlightController.DJIFlightControllerDelegate;
import dji.sdk.MissionManager.DJIMission;
import dji.sdk.MissionManager.DJIMissionManager;
import dji.sdk.MissionManager.DJIWaypoint;
import dji.sdk.MissionManager.DJIWaypoint.DJIWaypointActionType;
import dji.sdk.MissionManager.DJIWaypointMission;
import dji.sdk.Products.DJIAircraft;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.base.DJIError;
import dji.sdk.base.DJIFlightControllerError;


import org.mapsforge.map.util.MapViewProjection;
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

public class MainActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, DJIMissionManager.MissionProgressStatusCallback, DJIBaseComponent.DJICompletionCallback, TextureView.SurfaceTextureListener, View.OnClickListener {




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
                break;
            }
        }
    };


    //Drone camera variables


    protected DJICamera.CameraReceivedVideoDataCallback mReceivedVideoDataCallBack = null;


    // public CameraDrone dc = new CameraDrone(MainActivity.this);


    // Codec for video live view
    protected DJICodecManager mCodecManager = null;
    protected static TextView mConnectStatusTextView,altitute,speed,speedy;

    protected TextureView mVideoSurface = null;
    private Button mCaptureBtn, mShootPhotoModeBtn, mRecordVideoModeBtn;
    private ToggleButton mRecordBtn;
    protected TextView recordingTime;


    //Main Activity variables
    protected static final String TAG = "GSDemoActivity";


    public Button add, clear, alti_stay;
    public static Button add_waypoints;
    private ImageButton locate;
    private Button swit, config, prepare, start, stop;
    private CheckBox photo;


    public static boolean isAdd = false, flag = true, photocheck = false, change = false, addpoint = false;
    private static final String MAP_FILE = "cyprus.map";
    public static MapView mapView;

    private double droneLocationLat = 35.469352, droneLocationLng = 33.0, ndroneLocationLat, ndroneLocationLng;

    //private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<Integer, Marker>();
    private  Layer droneMarker = null;

    protected static float altitude_w[];
    protected float altitude = 100.0f;

    private float mSpeed = 10.0f;

    public static int dm=0, corner_points = 0, type;
    protected static int newalti;
    public Coordinates points[];

    public static Coordinates[] corner = new Coordinates[2];

    public static Coordinates[] drone_move;
    private Mark m;

    public static List<Layer> marks;
    protected static DJIWaypointMission mWaypointMission;
    private DJIMissionManager mMissionManager;
    private DJIFlightController mFlightController;
    public static boolean tap;

    private DJIWaypointMission.DJIWaypointMissionFinishedAction mFinishedAction = DJIWaypointMission.DJIWaypointMissionFinishedAction.NoAction;
    private DJIWaypointMission.DJIWaypointMissionHeadingMode mHeadingMode = DJIWaypointMission.DJIWaypointMissionHeadingMode.Auto;

    public File mCascadeFile;
    CascadeClassifier mJavaDetector;
    public ImageView opencvView;

    public int minWidth, minHeight;
    public int imWidth = 800, imHeight = 600;

    @Override
    protected void onResume() {
        super.onResume();
        initFlightController();
        initMissionManager();
        initPreviewer();
        updateTitleBar();



        if (mVideoSurface == null) {
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


        if (((CheckBox) v).isChecked())
            photocheck = true;
        else
            photocheck = true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera_menu, menu);
        return true;
    }
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }
    private void initUI() {

/*

       final Button showMenu = (Button) findViewById(R.id.show_dropdown_menu);



        showMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu dropDownMenu = new PopupMenu(getApplicationContext(), showMenu);
                dropDownMenu.getMenuInflater().inflate(R.menu.camera_menu, dropDownMenu.getMenu());
                showMenu.setText("DropDown Menu");

                MenuItem item = dropDownMenu.getMenu().findItem(R.id.spinner);
                Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.Image_Resolution, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);



                dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch(menuItem.getItemId()){

                         // case  R.id.file:
                            //setResultToToast("0");
                           // break;


                        }


                      //  Toast.makeText(getApplicationContext(), "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                });
                dropDownMenu.show();
            }



        });

*/

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
        add_waypoints.setEnabled(false);



        alti_stay.setOnClickListener(this);
        locate.setOnClickListener(this);
        add.setOnClickListener(this);
        add_waypoints.setOnClickListener(this);
        clear.setOnClickListener(this);
        config.setOnClickListener(this);
        prepare.setOnClickListener(this);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        locate.setEnabled(false);
        mConnectStatusTextView = (TextView) findViewById(R.id.ConnectStatusTextViewCamera);
        // init mVideoSurface
        mVideoSurface = (TextureView) findViewById(R.id.video_previewer_surface);

       altitute=(TextView)findViewById(R.id.altitude);


       speed=(TextView)findViewById(R.id.speed);

         //speedx=(TextView)findViewById(R.id.speedx);

        speedy=(TextView)findViewById(R.id.speedy);



        opencvView = (ImageView) findViewById(R.id.ImageFeed);

        recordingTime = (TextView) findViewById(R.id.timer);
        mCaptureBtn = (Button) findViewById(R.id.btn_capture);
        mRecordBtn = (ToggleButton) findViewById(R.id.btn_record);
        mShootPhotoModeBtn = (Button) findViewById(R.id.btn_shoot_photo_mode);
        mRecordVideoModeBtn = (Button) findViewById(R.id.btn_record_video_mode);


        Spinner spinner = (Spinner) findViewById(R.id.camera);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Camera_mode, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                mVideoSurface.setVisibility(View.VISIBLE);
                opencvView.setVisibility(View.INVISIBLE);
                modec = false;
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
                switch (position) {

                    case 0:
                        mVideoSurface.setVisibility(View.VISIBLE);
                        opencvView.setVisibility(View.INVISIBLE);
                        modec = false;
                        break;
                    case 1:
                        minWidth = 40;
                        minHeight = 40;
                        //mVideoSurface.setVisibility(View.INVISIBLE);
                        opencvView.setVisibility(View.VISIBLE);
                        modec = true;

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

                        break;
                    case 2:
                        minWidth = 48;
                        minHeight = 96;
                        opencvView.setVisibility(View.VISIBLE);
                        modec = true;

                        //mVideoSurface.setVisibility(View.INVISIBLE);

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


                        break;
                    case 3:
                        minWidth = 50;
                        minHeight = 20;
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

                        break;
                }


                String item = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }
        });


       Spinner SpinImageRes = (Spinner) findViewById(R.id.image_resol);
        ArrayAdapter<CharSequence> adapterImageRes = ArrayAdapter.createFromResource(this,
                R.array.Image_Resolution, android.R.layout.simple_spinner_item);

        SpinImageRes.setAdapter(adapterImageRes);

       // SpinImageRes.setOnClickListener(this);

        SpinImageRes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                imHeight = 120;
                imWidth = 160;
            }

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position) {
                    case 0:
                        imHeight = 120;
                        imWidth = 160;
                        break;
                    case 1:
                        imHeight = 320;
                        imWidth = 240;
                        break;
                    case 2:
                        imHeight = 640;
                        imWidth = 480;
                        break;
                    case 3:
                        imHeight = 1024;
                        imWidth = 600;
                        break;
                }
                String item = parentView.getItemAtPosition(position).toString();

                // Showing selected spinner item
                Toast.makeText(parentView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }
        });

        if (null != mVideoSurface) {
            mVideoSurface.setSurfaceTextureListener(this);
        }

        mCaptureBtn.setOnClickListener(this);
        mRecordBtn.setOnClickListener(this);
        mShootPhotoModeBtn.setOnClickListener(this);
        mRecordVideoModeBtn.setOnClickListener(this);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // When the compile and target version is higher than 22, please request the
        // following permissions at runtime to ensure the
        // SDK work well.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.VIBRATE,
                            Manifest.permission.INTERNET, Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.WAKE_LOCK, Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SYSTEM_ALERT_WINDOW,
                            Manifest.permission.READ_PHONE_STATE,
                    }
                    , 1);
        }

        marks = new LinkedList<>();
        // m=new Mark(getApplicationContext());
        //final GestureDetector gestureDetector = new GestureDetector(this, new SingleTDetector());

        TapMap tm = new TapMap(getApplicationContext());

        TapMap.SingleTDetector s = tm.new SingleTDetector();
        final GestureDetector gestureDetector = new GestureDetector(this, s);


        AndroidGraphicFactory.createInstance(this.getApplication());
        // this.mapView = getMapView();
        // mapView = (MapView)findViewById(R.id.mapview);
        //this.mapView = new MapView(this);
        setContentView(R.layout.activity_main);

        this.mapView = (MapView) findViewById(R.id.mapview);


        this.mapView.setClickable(true);
        this.mapView.getMapScaleBar().setVisible(true);
        this.mapView.setBuiltInZoomControls(true);
        this.mapView.setZoomLevelMin((byte) 10);
        this.mapView.setZoomLevelMax((byte) 20);
        // create a tile cache of suitable size
        TileCache tileCache = AndroidUtil.createTileCache(this, "mapcache",
                this.mapView.getModel().displayModel.getTileSize(), 1f,
                this.mapView.getModel().frameBufferModel.getOverdrawFactor());

        // tile renderer layer using internal render theme
        MapDataStore mapDataStore = new MapFile(new File(Environment.getExternalStorageDirectory(), MAP_FILE));
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                this.mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

        // only once a layer is associated with a mapView the rendering starts
        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);
        this.mapView.setCenter(new LatLong(35.14448546, 33.40969473));


        this.mapView.setZoomLevel((byte) 12);

        initUI();


        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(DJIDemoApplication.FLAG_CONNECTION_CHANGE);
        registerReceiver(mReceiver, filter);


        // new CameraDrone(getApplicationContext()).intCamera();
        //Drone camera
        // The callback for receiving the raw H264 video data for camera live view
        mReceivedVideoDataCallBack = new DJICamera.CameraReceivedVideoDataCallback() {

            @Override
            public void onResult(byte[] videoBuffer, int size) {
                if (mCodecManager != null) {
                    // Send the raw H264 video data to codec manager for decoding
                    mCodecManager.sendDataToDecoder(videoBuffer, size);
                } else {
                    Log.e(TAG, "mCodecManager is null");
                }
            }
        };


        DJICamera camera = FPVDemoApplication.getCameraInstance();

        if (camera != null) {

            camera.setDJICameraUpdatedSystemStateCallback(new DJICamera.CameraUpdatedSystemStateCallback() {
                @Override
                public void onResult(DJICamera.CameraSystemState cameraSystemState) {
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
                                if (isVideoRecording) {
                                    recordingTime.setVisibility(View.VISIBLE);
                                } else {
                                    recordingTime.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                }
            });

        }
    }


    protected BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
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
        DJIBaseProduct product = DJIDemoApplication.getProductInstance();

        if (product == null || !product.isConnected()) {
            setResultToToast("Disconnected drone");
            mMissionManager = null;
            return;
        } else {

            setResultToToast("Product connected");
            mMissionManager = product.getMissionManager();
            mMissionManager.setMissionProgressStatusCallback(this);
            mMissionManager.setMissionExecutionFinishedCallback(this);
        }

        mWaypointMission = new DJIWaypointMission();
        mWaypointMission.flightPathMode=DJIWaypointMission.DJIWaypointMissionFlightPathMode.Normal;
    }


    private void initFlightController() {
        DJIBaseProduct product = DJIDemoApplication.getProductInstance();
        if (product != null && product.isConnected()) {
            if (product instanceof DJIAircraft) {
                mFlightController = ((DJIAircraft) product).getFlightController();
            }
        }



        if (mFlightController != null) {
            locate.setEnabled(true);
            mFlightController.setUpdateSystemStateCallback(new DJIFlightControllerDelegate.FlightControllerUpdateSystemStateCallback() {
                @Override
                public void onResult(DJIFlightControllerDataType.DJIFlightControllerCurrentState state) {

if (photocheck&& dm<drone_move.length)
                    if (new LatLong(droneLocationLat,droneLocationLng).equals(new LatLong(drone_move[dm].lat,drone_move[dm].lon))){
                        new CameraDrone(MainActivity.this).captureAction();
                        dm++;
                    }


                    final TextView speedx = (TextView) findViewById(R.id.speedx);



                    final float uy=state.getVelocityY();
                    final double u=Math.sqrt(Math.pow(state.getVelocityX(),2)+Math.pow(state.getVelocityY(),2));
                    final double ux=state.getVelocityX();
                    final double alt=state.getUltrasonicHeight();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            altitute.setText("height: "+alt+" m");
                            speed.setText("u: "+u+"m/s");
                            speedx.setText("Ux: "+ux+" m/s");
                            speedy.setText("Uy: "+Float.toString(uy)+" m/s");
                        }
                    });





                    droneLocationLat = state.getAircraftLocation().getLatitude();
                    droneLocationLng = state.getAircraftLocation().getLongitude();


                    updateDroneLocation();//

                    // }




                }
            });
        }
    }

    /**
     * DJIMissionManager Delegate Methods
     */
    @Override
    public void missionProgressStatus(DJIMission.DJIMissionProgressStatus progressStatus) {

    }

    /**
     * DJIMissionManager Delegate Methods
     */
    @Override
    public void onResult(DJIError error) {
        setResultToToast("Execution finished: " + (error == null ? "Success drone!" : error.getDescription()));
    }


    public void addMarks() {


        for (int i = 0; i < altitude_w.length; i++) {
            altitude_w[i] = 0;
        }


        final LatLong LatLong = new LatLong(drone_move[0].lat, drone_move[0].lon);

        Drawable drawable = getResources().getDrawable(R.drawable.red_mark);
        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.incrementRefCount();


        Mark k = new Mark(LatLong, bitmap, 0, -bitmap.getHeight() / 2);
        DJIWaypoint mWaypoint = new DJIWaypoint(LatLong.latitude, LatLong.longitude, altitude);


        boolean flag4 = false;
        Run r;
        k.setOnTabAction(r = new Run(0, getApplicationContext()));

        marks.add(k);

        mapView.getLayerManager().getLayers().add(k);


        //Add Waypoints to Waypoint arraylist;
        if (mWaypointMission != null) {
            mWaypointMission.addWaypoint(mWaypoint);
        }

        for (int i = 1; i < drone_move.length; i++) {
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
                    marks.add(polyline);

                } else {
                    List<LatLong> latLongs = polyline.getLatLongs();
                    latLongs.add(new LatLong(drone_move[i].lat, drone_move[i].lon));
                    latLongs.add(new LatLong(drone_move[i + 1].lat, drone_move[i + 1].lon));
                }
                mapView.getLayerManager().getLayers().add(polyline);
                marks.add(polyline);
            }


            final LatLong LatLong2 = new LatLong(drone_move[i].lat, drone_move[i].lon);

            k = new Mark(LatLong2, bitmap, 0, -bitmap.getHeight() / 2);
            mapView.getLayerManager().getLayers().add(k);

            k.setOnTabAction(r = new Run(i, getApplicationContext()));


            mWaypoint = new DJIWaypoint(LatLong2.latitude, LatLong2.longitude, altitude);
            //Add Waypoints to Waypoint arraylist;
            if (mWaypointMission != null) {
                mWaypointMission.addWaypoint(mWaypoint);
            }

            marks.add(k);
        }
        // mapView.getLayerManager().getLayers().addAll(marks);

    }


    public static boolean checkGpsCoordination(double latitude, double longitude) {
        return (latitude > -90 && latitude < 90 && longitude > -180 && longitude < 180) && (latitude != 0f && longitude != 0f);
    }


    // Update the drone location based on states from MCU.
   public void updateDroneLocation() {
      //
        //this.mapView.setZoomLevel((byte) 12);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                speed.setText("m/s");
                if (droneMarker != null) {
                    mapView.getLayerManager().getLayers().remove(droneMarker);
                }

                if (checkGpsCoordination(droneLocationLat, droneLocationLng)) {
                    final LatLong pos = new LatLong(droneLocationLat, droneLocationLng);

                    Drawable drawable = getResources().getDrawable(R.drawable.aircraft);
                    Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
                    bitmap.incrementRefCount();


                   droneMarker = new Mark(pos, bitmap, 0, -bitmap.getHeight() / 2);

                    mapView.getLayerManager().getLayers().add(droneMarker);
                    marks.add(droneMarker);
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

    public boolean modec = false;


    boolean flag1 = true, a1 = false, h1 = false;

    @Override
    public void onClick(View v) {


        switch (v.getId()) {


            case R.id.alt_stay: {

                if (change == false) {
                    window_change_altitute();
                    change = true;
                    alti_stay.setText("Finish");

                } else {

                    change = false;
                    alti_stay.setText("Points settings");

                }

                break;
            }

            case R.id.locate: {



                if (checkGpsCoordination(droneLocationLat, droneLocationLng)) {
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
                                type = 1;
                                enableDisableAdd();
                            } else if (checkedId == R.id.grid) {

                                type = 2;
                            } else if (checkedId == R.id.square) {
                                changeflag();
                                type = 2;
                            }
                            else if (checkedId == R.id.fromefile) {
                                readfile();
                            }

                        }

                    });
                    new AlertDialog.Builder(this)
                            .setTitle("")
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
                } else
                    enableDisableAdd();

                break;
            }

            case R.id.add_w: {


                init_waypoints_settings();


                break;
            }

            case R.id.clear: {
                add_waypoints.setEnabled(false);
                corner_points = 0;


                for (int i = 0; i < marks.size(); i++)
                    mapView.getLayerManager().getLayers().remove(marks.get(i));
                if (mWaypointMission != null) {
                    mWaypointMission.removeAllWaypoints(); // Remove all the waypoints added to the task
                }
                break;
            }


            case R.id.config: {
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
                new CameraDrone(MainActivity.this).captureAction();
                break;
            }
            case R.id.btn_shoot_photo_mode: {
                new CameraDrone(MainActivity.this).switchCameraMode(DJICameraSettingsDef.CameraMode.ShootPhoto);
                break;
            }
            case R.id.btn_record_video_mode: {
                new CameraDrone(MainActivity.this).switchCameraMode(DJICameraSettingsDef.CameraMode.RecordVideo);
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
            if (!marks.isEmpty())
                altitude_w=new float[marks.size()];
            isAdd = false;
            add.setText("Add");
        }
    }

    private void showSettingDialog() {
        LinearLayout wayPointSettings = (LinearLayout) getLayoutInflater().inflate(R.layout.dialog_waypointsetting, null);

        final TextView wpAltitude_TV = (TextView) wayPointSettings.findViewById(R.id.altitude);
        RadioGroup speed_RG = (RadioGroup) wayPointSettings.findViewById(R.id.speed);
        RadioGroup actionAfterFinished_RG = (RadioGroup) wayPointSettings.findViewById(R.id.actionAfterFinished);
        RadioGroup heading_RG = (RadioGroup) wayPointSettings.findViewById(R.id.heading);

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

        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(wayPointSettings)
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String altitudeString = wpAltitude_TV.getText().toString();
                        altitude = Integer.parseInt(nulltoIntegerDefalt(altitudeString));
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

        if (mWaypointMission != null) {
            mWaypointMission.finishedAction = mFinishedAction;
            mWaypointMission.headingMode = mHeadingMode;
            mWaypointMission.autoFlightSpeed = mSpeed;

            DJIWaypoint.DJIWaypointAction mstay = new DJIWaypoint.DJIWaypointAction(DJIWaypointActionType.Stay, 0);
            DJIWaypoint.DJIWaypointAction tphoto = new DJIWaypoint.DJIWaypointAction(DJIWaypointActionType.StartTakePhoto, 1);


setResultToToast("size waypoints "+mWaypointMission.waypointsList.size());
            if (mWaypointMission.waypointsList.size() > 0) {
                for (int i = 0; i < mWaypointMission.waypointsList.size(); i++) {

                    //if (i<altitude_w.length)
                    if (altitude_w[i] > 0) {

                        mWaypointMission.getWaypointAtIndex(i).altitude = altitude_w[i];
                    } else
                        mWaypointMission.getWaypointAtIndex(i).altitude = altitude;
                    //mWaypointMission.getWaypointAtIndex(i).altitude = altitude_w[i];
                    mWaypointMission.getWaypointAtIndex(i).addAction(mstay);
                    if (photocheck) {

                        mWaypointMission.getWaypointAtIndex(i).addAction(tphoto);
                        photocheck = false;

                    }


                }
            }
        }
    }

    private void prepareWayPointMission() {

        if (mMissionManager != null && mWaypointMission != null) {

            DJIMission.DJIMissionProgressHandler progressHandler = new DJIMission.DJIMissionProgressHandler() {
                @Override
                public void onProgress(DJIMission.DJIProgressType type, float progress) {
                }
            };

            mMissionManager.prepareMission(mWaypointMission, progressHandler, new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    setResultToToast(error == null ? "Mission Prepare Successfully" : error.getDescription());
                }
            });
        }

    }

    private void startWaypointMission() {

        if (mMissionManager != null) {

            mMissionManager.startMissionExecution(new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {
                    setResultToToast("Mission Start: " + (error == null ? "Successfully" : error.getDescription()));
                }
            });

        }
    }

    private void stopWaypointMission() {

        if (mMissionManager != null) {
            mMissionManager.stopMissionExecution(new DJIBaseComponent.DJICompletionCallback() {

                @Override
                public void onResult(DJIError error) {
                    setResultToToast("Mission Stop: " + (error == null ? "Successfully" : error.getDescription()));
                }
            });

            if (mWaypointMission != null) {
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
                mConnectStatusTextView.setText(FPVDemoApplication.getProductInstance().getModel() + " Connected");
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
            mConnectStatusTextView.setText("Disconnected");
        }
    }

    protected void onProductChange() {
        initPreviewer();
    }

    private void initPreviewer() {

        DJIBaseProduct product = FPVDemoApplication.getProductInstance();

        if (product == null || !product.isConnected()) {
            // showToast(getString(R.string.disconnected));
        } else {
            if (null != mVideoSurface) {
                mVideoSurface.setSurfaceTextureListener(this);
            }
            if (!product.getModel().equals(DJIBaseProduct.Model.UnknownAircraft)) {
                DJICamera camera = product.getCamera();
                if (camera != null) {
                    // Set the callback
                    camera.setDJICameraReceivedVideoDataCallback(mReceivedVideoDataCallBack);
                }
            }
        }
    }

    private void uninitPreviewer() {
        DJICamera camera = FPVDemoApplication.getCameraInstance();
        if (camera != null) {
            // Reset the callback
            FPVDemoApplication.getCameraInstance().setDJICameraReceivedVideoDataCallback(null);
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.v(TAG, "onSurfaceTextureAvailable");
        DJICamera camera = FPVDemoApplication.getCameraInstance();
        if (mCodecManager == null && surface != null && camera != null) {
            //Normal init for the surface
            mCodecManager = new DJICodecManager(this, surface, width, height);
            Log.v(TAG, "Initialized CodecManager");
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
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

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        if (modec) {

            final android.graphics.Bitmap image = mVideoSurface.getBitmap();
            //Bitmap bm = Bitmap.createScaledBitmap(image,100, 100, true);
            android.graphics.Bitmap bm = image.copy(image.getConfig(), true);

            if (bm == null)
                return;
            Mat rgba = new Mat();
            Mat gray = new Mat();
            Mat resizedImg = Mat.zeros(new Size(imWidth, imHeight), rgba.type());

            Utils.bitmapToMat(bm, rgba);

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
                // Each rectangle in the faces array is a face
                // Draw a rectangle around each face
                Rect[] objArray = objects.toArray();
                for (int i = 0; i < objArray.length; i++)
                    Core.rectangle(resizedImg, objArray[i].tl(), objArray[i].br(), new Scalar(0, 255, 0, 255), 3);

            }

            Imgproc.resize(resizedImg, rgba, rgba.size());
            Utils.matToBitmap(rgba, bm);

            Canvas imCanvas = new Canvas(bm);
            imCanvas.drawBitmap(bm, 0, 0, null);
            opencvView.setImageDrawable(new BitmapDrawable(getResources(), bm));


            //Toast.makeText(MainActivity.this,String.format("%d - %d - %d - %d",image.getPixel(100,100)&0x000000FF,bmap.getPixel(100,100)&0x000000FF,image.getHeight(),image.getWidth()),Toast.LENGTH_SHORT).show();

        }
        // opencv_detection();

    }

    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void init_waypoints_settings() {
        LinearLayout wayPointSettings = (LinearLayout) getLayoutInflater().inflate(R.layout.labyrinth_settings, null);
        final double[] lon = new double[2];
        final double[] lan = new double[2];

        final EditText a = (EditText) wayPointSettings.findViewById(R.id.ang);
        final EditText h = (EditText) wayPointSettings.findViewById(R.id.h);
        final TextView d = (TextView) wayPointSettings.findViewById(R.id.distance);
a1=false;
        h1=false;

        a.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                //d.clearComposingText();
                //Your query to fetch Data
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // d.clearComposingText();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {

                    a1 = true;
                    if (h1) {
                        d.clearComposingText();
                       long al=Integer.parseInt(h.getText().toString().trim());
                        long f=Integer.parseInt(a.getText().toString().trim());
                        Log.d("Distance "," "+al+" "+f);
                        long r = Math.abs(Math.round(2.0 * al * Math.tan(Math.toRadians(f / 2.0))));

                        Log.d("Distance "," "+r);

                        d.setText(String.valueOf(r));


                    }


                } else {
                    d.clearComposingText();
                   // d.setText(" ");
                    a1 = false;
                }

            }
        });
        h.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // d.clearComposingText();
                //Your query to fetch Data
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
               // d.clearComposingText();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    h1 = true;
                    d.clearComposingText();

                    if (a1) {
                        long al=Integer.parseInt(h.getText().toString().trim());
                        long f=Integer.parseInt(a.getText().toString().trim());

                        long r = Math.abs(Math.round(2.0 * al * Math.tan(Math.toRadians(f / 2.0))));
                        Log.d("Distance "," "+r);
                        d.setText(String.valueOf(r));
                    }


                } else {
                    h1 = false;
                    d.clearComposingText();
                   // d.setText(" ");
                    //
                }
                //Your query to fetch Data

            }
        });

        if (h1&&a1){

        }

        if (corner[0].lon < corner[1].lon) {
            lon[0] = corner[0].lon;

            lan[0] = corner[0].lat;
            lon[1] = corner[1].lon;
            lan[1] = corner[1].lat;
        } else {
            lon[1] = corner[0].lon;

            lan[1] = corner[0].lat;
            lon[0] = corner[1].lon;
            lan[0] = corner[1].lat;
        }


        new AlertDialog.Builder(this)
                .setTitle("")
                .setView(wayPointSettings)
                .setPositiveButton("Finish", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        mid = 0;

                        dr = 0;
                        if (!(a.getText().toString().isEmpty() && a.getText().toString().isEmpty())) {
                            if (flag)
                                drone_move = Coordinates.create_grid(lon, lan, Integer.parseInt(a.getText().toString()), Integer.parseInt(h.getText().toString()));
                            else
                                drone_move = Coordinates.create_square(lon, lan, Integer.parseInt(a.getText().toString()), Integer.parseInt(h.getText().toString()));
                            if (drone_move != null) {
                                altitude_w = new float[drone_move.length];
                                add_waypoints.setEnabled(false);
                                addMarks();
                            } else {
                                //Toast.makeText(c,"Increase points distance ",Toast.LENGTH_SHORT).show();
                                setResultToToast("Increase points distance ");

                            }
                        }

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

public void readfile() {

List<Coordinates> distance=new LinkedList<Coordinates>();
    String line = null;
    Drawable drawable = getResources().getDrawable(R.drawable.red_mark);
    Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
    bitmap.incrementRefCount();

    try {

        File f=new File(Environment.getExternalStorageDirectory(),"passaloi");
       // FileInputStream fileInputStream = new FileInputStream (f);

        Scanner scan = new Scanner(f);


        //InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
      //  BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

      scan.nextLine();


        while ( (scan.hasNext()) )
        {
           scan.next();
           float alt= scan.nextInt();
           double lat=scan.nextDouble();
            double lon=scan.nextDouble();



            //Mark k = new Mark(new LatLong(lon,lat), bitmap, 0, -bitmap.getHeight() / 2);
            if (checkGpsCoordination(droneLocationLat,droneLocationLng))

                distance.add(new Coordinates(new LatLong(lon,lat),Coordinates.distFrom(droneLocationLat,droneLocationLng, lon, lat),alt));




        }
        //fileInputStream.close();



    }
    catch(FileNotFoundException ex) {
        Log.d(TAG, ex.getMessage());
    }
    catch(IOException ex) {
        Log.d(TAG, ex.getMessage());
    }

    //drawable = getResources().getDrawable(R.drawable.aircraft);
  // bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
    //bitmap.incrementRefCount();


    Collections.sort(distance, new Comparator<Coordinates>() {
        @Override
        public int compare(Coordinates o1, Coordinates o2) {
            if (o1.distance>o2.distance)
                return 1;
            else if (o1.distance<o2.distance)
                return -1;
            return 0;
        }
    });
   // mapView.getLayerManager().getLayers().add(k);
    //distance=bubbleSort(distance);


        //Log.d("Distance "," "+distance.get(i).distance);
    Mark k;
    if (distance.get(0).distance<2){
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


float max=distance.get(0).altitute;
    for (int i=1;i<distance.size();i++){

        if (distance.get(i).altitute>max)
        altitude=max+3;

        if (Coordinates.distFrom(distance.get(i-1).l.latitude,distance.get(i-1).l.longitude,distance.get(i).l.latitude,distance.get(i).l.longitude)<2){
             k = new Mark(distance.get(i).l, bitmap, 0, -bitmap.getHeight() / 2);
            mapView.getLayerManager().getLayers().add(k);
            marks.add(k);
            DJIWaypoint mWaypoint = new DJIWaypoint(distance.get(i).l.latitude, distance.get(i).l.longitude, altitude);
            //Add Waypoints to Waypoint arraylist;
            if (mWaypointMission != null) {

                mWaypointMission.addWaypoint(mWaypoint);
            }
        }





    }





}


}

package full.view;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import dji.common.camera.CameraSystemState;
import dji.common.camera.DJICameraSettingsDef;
import dji.common.error.DJIError;
import dji.common.error.DJISDKError;
import dji.common.util.DJICommonCallbacks;
import dji.sdk.camera.DJICamera;
import dji.sdk.camera.DJICamera.CameraReceivedVideoDataCallback;


import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.codec.DJICodecManager;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;
import full.view.FPVDemoApplication;
import googlemap.gsdemo.dji.com.gsdemo.MainActivity;
import image.edit.opencv.ImageEdit;

/**
 * Created by George on 8/8/2016.
 */
public class CameraDrone extends MainActivity {


    private Context context;
    String STITCHING_SOURCE_IMAGES_DIRECTORY = Environment.getExternalStorageDirectory().getPath()+"/smart drone/photos";

    /**
     * ATTENTION: This was auto-gedji.sdk.camera.DJIPlaybackManagernerated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public CameraDrone(Context c) {
        this.context = c;
    }

    /*public void intCamera() {

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

                        CameraDrone.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                recordingTime.setText(timeString);

                                *//*
                                 * Update recordingTime TextView visibility and mRecordBtn's check state
                                 *//*
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


    }*/


    public void switchCameraMode(DJICameraSettingsDef.CameraMode cameraMode){

        DJICamera camera = FPVDemoApplication.getCameraInstance();
        if (camera != null) {
            camera.setCameraMode(cameraMode, new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {

                    if (error == null) {

                        Toast.makeText(context,"Switch Camera Mode Succeeded", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(context, error.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
//private static int flag1=0;
    // Method for taking photo
    public  int captureAction() {



        DJICameraSettingsDef.CameraMode cameraMode = DJICameraSettingsDef.CameraMode.ShootPhoto;

         DJICamera camera = FPVDemoApplication.getProductInstance().getCamera();

        if (camera != null) {

                DJICameraSettingsDef.CameraShootPhotoMode photoMode = DJICameraSettingsDef.CameraShootPhotoMode.Single; // Set the camera capture mode as Single mode

                camera.startShootPhoto(photoMode, new DJICommonCallbacks.DJICompletionCallback() {

                    @Override
                    public void onResult(DJIError error) {
                        if (error == null) {
                            flag1=1;
                            num_photos++;

                          //  Toast.makeText(context, "take photo: success " +flag1, Toast.LENGTH_SHORT).show();//}
                        } else {
                            flag1=0;
                          //  Toast.makeText(context, error.getDescription(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }); // Execute the startShootPhoto API


        }
        Toast.makeText(context, "take photo: success " +flag1 , Toast.LENGTH_SHORT).show();
return flag1;
    }






    // Method for starting recording
    public void startRecord() {


        DJICameraSettingsDef.CameraMode cameraMode = DJICameraSettingsDef.CameraMode.RecordVideo;
        final DJICamera camera = FPVDemoApplication.getCameraInstance();
        if (camera != null) {
            camera.startRecordVideo(new DJICommonCallbacks.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error)
                {
                    if (error == null) {

                        Toast.makeText(context, "Record video: success", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(context, error.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
            }); // Execute the startRecordVideo API
        }
    }

    // Method for stopping recording
    public void stopRecord() {

        DJICamera camera = FPVDemoApplication.getCameraInstance();
        if (camera != null) {
            camera.stopRecordVideo(new DJICommonCallbacks.DJICompletionCallback() {

                @Override
                public void onResult(DJIError error)
                {
                    if(error == null) {
                        Toast.makeText(context,"Stop recording: success", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(context, error.getDescription(), Toast.LENGTH_SHORT).show();
                    }
                }
            }); // Execute the stopRecordVideo API
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CameraDrone Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://googlemap.gsdemo.dji.com.gsdemo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CameraDrone Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://googlemap.gsdemo.dji.com.gsdemo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


  /*  public  void dowloadfiles(){


        final DJICamera camera = FPVDemoApplication.getCameraInstance();

    File downloadPath = new File(STITCHING_SOURCE_IMAGES_DIRECTORY);
    camera.getPlayback().downloadSelectedFiles(downloadPath, new DJIPlaybackManager.CameraFileDownloadCallback() {
        @Override
        public void onStart() {

        }

        @Override
        public void onEnd() {

        }

        @Override
        public void onError(Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProgressUpdate(int i) {

        }
    });

}*/







       // File downloadPath = new File(STITCHING_SOURCE_IMAGES_DIRECTORY);
     //   camera.getPlayback().downloadSelectedFiles();

    public static class DJIDemoApplication extends Application {

        private static final String TAG = DJIDemoApplication.class.getName();

        public static final String FLAG_CONNECTION_CHANGE = "dji_sdk_connection_change";

        private static DJIBaseProduct mProduct;

        private Handler mHandler;

        public static synchronized DJIBaseProduct getProductInstance() {
            if (null == mProduct) {
                mProduct = DJISDKManager.getInstance().getDJIProduct();
            }
            return mProduct;
        }

        @Override
        public void onCreate() {
            super.onCreate();

            mHandler = new Handler(Looper.getMainLooper());
            DJISDKManager.getInstance().initSDKManager(this, mDJISDKManagerCallback);

        }

        protected void attachBaseContext(Context base){
            super.attachBaseContext(base);
            MultiDex.install(this);
        }

        private DJISDKManager.DJISDKManagerCallback mDJISDKManagerCallback = new DJISDKManager.DJISDKManagerCallback() {

            @Override
            public void onGetRegisteredResult(DJIError error) {

                Log.d(TAG, error == null ? "success" : error.getDescription());
                if(error == DJISDKError.REGISTRATION_SUCCESS) {
                    DJISDKManager.getInstance().startConnectionToProduct();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.d(TAG, "Register success");

                } else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "register sdk fails, check network is available", Toast.LENGTH_LONG).show();
                        }
                    });

                    Log.d(TAG, "Register failed");

                }
                Log.e(TAG, error == null ? "success" : error.getDescription());
            }

            @Override
            public void onProductChanged(DJIBaseProduct oldProduct, DJIBaseProduct newProduct) {

                mProduct = newProduct;
                if(mProduct != null) {
                    mProduct.setDJIBaseProductListener(mDJIBaseProductListener);
                }

                notifyStatusChange();
            }
        };

        private DJIBaseProduct.DJIBaseProductListener mDJIBaseProductListener = new DJIBaseProduct.DJIBaseProductListener() {

            @Override
            public void onComponentChange(DJIBaseProduct.DJIComponentKey key, DJIBaseComponent oldComponent, DJIBaseComponent newComponent) {
                if(newComponent != null) {
                    newComponent.setDJIComponentListener(mDJIComponentListener);
                }
                notifyStatusChange();
            }

            @Override
            public void onProductConnectivityChanged(boolean isConnected) {
                notifyStatusChange();
            }

        };

        private DJIBaseComponent.DJIComponentListener mDJIComponentListener = new DJIBaseComponent.DJIComponentListener() {

            @Override
            public void onComponentConnectivityChanged(boolean isConnected) {
                notifyStatusChange();
            }

        };

        private void notifyStatusChange() {
            mHandler.removeCallbacks(updateRunnable);
            mHandler.postDelayed(updateRunnable, 500);
        }

        private Runnable updateRunnable = new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(FLAG_CONNECTION_CHANGE);
                sendBroadcast(intent);
            }
        };




    }

    }




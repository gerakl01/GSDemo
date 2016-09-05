package googlemap.gsdemo.dji.com.gsdemo;

import android.widget.Toast;

import dji.sdk.Camera.DJICamera;
import dji.sdk.Camera.DJICameraSettingsDef;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIError;

/**
 * Created by George on 8/8/2016.
 */
public class CameraDrone {
   public static void switchCameraMode(DJICameraSettingsDef.CameraMode cameraMode){
       final MainActivity ma=new MainActivity();
        DJICamera camera = FPVDemoApplication.getCameraInstance();
        if (camera != null) {
            camera.setCameraMode(cameraMode, new DJIBaseComponent.DJICompletionCallback() {
                @Override
                public void onResult(DJIError error) {

                    if (error == null) {
                        ma.showToast("Switch Camera Mode Succeeded");
                    } else {
                        ma.showToast(error.getDescription());
                    }
                }
            });
        }

    }

    // Method for taking photo
   public static void captureAction(){
        final MainActivity ma=new MainActivity();

        DJICameraSettingsDef.CameraMode cameraMode = DJICameraSettingsDef.CameraMode.ShootPhoto;

        final DJICamera camera = FPVDemoApplication.getCameraInstance();
        if (camera != null) {

            DJICameraSettingsDef.CameraShootPhotoMode photoMode = DJICameraSettingsDef.CameraShootPhotoMode.Single; // Set the camera capture mode as Single mode
            camera.startShootPhoto(photoMode, new DJIBaseComponent.DJICompletionCallback() {

                @Override
                public void onResult(DJIError error) {
                    if (error == null) {
                        ma.showToast("take photo: success");
                    } else {
                       ma. showToast(error.getDescription());
                    }
                }

            }); // Execute the startShootPhoto API
        }
    }

    // Method for starting recording
    public static void startRecord(){
        final MainActivity ma=new MainActivity();

        DJICameraSettingsDef.CameraMode cameraMode = DJICameraSettingsDef.CameraMode.RecordVideo;
        final DJICamera camera = FPVDemoApplication.getCameraInstance();
        if (camera != null) {
            camera.startRecordVideo(new DJIBaseComponent.DJICompletionCallback(){
                @Override
                public void onResult(DJIError error)
                {
                    if (error == null) {
                        ma.showToast("Record video: success");
                    }else {
                        ma.showToast(error.getDescription());
                    }
                }
            }); // Execute the startRecordVideo API
        }
    }

    // Method for stopping recording
    public static void stopRecord(){
final MainActivity ma=new MainActivity();
        DJICamera camera = FPVDemoApplication.getCameraInstance();
        if (camera != null) {
            camera.stopRecordVideo(new DJIBaseComponent.DJICompletionCallback(){

                @Override
                public void onResult(DJIError error)
                {
                    if(error == null) {
                        ma.showToast("Stop recording: success");
                    }else {
                        ma.showToast(error.getDescription());
                    }
                }
            }); // Execute the stopRecordVideo API
        }

    }




}

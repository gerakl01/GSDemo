package image.edit.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import googlemap.gsdemo.dji.com.gsdemo.MainActivity;


/**
 * Created by george on 16/01/2017.
 */

public class Stitch extends MainActivity {
    public void loadOneImage(String directory,String path)
    {

        String Filelist=getOnFile(directory);
        Bitmap b;

        int start = Filelist.indexOf("DJI");
        //String path=Filelist.substring(0,start-1);

        try {

            String pic = Filelist.substring(start);
            Log.d("dji pic", pic);

            File f = new File(path, pic);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            //b[0] = BitmapFactory.decodeStream(new FileInputStream(f));
            saveToInternalStorage(b,pic);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


      //// Bitmap result= mergeMultiple(b);
       // saveToInternalStorage(result,"merge.JPG");
        // return result;
    }


    public void loadImageFromStorage()
    {

        String Filelist[]=getDirectoryFilelist(Environment.getExternalStorageDirectory().getPath() + "/smartdrone/"+"photos");
        Bitmap[] b=new Bitmap[Filelist.length];
        Log.d("length", Filelist.length+" ");
        int start = Filelist[0].indexOf("DJI");
        String path=Filelist[0].substring(0,start-1);
        for (int i=0;i< Filelist.length;i++) {
            try {

                String pic = Filelist[i].substring(start);
                Log.d("dji pic", pic);

                File f = new File(path, pic);
              b[i] = BitmapFactory.decodeStream(new FileInputStream(f));
                //b[0] = BitmapFactory.decodeStream(new FileInputStream(f));
                saveToInternalStorage(b[i],pic);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

       //Bitmap result= mergeMultiple(b);
        //saveToInternalStorage(result,"merge.JPG");
       // return result;
    }
    //get directory filelist
    protected String getOnFile(String directory) {


        String[] filelist;
        File sourceDirectory = new File(directory);








File[] f=sourceDirectory.listFiles();



             String result=f[sourceDirectory.listFiles().length].getPath();






        return result;
    }
    //get directory filelist
    protected String[] getDirectoryFilelist(String directory) {


        String[] filelist;
        File sourceDirectory = new File(directory);

        int folderCount = 0;
        //except folders
        for (File file : sourceDirectory.listFiles()) {
            if (file.isDirectory()) {
                folderCount++;
            }
        }
        int index=0;
        filelist = new String[sourceDirectory.listFiles().length - folderCount];



        for (File file : sourceDirectory.listFiles()) {




            if (!file.isDirectory() && !(file.getPath().endsWith(".tmp"))) {
                Log.d("getFilelist file:" , file.getPath());
                filelist[index] = file.getPath();
                index++;
            }


        }

        return filelist;
    }
    //get directory filelist
    protected String[] getDirectoryFilelist1(String directory,int index) {


        String[] filelist;
        File sourceDirectory = new File(directory);

        int folderCount = 0;
        //except folders
        for (File file : sourceDirectory.listFiles()) {
            if (file.isDirectory()) {
                folderCount++;
            }
        }
        int length=index+1;
        filelist = new String[2];

int j=0;
File[] f=sourceDirectory.listFiles();
        for (int i=index;i<=length;i++) {




            if (!f[i].isDirectory() && !(f[i].getPath().endsWith(".tmp"))) {
                 Log.d("getFilelist file:" , f[i].getPath());
                filelist[j] = f[i].getPath();
j++;
            }


        }

        return filelist;
    }
    private String saveToInternalStorage(Bitmap bitmapImage,String pic){

        File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/smartdrone/compress");
        initDirectory(Environment.getExternalStorageDirectory().getPath() + "/smartdrone/compress");
        // Create imageDir

        File mypath=new File(directory,pic);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }
    //init stitching image folder
    public int initDirectory(String mkdir)
    {
        //check exists,if not,create
        File sourceDirectory = new File(mkdir);
        if(!sourceDirectory.exists())
        {
            sourceDirectory.mkdirs();
        }
        if (sourceDirectory.listFiles()!=null)
return sourceDirectory.listFiles().length;
        else
            return 0;
    }
    private Bitmap mergeMultiple(Bitmap[] parts){

        Bitmap result = Bitmap.createBitmap(parts[0].getWidth() * 2, parts[0].getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        for (int i = 0; i < parts.length; i++) {
            canvas.drawBitmap(parts[i], parts[i].getWidth() * (i % 2), parts[i].getHeight() * (i / 2), paint);
        }
        return result;
    }
    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo=Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }






}
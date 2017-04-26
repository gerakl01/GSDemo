package image.edit.opencv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by george on 02/02/2017.
 */

public class ImageEdit {

    Context c;
    public ImageEdit(){

        //this.c=c;
    }


    public  Drawable loadImageFromStorage(String file)
    {
//initDirectory(Environment.getExternalStorageDirectory().getPath() + "/smartdrone/"+"compress");

        Bitmap b=null ;

        Drawable d=null ;

        int start = file.indexOf("DJI");
        String path=file.substring(0,start-1);

            try {

                String pic =file.substring(start);
                Log.d("dji pic", pic);

                File f = new File(path, pic);
                do{
                b= BitmapFactory.decodeStream(new FileInputStream(f));

             d = new BitmapDrawable(b);
            } while (b==null);
                //b[0] = BitmapFactory.decodeStream(new FileInputStream(f));
                 //  saveToInternalStorage(b,pic, Environment.getExternalStorageDirectory().getPath() + "/smartdrone/"+"");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        /*if (b!=null) {
            b.recycle();

            b=null;
        }*/




    return d;
    }
    //get directory filelist


    public  Bitmap showimage(String file)
    {
//initDirectory(Environment.getExternalStorageDirectory().getPath() + "/smartdrone/"+"compress");

        Bitmap b=null ;



        int start = file.indexOf("DJI");

        if (start<0)
            return null;
        String path=file.substring(0,start-1);

        try {

            String pic =file.substring(start);
            Log.d("dji pic", pic);

            File f = new File(path, pic);
            do{
                b= BitmapFactory.decodeStream(new FileInputStream(f));


            } while (b==null);
            //b[0] = BitmapFactory.decodeStream(new FileInputStream(f));
            //  saveToInternalStorage(b,pic, Environment.getExternalStorageDirectory().getPath() + "/smartdrone/"+"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /*if (b!=null) {
            b.recycle();

            b=null;
        }*/




        return b;
    }


    //get directory filelist
    public String[] getDirectoryFilelist(String directory) {


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
    protected String[] getDirectoryFilelist1(String directory, int index, int num) {


        String[] filelist;
        File sourceDirectory = new File(directory);

        int folderCount = 0;
        //except folders
        for (File file : sourceDirectory.listFiles()) {
            if (file.isDirectory()) {
                folderCount++;
            }
        }
        int length=index+num-1;
        filelist = new String[num];

        int j=0;
        File[] f=sourceDirectory.listFiles();
        for (int i=index;i<=length&&i<f.length;i++) {




            if (!f[i].isDirectory() && !(f[i].getPath().endsWith(".tmp"))) {
                Log.d("getFilelist file:" , f[i].getPath());
                filelist[j] = f[i].getPath();
                j++;

            }


        }

        return filelist;
    }
    public static int saveToInternalStorage(Bitmap bitmapImage, String pic, String path){

        // File directory = new File(d);
        // initDirectory(Environment.getExternalStorageDirectory().getPath() + "/smartdrone/compress");
        // Create imageDir
if (bitmapImage==null)
    return 0;
        File mypath=new File(path,pic);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
           // Bitmap b=makeBackgroundWhite(bitmapImage);
            Bitmap b1=bitmapImage.createScaledBitmap(bitmapImage,(int)(bitmapImage.getWidth()*0.6),(int)(bitmapImage.getHeight()*0.6),true);
            b1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       if (bitmapImage!=null){
            bitmapImage.recycle();
        bitmapImage=null;}
        return 1;
    }



    //init stitching image folder
    public static int initDirectory(String mkdir)
    {
        //check exists,if not,create
        File sourceDirectory = new File(mkdir);
        if(!sourceDirectory.exists())
        {
            sourceDirectory.mkdirs();
        }


        return sourceDirectory.listFiles().length;
    }







    protected String getCurrentDateTime()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return df.format(c.getTime());
    }



}

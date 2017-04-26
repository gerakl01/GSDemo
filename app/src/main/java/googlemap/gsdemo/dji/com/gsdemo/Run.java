package googlemap.gsdemo.dji.com.gsdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import java.io.File;

import image.edit.opencv.ImageEdit;

/**
 * Created by george on 07/10/2016.
 */
public class Run extends MainActivity implements Runnable {

int i;
    static Context c;


    static int first=0;
    public Run(int i,Context c){

this.i=i;
        this.c=c;
    }
    @Override
    public void run() { //Log.d("Main ",  altitude_w[0]+" " );


        Log.d("Run", "point: "+i);

if (i<0) {


    if (i==-1 ){

        second++;
    }

    if (i==-2 ){
        first++;
    }


    if (i==-1&&((second==2&&!chekcedopti)||(second==1&&chekcedopti))&&change==false&&!showimage) {
/*
drone_move=null;
        if (!calt.isEmpty()){

            for (int i = 0; i < calt.size(); i++)
                mapView.getLayerManager().getLayers().remove(calt.get(i));

        }

        for (int i = 0; i < markspolyline.size(); i++)
            mapView.getLayerManager().getLayers().remove(markspolyline.get(i));
        mapView.getLayerManager().getLayers().remove( diagonios[1]);
        //diagonios.remove(1);
        diagonios[1]=null;
        corner_points--;
        second=0;
        for (int i = 0; i < marks.size(); i++){
            mapView.getLayerManager().getLayers().remove(marks.get(i));


        }

       //marks.clear();
        if (mWaypointMission != null) {
            mWaypointMission.removeAllWaypoints(); // Remove all the waypoints added to the task
        }
        Log.d("Run ",  i+" " +"tap");*/
    }
    else if (i==-1&&change){


        Drawable drawable = c.getResources().getDrawable(R.drawable.check);
        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
        bitmap.incrementRefCount();


        Mark k = new Mark(new LatLong(drone_move[d].lat, drone_move[d].lon), bitmap, 0, -bitmap.getHeight()/2 );
        mapView.getLayerManager().getLayers().add(k);
        calt.add(k);
            Log.d("Run ",  (d)+" " +altitude_w[d]);
        altitude_w[d]=newalti;

    }


}

      //
         if (showimage&&i>=0){


            Log.d("Run ", "Image");
            ImageEdit im=new ImageEdit();
             Toast.makeText(c1, STITCHING_SOURCE_IMAGES_DIRECTORY+"/photos", Toast.LENGTH_SHORT).show();
            String Filelist[]=im.getDirectoryFilelist(STITCHING_SOURCE_IMAGES_DIRECTORY+"/photos");

            if (Filelist.length<i)
                return;

            android.graphics.Bitmap b=im.showimage(Filelist[i]);

           // ImageView mImgView1 = ((ImageView)a.findViewById(R.id.ImageFeed));

if (b!=null){
    ImageView show=(ImageView) a.findViewById(R.id.video_previewer_surface);
    //uninitPreviewer();
    show.setVisibility(View.VISIBLE);
    show.setImageBitmap(b);}




        }


        if (change){

            Drawable drawable = c.getResources().getDrawable(R.drawable.check);
            Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
            bitmap.incrementRefCount();
/*

           if (type==1 &&i<random_marks.size()) {
               Mark k = new Mark(new LatLong(random_marks.get(i).getPosition().latitude, random_marks.get(i).getPosition().longitude), bitmap, 0, -bitmap.getHeight() / 2);
               mapView.getLayerManager().getLayers().add(k);
               marks.add(k);
               Log.d("Main ", i + " ");
               // addmap(i);
               altitude_w[i] = newalti;
               Log.d("Run ", i + " " + altitude_w[i]);
           }

*/
           // Log.d("Run ",  i+" " +drone_move.length);

             if (!TapMap.random_points.isEmpty()&&type==1){
                drawable = c.getResources().getDrawable(R.drawable.check);
                bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
                bitmap.incrementRefCount();
                Mark k = new Mark(new LatLong(TapMap.random_points.get(i).getLatitude(), TapMap.random_points.get(i).getLongitude()), bitmap, 0, -bitmap.getHeight()/2 );
                mapView.getLayerManager().getLayers().add(k);
                calt.add(k);
                altitude_w[i]=newalti;

            }

if (drone_move!=null&&altitude_w!=null&&i<altitude_w.length&&i>=0&&i<drone_move.length)
            if (type==2||type==3){


                drawable = c.getResources().getDrawable(R.drawable.check);
                bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
                bitmap.incrementRefCount();
                Mark k = new Mark(new LatLong(drone_move[i].lat, drone_move[i].lon), bitmap, 0, -bitmap.getHeight()/2 );
                mapView.getLayerManager().getLayers().add(k);
                calt.add(k);

                altitude_w[i]=newalti;
                Log.d("Run ",  i+" " +altitude_w[i]);


            }

        }

    }
}

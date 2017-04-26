package googlemap.gsdemo.dji.com.gsdemo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.overlay.Marker;

import java.text.DecimalFormat;

/**
 * Created by george on 05/10/2016.
 */
public class Mark  extends Marker{


    public Mark(LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
    }


    public String marker_description;
       public int  marker_type;
       private Runnable action;

       public void setOnTabAction(Runnable action){

           this.action = action;
  }

    @Override
       public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {



        double centerX = layerXY.x + getHorizontalOffset();
        double centerY = layerXY.y + getVerticalOffset();

        double radiusX = (getBitmap().getWidth() / 2) *1.1;
        double radiusY = (getBitmap().getHeight() / 2) *1.1;


        double distX = Math.abs(centerX - tapXY.x);
        double distY = Math.abs(centerY - tapXY.y);


        if( distX < radiusX && distY < radiusY){

            if(action != null){
                action.run();
                return true;
            }
        }
        return false;


        }




}
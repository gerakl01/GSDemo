package googlemap.gsdemo.dji.com.gsdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.util.MapViewProjection;
import dji.sdk.missionmanager.DJIWaypoint;

import java.io.File;
import java.util.LinkedList;
import java.util.List;



/**
 * Created by george on 11/10/2016.
 */
public class TapMap extends MainActivity {

    private static Layer inDrag=null;
    static Context c;
    static Activity a;
    static List<LatLong> random_points = new LinkedList<>();
    private double xDragImageOffset=0;
    private double yDragImageOffset=0;
    private double xDragTouchOffset=0;
    private double yDragTouchOffset=0;
    private ImageView dragImage;
    private static boolean limit=true;
    private static boolean changescreen=true;
private static int next=0;

    TapMap(Context c, Activity a) {
        this.c = c;
        this.a=a;
    }


    TapMap() {

    }
    class SingleTDetector extends GestureDetector.SimpleOnGestureListener

    {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        public boolean onDoubleTap(MotionEvent ev) {
            Log.d("TapCamera","Couble tap");
            // Activity a = (Activity) c;



            return super.onDoubleTap(ev);
        }


        @Override
        public boolean onDown(MotionEvent event) {



            return true;
        }


        @Override
        public void onLongPress(MotionEvent e) {


            int e1 = e.getAction();
            if (e1 == MotionEvent.ACTION_DOWN) {

                //Toast.makeText(c, "Random Waypoint added", Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        public boolean onSingleTapUp(MotionEvent e) {
           // ImageView show=(ImageView) findViewById(R.id.showimage);
            //uninitPreviewer();
            //show.setVisibility(View.INVISIBLE);


            Log.d("DoubleTapDetector", "Double Tap!");
            //  if (event.getAction() == 1&&!(event.getAction()==MotionEvent.ACTION_BUTTON_PRESS)) {
            int[] viewCoords = new int[2];
            mapView.getLocationOnScreen(viewCoords);
            double x = e.getX() - viewCoords[0];
            double y = e.getRawY() - viewCoords[1];


            LatLong gpoint = new MapViewProjection(mapView).fromPixels(x, y);
            double latitude = (float) gpoint.getLatitude();
            double longitude = (float) gpoint.getLongitude();

                /*if (corner[1]!=null)
                    if (Double.compare(corner[1].lat, latitude)==0&&Double.compare(corner[1].lon, longitude)==0) {
                        mapView.getLayerManager().getLayers().remove(marks.get(0));
                        Log.d("diagonios","onDown: " );



                    }*/


            Drawable drawable = c.getResources().getDrawable(R.drawable.red_mark);
            Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
            bitmap.incrementRefCount();

            Log.d("DoubleTapDetector", type + " " + isAdd);
            if (change==false)
            if (mapView.getModel().mapViewPosition.getZoomLevel() > 14) {
                if (type == 1) {
                    Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
                    paint.setColor(Color.BLUE);
                    paint.setStyle(Style.STROKE);
                    paint.setStrokeWidth(5);
                    Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);
                    boolean distance = true;


                    if (isAdd == true) {
                        LatLong l = new LatLong(latitude, longitude);


                        Mark k = new Mark(l, bitmap, 0, -bitmap.getHeight() / 2);
                        k.setOnTabAction(new Run(0, c));
                        if (random_points.size() >= 1) {
                            Log.d("TapMap", l.getLatitude() + " " + l.getLongitude());
                            Log.d("TapMap", random_points.get(random_points.size() - 1).getLatitude() + "," + random_points.get(random_points.size() - 1).getLongitude());
                            Log.d("TapMap", Coordinates.distFrom(l.getLatitude(), l.getLongitude(), random_points.get(random_points.size() - 1).getLatitude(), random_points.get(random_points.size() - 1).getLongitude()) + " ");
                            if (Coordinates.distFrom(l.getLatitude(), l.getLongitude(), random_points.get(random_points.size() - 1).getLatitude(), random_points.get(random_points.size() - 1).getLongitude()) > 2) {
                                Toast.makeText(c, "Decrease points distance less than 2 km", Toast.LENGTH_SHORT).show();
                                distance = false;
                            }
                        }

                        if (random_points.size() >= 1 && distance) {


                            List<LatLong> latLongs = polyline.getLatLongs();

                            latLongs.add(random_points.get(random_points.size() - 1));
                            latLongs.add(l);
                            marks.add(polyline);
                            mapView.getLayerManager().getLayers().add(polyline);


                        }
                        if (random_points.isEmpty()) {
                            drawable = c.getResources().getDrawable(R.drawable.start1);
                            bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
                            bitmap.incrementRefCount();
                            k = new Mark(l, bitmap, 0, -bitmap.getHeight() / 2);

                        }
                        if (distance) {
                            random_points.add(l);
                            mapView.getLayerManager().getLayers().add(k);

                            k.setOnTabAction(new Run(random_points.size() - 1, c));
                            marks.add(k);
                            // k.setOnTabAction(r = new Run(random_marks.size(), c));
                            DJIWaypoint mWaypoint = new DJIWaypoint(latitude, longitude, altitude);
                            //Add Waypoints to Waypoint arraylist;
                            if (mWaypointMission != null) {
                                Toast.makeText(c, "Random Waypoint added", Toast.LENGTH_SHORT).show();
                                mWaypointMission.addWaypoint(mWaypoint);

                            }
                        }
                    } else if (type != 0) {
                        if (!change)
                            Toast.makeText(c, "Cannot Add Waypoint", Toast.LENGTH_SHORT).show();

                    }
                    if (polyline != null) {
                    }

                } else if ((type == 2||type==3)) {


                    if (corner_points < 2)

                    {
                        Toast.makeText(c, corner_points + " ", Toast.LENGTH_SHORT).show();
                        diagonii[corner_points] = new Coordinates(latitude, longitude);

                        if (corner_points == 0) {

                            drawable = c.getResources().getDrawable(R.drawable.start1);
                            bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
                            bitmap.incrementRefCount();
                        }


                        Mark k = new Mark(new LatLong(latitude, longitude), bitmap, 0, -bitmap.getHeight() / 2);

                        if (corner_points == 1) {
                            if (type==3)
                            k.setOnTabAction(new Run(-1, c));

                        } else {


                        }
                  // marks.add(k);
                        mapView.getLayerManager().getLayers().add(k);


                        diagonios[corner_points] = k;
                        corner[corner_points] = new Coordinates(latitude, longitude);
                        Log.d("corner 1", corner[corner_points].toString());


                        corner_points++;
                    } else {
                        if (!change)
                            Toast.makeText(c, "Only two points can add", Toast.LENGTH_SHORT).show();


                    }


                    if (corner_points == 2&&drone_move==null) {

                        Log.d(TAG, drone_move+" drone mmove");
                        for (int i = 0; i < marks.size(); i++)
                            mapView.getLayerManager().getLayers().remove(marks.get(i));
                        // marks.clear();
                        for (int i = 1; i < markspolyline.size(); i++) {
                            mapView.getLayerManager().getLayers().remove(markspolyline.get(i));


                        }

                        if (Double.compare(corner[1].lat,corner[0].lat)!=1){
                            Log.d(TAG, " change");
                            mapView.getLayerManager().getLayers().remove(diagonios[0]);
                            mapView.getLayerManager().getLayers().remove(diagonios[1]);

                            drawable = c.getResources().getDrawable(R.drawable.start1);
                            bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
                            bitmap.incrementRefCount();
                            Mark k = new Mark(new LatLong(corner[1].lat, corner[1].lon), bitmap, 0, -bitmap.getHeight() / 2);
                            mapView.getLayerManager().getLayers().add(k);
                            diagonios[0]=k;


                            drawable = c.getResources().getDrawable(R.drawable.red_mark);
                            bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
                            bitmap.incrementRefCount();
                             k = new Mark(new LatLong(corner[0].lat, corner[0].lon), bitmap, 0, -bitmap.getHeight() / 2);
                            mapView.getLayerManager().getLayers().add(k);
                            diagonios[1]=k;

                            Coordinates temp=corner[0];

                            corner[0]=new Coordinates(corner[1].lat,corner[1].lon);

                            corner[1]=new Coordinates(temp.lat,temp.lon);

                        }

                         double[] lon = new double[2];
                         double[] lan = new double[2];

                        lon[0] = corner[0].lon;

                        lan[0] = corner[0].lat;
                        lon[1] = corner[1].lon;
                        lan[1] = corner[1].lat;

                        if (type==3)
                            drone_move = Coordinates.create_square(lon, lan, fov, altitude);
                        else  if (type==2)
                        drone_move = new Coordinates(c,null).create_grid(lon, lan, fov, altitude,chekcedopti);

                        MainActivity m = new MainActivity();
                        if (drone_move != null)
                            m.addMarks(drone_move);
                        else {
                            drone_move = new Coordinates[4];
                            drone_move[0] = new Coordinates(lan[0],lon[0]);
                            drone_move[1] = new Coordinates(lan[1],lon[0]);

                            drone_move[2] = new Coordinates(lan[1],lon[1]);
                            drone_move[3] = new Coordinates(lan[0],lon[1]);
                            m.addMarks(drone_move);

                        }

                        add_waypoints.setEnabled(true);


                    }

                }
            } else if (type != 0) {
                Toast.makeText(c, "Zoom in more to add waypoints", Toast.LENGTH_SHORT).show();
            }


            return false;
        }


    }


    public static  void clearpoints(){
        STITCHING_SOURCE_IMAGES_DIRECTORY=null;
       // drone_move=null;
      //  if (droneMarker!=null)
         //   mapView.getLayerManager().getLayers().remove(droneMarker);
        if (!calt.isEmpty()){

            for (int i = 0; i < calt.size(); i++)
                mapView.getLayerManager().getLayers().remove(calt.get(i));

        }
        second = 0;
        add_waypoints.setEnabled(false);
        //corner_points = 0;
        if (!TapMap.random_points.isEmpty())
            TapMap.random_points.clear();

        for (int i = 0; i < marks.size(); i++)
            mapView.getLayerManager().getLayers().remove(marks.get(i));
        for (int i = 0; i < markspolyline.size(); i++)
           mapView.getLayerManager().getLayers().remove(markspolyline.get(i));

        if (!marks.isEmpty())
            marks.clear();
        if (diagonios[0] != null)
            for (int i = 0; i < diagonios.length&&diagonios[i]!=null; i++)

                mapView.getLayerManager().getLayers().remove(diagonios[i]);
        //diagonios = new Layer[2];

        if (random_marks != null)
            for (int i = 0; i < random_marks.size(); i++)
                mapView.getLayerManager().getLayers().remove(random_marks.get(i));
        if (mWaypointMission != null) {
            mWaypointMission.removeAllWaypoints(); // Remove all the waypoints added to the task
        }

    }


    public  boolean touch(MotionEvent event) {
        dragImage=(ImageView)a.findViewById(R.id.drag);
        int[] viewCoords = new int[2];
        mapView.getLocationOnScreen(viewCoords);
        double x = event.getX() - viewCoords[0];
        double y = event.getRawY() - viewCoords[1];


        LatLong gpoint = new MapViewProjection(mapView).fromPixels(x, y);
        double latitude = (float) gpoint.getLatitude();
        double longitude = (float) gpoint.getLongitude();

        Log.d("TapMap","in");
        final int action=event.getAction();

        boolean result=false;
        xDragImageOffset=dragImage.getDrawable().getIntrinsicWidth()/2;
        yDragImageOffset=dragImage.getDrawable().getIntrinsicHeight();


        if (action==MotionEvent.ACTION_DOWN) {
next=0;
            if (diagonios[0]!=null&&diagonios[1]!=null&&drone_move!=null&&missionstart==false){
                for (Layer item : diagonios) {


                    Log.d("TapMap","Marker: "+item.getPosition().toString());
                    Log.d("TapMap",gpoint.toString());
                    Point p=new Point(0,0);

                    mapView.getMapViewProjection().toPixels(item.getPosition());


                    if (Coordinates.distFrom(latitude,longitude,item.getPosition().getLatitude(),item.getPosition().getLongitude())<=0.02/*item.getPosition().compareTo(gpoint)==*/) {
                      //  mapView.setEnabled(false);
                        Log.d("TapMap","found");
                        result=true;
                        inDrag=item;
                        mapView.getLayerManager().getLayers().remove(inDrag);
                        marks.remove(inDrag);
                        // populate();

                        xDragTouchOffset=0;
                        yDragTouchOffset=0;

                        setDragImagePosition(p.x, p.y);
                        dragImage.setVisibility(View.VISIBLE);

                        xDragTouchOffset=x-p.x;
                        yDragTouchOffset=y-p.y;

                        break;
                    }
                    next++;

                }
            }}
        else if (action==MotionEvent.ACTION_MOVE && inDrag!=null) {
           // mapView.setEnabled(true);
           if (type==2 ||type==3){
              // clearpoints();
                double mx = event.getX() - viewCoords[0];
                double my = event.getRawY() - viewCoords[1];
                 gpoint = new MapViewProjection(mapView).fromPixels(mx, my);
                 latitude = (float) gpoint.getLatitude();
                 longitude = (float) gpoint.getLongitude();
               // drone_move=null;
                double[] lat=new double[2];
                double[] lon=new double[2];

                 if (next==1){

                lat[0]=corner[0].lat;
                lat[1]=latitude;
                lon[0]=corner[0].lon;
                lon[1]=longitude;}
               else {

                     lat[0]=latitude;
                     lat[1]=corner[1].lat;
                     lon[0]=longitude;
                     lon[1]=corner[1].lon;

                 }

               if (Double.compare(lat[1],lat[0])==1){dragImage.setVisibility(View.VISIBLE);
                   setDragImagePosition(event.getX(), event.getY());
                   limit=true;}
               else {   dragImage.setVisibility(View.GONE);
                   limit=false;}
             /*  if (type==2)
               drone_move=new Coordinates(c,a).create_grid(lon, lat,fov,altitude,false);
else if (type==3)
                   drone_move=new Coordinates(c,a).create_square(lon, lat,fov,altitude);
               MainActivity m = new MainActivity();
               if (drone_move != null) {
                   altitude_w=new float[drone_move.length];
                   Log.d("Demo ",  " " +drone_move.length);
                   m.addMarks(drone_move);
               } else {
                 altitude_w=new float[4];
                   *//**//*  drone_move = new Coordinates[4];
                   drone_move[0] = corner[0];
                   drone_move[1] = new Coordinates(corner[1].lat, corner[0].lon);

                   drone_move[2] = corner[1];
                   drone_move[3] = new Coordinates(corner[0].lat, corner[1].lon);
                   m.addMarks(drone_move);*//**//*
corner[0]= new Coordinates(lat[0],lon[0]);
                   corner[1]= new Coordinates(lat[1],lon[1]);
                   drone_move = new Coordinates[4];
                   drone_move[0] = new Coordinates(lat[0],lon[0]);
                   drone_move[1] = new Coordinates(lat[1],lon[0]);

                   drone_move[2] = new Coordinates(lat[1],lon[1]);
                   drone_move[3] = new Coordinates(lat[0],lon[1]);
                   m.addMarks(drone_move);
               }*/
           }
            Log.d("TapMap","action move");
           // mapView.setEnabled(false);

            result=true;
        }
        else if (action==MotionEvent.ACTION_UP && inDrag!=null&&limit) {
            Log.d("TapMap","action up");
            dragImage.setVisibility(View.GONE);
        /*    if (Double.compare(corner[1].lat,corner[0].lat)!=1){

                mapView.getLayerManager().getLayers().remove(diagonios[0]);
                mapView.getLayerManager().getLayers().remove(diagonios[1]);

               Drawable drawable = c.getResources().getDrawable(R.drawable.start1);
              Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
                bitmap.incrementRefCount();
                Mark k = new Mark(new LatLong(corner[1].lat, corner[1].lon), bitmap, 0, -bitmap.getHeight() / 2);
                mapView.getLayerManager().getLayers().add(k);
                diagonios[0]=k;


                drawable = c.getResources().getDrawable(R.drawable.red_mark);
                bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
                bitmap.incrementRefCount();
                k = new Mark(new LatLong(corner[0].lat, corner[0].lon), bitmap, 0, -bitmap.getHeight() / 2);
                mapView.getLayerManager().getLayers().add(k);
                diagonios[1]=k;

                Coordinates temp=corner[0];

                corner[0]=new Coordinates(corner[1].lat,corner[1].lon);

                corner[1]=new Coordinates(temp.lat,temp.lon);

            }*/

         //   mapView.setEnabled(true);
            gpoint = new MapViewProjection(mapView).fromPixels(x, y);
            latitude = (float) gpoint.getLatitude();
            longitude = (float) gpoint.getLongitude();


            LatLong LatLong2 = new LatLong(latitude, longitude);
            Drawable drawable = c1.getResources().getDrawable(R.drawable.red_mark);
            org.mapsforge.core.graphics.Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(drawable);
            bitmap.incrementRefCount();



            Marker k = new Mark(LatLong2, bitmap, 0, -bitmap.getHeight() / 2);
            mapView.getLayerManager().getLayers().add(k);


            marks.add(k);
            // populate();
            if (type==2 ||type==3){
                clearpoints();

                gpoint = new MapViewProjection(mapView).fromPixels(x, y);
                latitude = (float) gpoint.getLatitude();
                longitude = (float) gpoint.getLongitude();
                drone_move=null;
                double[] lat=new double[2];
                double[] lon=new double[2];

                if (next==1){
                    diagonios[1]=k;
                    lat[0]=corner[0].lat;
                    lat[1]=latitude;
                    lon[0]=corner[0].lon;
                    lon[1]=longitude;}
                else {
                    diagonios[0] = k;
                    lat[0]=latitude;
                    lat[1]=corner[1].lat;
                    lon[0]=longitude;
                    lon[1]=corner[1].lon;

                }
                if (type==2)
                    drone_move=new Coordinates(c,a).create_grid(lon, lat,fov,altitude,false);
                else if (type==3)
                    drone_move=new Coordinates(c,a).create_square(lon, lat,fov,altitude);

                MainActivity m = new MainActivity();
                if (drone_move != null) {
                    altitude_w=new float[drone_move.length];
                    Log.d("Demo ",  " " +drone_move.length);
                    m.addMarks(drone_move);
                } else {
                     altitude_w=new float[4];
                   /* drone_move = new Coordinates[4];
                   drone_move[0] = corner[0];
                   drone_move[1] = new Coordinates(corner[1].lat, corner[0].lon);

                   drone_move[2] = corner[1];
                   drone_move[3] = new Coordinates(corner[0].lat, corner[1].lon);
                   m.addMarks(drone_move);*/

                    drone_move = new Coordinates[4];
                    drone_move[0] = new Coordinates(lat[0],lon[0]);
                    drone_move[1] = new Coordinates(lat[1],lon[0]);

                    drone_move[2] = new Coordinates(lat[1],lon[1]);
                    drone_move[3] = new Coordinates(lat[0],lon[1]);
                    m.addMarks(drone_move);
                }
            }
            inDrag=null;

        }

return result;
    }

    private void setDragImagePosition(double x, double y) {
        RelativeLayout.LayoutParams lp=
                (RelativeLayout.LayoutParams)dragImage.getLayoutParams();

        lp.setMargins((int)(x-xDragImageOffset-xDragTouchOffset),
                (int)( y-yDragImageOffset-yDragTouchOffset), 0, 0);
        dragImage.setLayoutParams(lp);
    }

    protected  void initialMap(String MAP_FILE){
        // create a tile cache of suitable size
        TileCache tileCache = AndroidUtil.createTileCache(c1, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor());

        // tile renderer layer using internal render theme
        // MapDataStore mapDataStore = new MapFile(new File(Environment.getExternalStorageDirectory(), MAP_FILE));
        MapDataStore mapDataStore = new MapFile(new File(Environment.getExternalStorageDirectory()+"/smartdrone/maps", MAP_FILE));
        TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);

        // only once a layer is associated with a mapView the rendering starts
        mapView.getLayerManager().getLayers().add(tileRendererLayer);
if (MAP_FILE.equals("cyprus.map"))
        mapView.setCenter(new LatLong(35.14448546, 33.40969473));
        else

    mapView.setCenter(new LatLong(38.1980244,19.3091665));
        //mapView.setCenter(new LatLong(  mapView.getMapViewProjection().getLatitudeSpan(),   mapView.getMapViewProjection().getLongitudeSpan()));
        mapView.setZoomLevel((byte) 12);


    }

}

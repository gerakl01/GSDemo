package googlemap.gsdemo.dji.com.gsdemo;

import android.util.Log;
import android.widget.Toast;
import android.content.Context;

import org.mapsforge.core.model.LatLong;

import java.text.DecimalFormat;

import googlemap.gsdemo.dji.com.gsdemo.MainActivity;



/**
 * Created by George on 7/5/2016.
 */
public class Coordinates {



    protected static final String TAG = "GSDemoActivity";

    double lat;

    LatLong l;
    double distance;
    double lon;
    float altitute;
    Coordinates(double lat,double lon){

        this.lat=lat;
        this.lon=lon;


    }
    Coordinates(LatLong l,double distance,float h){
this.l=l;
        this.distance=distance;
        this.altitute=h;


    }

    @Override
    public String toString() {
        return lat+" "+lon;
    }
    public static double bearing(double lat1, double long1, double lat2, double long2)

    {

        double degToRad = Math.PI / 180.0;

        double phi1 = lat1 * degToRad;

        double phi2 = lat2 * degToRad;

        double lam1 = long1 * degToRad;

        double lam2 = long2 * degToRad;


        return Math.atan2(Math.sin(lam2 - lam1) * Math.cos(phi2),

                Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(lam2 - lam1)

        ) * 180 / Math.PI;

    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371.0; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }
    public static boolean check_array(boolean d[][]) {
        for (int i = 0; i < d.length; i++) {

            for (int j = 0; j < d[0].length; j++) {
                if (d[i][j])
                    return true;

            }
        }
        return false;

    }


    public static int[][] neighboor(boolean d[][], int i, int j) {
        int flag[][] = new int[2][2];
        int k = 0;


        if (i - 1 >= 0 && d[i - 1][j] == true) {
            flag[k][0] = i - 1;
            flag[k][1] = j;
            k++;
        }

        if (i + 1 < d.length && d[i + 1][j] == true) {
            flag[k][0] = i + 1;
            flag[k][1] = j;
            k++;
        }
        if (j + 1 < d[0].length && d[i][j + 1] == true) {
            flag[k][0] = i;
            flag[k][1] = j + 1;
            k++;
        }
        if (j - 1 >= 0 && d[i][j - 1] == true) {
            flag[k][0] = i;
            flag[k][1] = j - 1;
            k++;
        }

        if (k == 1)
            flag[1][1] = -1;


        return flag;

    }



    // clone two dimensional array
    public static Coordinates[][] twoDimensionalArrayClone( Coordinates[][] a) {
        Coordinates[][] b = new Coordinates[a.length][];
        for (int i = 0; i < a.length; i++) {
            b[i] = a[i].clone();
        }
        return b;
    }

    public static Coordinates [] algorithm_path(Coordinates drone_point[][],int drone_num[][]) {
        Coordinates drone_move[]= new Coordinates[drone_point.length*drone_point[0].length];
        boolean[][] drone_flag = new boolean[drone_point.length][drone_point[0].length];

        int dm = 1;

        for (int i = 0; i < drone_flag.length; i++) {

            for (int j = 0; j < drone_flag[0].length; j++) {
                drone_flag[i][j] = true;
            }
        }

        int i = 0, j = 0, k = 0;
        int flag[][] = new int[2][2];
        Log.e(TAG, "Path 1 " + "in " + drone_num[i][j]);
        drone_move[0] = drone_point[0][0];
        while (Coordinates.check_array(drone_flag)&&dm<drone_move.length) {
            //Log.e(TAG, "flag " + drone_flag[i][j] );
            drone_flag[i][j] = false;
            flag = Coordinates.neighboor(drone_flag, i, j);

            if (flag[1][1] == -1) {

                drone_move[dm] = drone_point[flag[0][0]][flag[0][1]];

                i = flag[0][0];
                j = flag[0][1];
                Log.e(TAG, "Path " + " in1 " + drone_num[flag[0][0]][flag[0][1]]);
            } else {

                if (drone_num[flag[0][0]][flag[0][1]] > drone_num[flag[1][0]][flag[1][1]]) {

                    drone_move[dm] = drone_point[flag[0][0]][flag[0][1]];

                    i = flag[0][0];
                    j = flag[0][1];
                    Log.e(TAG, "Path " + " in2  " + drone_num[flag[0][0]][flag[0][1]]);

                } else {

                    drone_move[dm] = drone_point[flag[1][0]][flag[1][1]];
                    i = flag[1][0];
                    j = flag[1][1];
                    Log.e(TAG, "Path3  " + drone_num[flag[1][0]][flag[1][1]]);

                }
            }
            dm++;
        }


return  drone_move;
    }

    public static Coordinates[][] extend_mikos(long r, Coordinates a, Coordinates b,Coordinates midpoints[][]) {


        int l = (int) ((1000 * distFrom(midpoints[1][midpoints[0].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, b.lon, b.lat)) / r);
        Coordinates emid[][] = new Coordinates[2][midpoints[0].length + l];

        for (int i = 0; i < emid.length; i++) {

            for (int j = 0; j < emid[0].length; j++) {

                if (j <= midpoints[0].length - 1)
                    emid[i][j] = midpoints[i][j];


            }
        }
Coordinates t1D[]=new Coordinates[emid[0].length-midpoints[0].length];
        get_gps_distance(emid[0][midpoints[0].length - 1].lat, emid[0][midpoints[0].length - 1].lon,bearing(emid[0][midpoints[0].length - 1].lat, emid[0][midpoints[0].length - 1].lon, a.lat, a.lon), emid[0].length-midpoints[0].length, r, 0, 2,t1D,emid,0);

        int e=midpoints[0].length;
        for (int i = 0; i < emid[0].length-midpoints[0].length; i++) {
            emid[0][e]=t1D[i];
            e++;
        }



        get_gps_distance(emid[1][midpoints[0].length - 1].lat, emid[1][midpoints[0].length - 1].lon,bearing(emid[1][midpoints[0].length - 1].lat, emid[1][midpoints[0].length - 1].lon, b.lat, b.lon),  emid[0].length-midpoints[0].length, r, 1, 2,t1D,emid,0);

        e=midpoints[0].length;
        for (int i = 0; i < emid[0].length-midpoints[0].length; i++) {
            emid[1][e]=t1D[i];
            e++;
        }


return emid;
    }
    public Coordinates[] decrease_y(Coordinates a,Coordinates b,Coordinates[] points){


        int sum=0;
        for (int i=0;i<points.length/2;i++){
            if (points[i].lon>a.lon)
                sum++;



        }
        Log.e(TAG, "Sum  " +" "+sum);
        Coordinates p[]=new Coordinates[points.length-(2*sum)];

        int j=0;

        for (int i=0;i<points.length;i++){

            if (points[i].lon<a.lon&&i<points.length/2){

                p[j]=points[i];
                j++;
                // Log.e(TAG, " decrease  " + i+" "+p[j].toString());
            }
            else  if (points[i].lon<b.lon&&i>=points.length/2){
                p[j]=points[i];
                j++;
                //Log.e(TAG, " decrease  " + i+" "+p[j].toString());
            }


        }
        Log.e(TAG, "Size decrease  " +" "+p.length);

        //points=new Coordinates[p.length];

        //System.arraycopy(p,0,points,0,p.length);

        return p;
    }




    public static int get_gps_distance(double lat1, double long1, double angle, int length, long d, int z, int type,Coordinates table1D[],Coordinates table2D[][],int s) {
        // Earth Radious in KM
        double R = 6371;


        // 6 decimal for Leaflet and other system compatibility
        //  double lat2 = Math.round (latitude2,6);
        //double long2 = Math.round (longitude2,6);


        double brng = Math.toRadians(angle);


        Coordinates c;
        double r = d / 1000.0;
        double sum = r;
        for (int i = 0; i < length; i++) {
            //sum = sum+r;

            // Degree to Radian
            double latitude1 = Math.toRadians(lat1);
            double longitude1 = Math.toRadians(long1);
            // double brng = Math.toRadians(angle);;

            double latitude2 = Math.asin(Math.sin(latitude1) * Math.cos(sum / R * 1.0) + Math.cos(latitude1) * Math.sin(sum / R * 1.0) * Math.cos(brng));
            double longitude2 = longitude1 + Math.atan2(Math.sin(brng) * Math.sin(sum / R * 1.0) * Math.cos(latitude1), Math.cos(sum / R * 1.0) - Math.sin(latitude1) * Math.sin(latitude2));

            double lon2 = ((longitude2 + 3 * Math.PI) % (2 * Math.PI)) - Math.PI;
            // double lon2=(( longitude1- longitude2  +Math.PI)%(2*Math.PI ))-Math.PI;

            // back to degrees
            latitude2 = Math.toDegrees(latitude2);
            longitude2 = Math.toDegrees(lon2);

            DecimalFormat df = new DecimalFormat("##.######");
            latitude2 = Double.parseDouble(df.format(latitude2));


            longitude2 = Double.parseDouble(df.format(longitude2));

            int k = 0;


            c = new Coordinates(latitude2, longitude2);


            if (type == 1) {


                table2D[z][i] = c;

                Log.e(TAG, "midPoint " + i + " " +  table2D[z][i].toString());


            } else  {

                table1D[s] = c;
                Log.e(TAG, "Point " + s + " " +  table1D[s].toString());
                s++;
            }


            lat1 = latitude2;
            long1 = longitude2;


        }

return s;
    }



    public static Coordinates[] create_grid(double lat[], double lon[], int f, int al) {
        Coordinates c=new Coordinates(0,0);

MainActivity ma=new MainActivity();
       // ma.setResultToToast("distance " +Coordinates. distFrom(lat[1], lon[1], lat[0], lon[0]));
        Log.e(TAG, "distance " +Coordinates. distFrom(lat[1], lon[1], lat[0], lon[0]));


        long r = Math.abs(Math.round(2.0 * al * Math.tan(Math.toRadians(f / 2.0))));
        Log.e(TAG, "r " + r);
        //ma.setResultToToast("r " + r);


       int x = (int) ((1000 * Coordinates.distFrom(lat[1], lon[1], lat[1], lon[0])) / r);
        Log.e(TAG, "distance x " + Coordinates.distFrom(lat[1], lon[1], lat[1], lon[0]));
       int y = (int) ((1000 * Coordinates.distFrom(lat[1], lon[1], lat[0], lon[1])) / r);
        Log.e(TAG, "distance y " + Coordinates.distFrom(lat[1], lon[1], lat[0], lon[1]));
        Log.e(TAG, "y " + y);
        Log.e(TAG,"Corner 1 " + new Coordinates(lon[1], lat[1]).toString());
        Log.e(TAG,"Corner 1 " + new Coordinates(lon[0], lat[1]).toString());

        Log.e(TAG, "y " + y);

        if (y > 1)
            y = y - 1;

        if (x == 0 || y == 0) {



            return null;

        }
        Log.e(TAG, "x " + x);
        //ma.setResultToToast("x " + x);
        Coordinates[] points = new Coordinates[(2 * y)];
        Coordinates[][] midpoints = new Coordinates[2][x];
        Coordinates[] inpoints = new Coordinates[(x * y)];
        Coordinates[][] drone_point = new Coordinates[x + 2][y + 2];
        int[][] drone_num = new int[x + 2][y + 2];


        //points_2line(new Coordinates( lon[0],lat[1]),new Coordinates( lon[1],lat[1]),r,x,true);
        //points_2line(new Coordinates(lon[1], lat[1]), new Coordinates(lon[0], lat[1]), r, y, false);
        //points_2line(new Coordinates( lon[0],lat[0]),new Coordinates( lon[1],lat[0]),r,x,true);
        /// points_2line(new Coordinates(lon[0], lat[0]), new Coordinates(lon[1], lat[0]), r, y, false);

        Coordinates t1D[]=new Coordinates[1];
        Coordinates t2D[][]=new Coordinates[1][1];
        int  sy=get_gps_distance(lon[0], lat[0],Coordinates. bearing(lon[0], lat[0], lon[0], lat[1]), y, r, 1, 2,points,t2D,0);
        get_gps_distance(lon[0], lat[1],Coordinates. bearing(lon[0], lat[1], lon[1], lat[1]), x, r, 1, 1,t1D,midpoints,0);//create points for mid points

        get_gps_distance(lon[1], lat[0], Coordinates.bearing(lon[1], lat[0], lon[1], lat[1]), y, r, 0, 2,points,t2D,sy);
        get_gps_distance(lon[0], lat[0],Coordinates. bearing(lon[0], lat[0], lon[1], lat[0]), x, r, 0, 1,t1D,midpoints,0);//create points for mid points


        Log.e(TAG, "extend distance before  " + 1000*Coordinates.distFrom(midpoints[1][midpoints[1].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]));

        while (1000*Coordinates.distFrom(midpoints[1][midpoints[0].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]) > r) {
            Log.e(TAG, "extend distance   " + 1000*Coordinates.distFrom(midpoints[1][midpoints[1].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]));
            int l = (int) ((1000 * c.distFrom(midpoints[1][midpoints[0].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1])) / r);
            Coordinates[][] emid = new Coordinates[2][midpoints[0].length + l];
            emid= c.extend_mikos(r, new Coordinates(lon[1], lat[0]), new Coordinates(lon[1], lat[1]),midpoints);
            x = emid[0].length;
            midpoints = new Coordinates[2][x];
            midpoints=c.twoDimensionalArrayClone(emid);

        }
        int em = midpoints[0].length;
        Log.e(TAG, "extend after   " + 1000*Coordinates.distFrom(midpoints[1][midpoints[1].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]));

        if (points[points.length-1].lon>lat[1]){
            points=c.decrease_y(new Coordinates(lon[0], lat[1]), new Coordinates(lon[1], lat[1]),points);
            y=points.length/2;
        }


        inpoints = new Coordinates[(x * y)];
        drone_point = new Coordinates[x + 2][y + 2];
        drone_num = new int[x + 2][y + 2];

        int in=0;
        for (int i = 0; i < x; i++)
            in=get_gps_distance(midpoints[0][i].lat, midpoints[0][i].lon, Coordinates.bearing(midpoints[0][i].lat, midpoints[0][i].lon, midpoints[1][i].lat, midpoints[1][i].lon), y, r, 0, 2,inpoints,t2D,in);


        int n = 1;
        int p = 0, m1 = 0, m2 = 0, mj = drone_point[0].length - 1, k = 0, p1 = 1;


        int c1 = 2;


        for (int i = 0; i < drone_point.length; i++) {

            for (int j = 0; j < drone_point[0].length; j++) {
                if (i == 0) {
                    drone_num[i][j] = n;
                    n++;
                } else {
                    drone_num[i][j] = n;
                    n--;
                }


                if (j == 0 && i != 0 && i != drone_point.length - 1) {

                    drone_point[i][j] = midpoints[0][m1];
                    m1++;

                } else if (j == mj && i != 0 && i != drone_point.length - 1) {

                    drone_point[i][j] = midpoints[1][m2];
                    m2++;

                } else if (i == 0 && j != mj && j != 0) {

                    drone_point[i][j] = points[p];
                    p++;
                } else if (i == drone_point.length - 1 && j != mj && j != 0) {

                    drone_point[i][j] = points[p];
                    p++;
                } else if (i == 0 && j == 0)
                    drone_point[i][j] = new Coordinates(lon[0], lat[0]);

                else if (i == drone_point.length - 1 && j == 0) {

                    drone_point[i][j] = new Coordinates(lon[1], lat[0]);
                } else if (i == drone_point.length - 1 && j == mj) {

                    drone_point[i][j] = new Coordinates(lon[1], lat[1]);

                } else if (i == 0 && j == mj) {

                    drone_point[i][j] = new Coordinates(lon[0], lat[1]);

                } else {
                    drone_point[i][j] = inpoints[k];
                    k++;
                }
            }
            n = (c1) * (y + 2);
            c1++;

        }

        Coordinates[] drone_move = new Coordinates[drone_point.length * drone_point[0].length];
         drone_move= algorithm_path( drone_point, drone_num);


return  drone_move;
    }

    public static Coordinates midpoint(Coordinates a, Coordinates b) {

        double dLon = Math.toRadians(b.lon - a.lon);

        //convert to radians
        a.lat = Math.toRadians(a.lat);
        b.lat = Math.toRadians(b.lat);
        a.lon = Math.toRadians(a.lon);

        double Bx = Math.cos(b.lat) * Math.cos(dLon);
        double By = Math.cos(b.lat) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(a.lat) + Math.sin(b.lat), Math.sqrt((Math.cos(a.lat) + Bx) * (Math.cos(a.lat) + Bx) + By * By));
        double lon3 = a.lon + Math.atan2(By, Math.cos(a.lat) + Bx);

        //print out in degrees
        Coordinates c = new Coordinates((Math.toDegrees(lon3)), (Math.toDegrees(lat3)));
        return c;

    }


   /* public void create_square(double lat[], double lon[], int al, int f) {
        corner_points  = 0;
        type = 0;
        mid = 0;
        in = 0;
        dr = 0;
        s = 0;
        setResultToToast("distance " +Coordinates. distFrom(lat[1], lon[1], lat[0], lon[0]));
        Log.e(TAG, "distance " +Coordinates. distFrom(lat[1], lon[1], lat[0], lon[0]));


        long r = Math.abs(Math.round(2.0 * al * Math.tan(f / 2.0)));
        Log.e(TAG, "r " + r);
        setResultToToast("r " + r);


        x = (int) ((1000 * Coordinates.distFrom(lat[1], lon[1], lat[1], lon[0])) / r);
        Log.e(TAG, "distance x " + Coordinates.distFrom(lat[1], lon[1], lat[1], lon[0]));
        y = (int) ((1000 * Coordinates.distFrom(lat[1], lon[1], lat[0], lon[1])) / r);
        Log.e(TAG, "distance y " + Coordinates.distFrom(lat[1], lon[1], lat[0], lon[1]));
        Log.e(TAG, "y " + y);
        Log.e(TAG,"Corner 1 " + new Coordinates(lon[1], lat[1]).toString());
        Log.e(TAG,"Corner 1 " + new Coordinates(lon[0], lat[1]).toString());

        Log.e(TAG, "y " + y);

        if (y > 1)
            y = y - 1;

        if (x == 0 || y == 0) {
            setResultToToast("mikos i platos ine miden ");
            corner_points  = 0;

            return;

        }
        Log.e(TAG, "x " + x);
        setResultToToast("x " + x);
        points = new Coordinates[(2 * y)];
        midpoints = new Coordinates[2][x];

        Coordinates  drone_pointS[] = new Coordinates[2*x +2*y +4];
       int drone_numS[] = new int[x + 2];


        get_gps_distance(lon[0], lat[0],Coordinates. bearing(lon[0], lat[0], lon[0], lat[1]), y, r, 1, 2);
        get_gps_distance(lon[0], lat[1],Coordinates. bearing(lon[0], lat[1], lon[1], lat[1]), x, r, 1, 1);//create points for mid points

        get_gps_distance(lon[1], lat[0], Coordinates.bearing(lon[1], lat[0], lon[1], lat[1]), y, r, 0, 2);
        get_gps_distance(lon[0], lat[0],Coordinates. bearing(lon[0], lat[0], lon[1], lat[0]), x, r, 0, 1);//create points for mid points

        while (1000*Coordinates.distFrom(midpoints[1][midpoints[0].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]) > r) {
            Log.e(TAG, "extend distance   " + 1000*Coordinates.distFrom(midpoints[1][midpoints[1].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]));
            extend_mikos(r, new Coordinates(lon[1], lat[0]), new Coordinates(lon[1], lat[1]));
            midpoints = new Coordinates[2][x];
            midpoints=Coordinates.twoDimensionalArrayClone(emid);

        }

        Log.e(TAG, "extend after   " + 1000*Coordinates.distFrom(midpoints[1][midpoints[1].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]));

        if (points[points.length-1].lon>lat[1])
            y=decrease_y(new Coordinates(lon[0], lat[1]), new Coordinates(lon[1], lat[1]));

        Log.e(TAG, "extend distance before  " + 1000*Coordinates.distFrom(midpoints[1][midpoints[1].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]));


        int n = 1;
        int p = 0, m1 = 0, m2 = 0, mj = drone_point[0].length - 1, k = 0, p1 = 1;


        int c1 = 2;
       int m=0,dps=1;

        drone_pointS[0]=new Coordinates(lon[0], lat[0]);
        for (int i = 0; i < midpoints[0].length; i++) {


                drone_pointS[dps]=midpoints[1][i];
            dps++;

        }
        drone_pointS[dps]=new Coordinates(lon[1], lat[0]);
        for (int i = midpoints[0].length; i <points.length; i++) {


            drone_pointS[dps]=points[i];
            dps++;
        }
        drone_pointS[dps]=new Coordinates(lon[1], lat[1]);
        for (int i = midpoints[0].length-1; i >0 ; i--) {


            drone_pointS[dps]=midpoints[0][i];
            dps++;
        }
        drone_pointS[dps]=new Coordinates(lon[0], lat[1]);
        for (int i = points.length/2-1; i > 0; i--) {


            drone_pointS[dps]=points[i];
            dps++;
        }



        add_waypoints.setEnabled(false);
        addMarks();

    }
*/
   public static Coordinates[] create_square(double lat[], double lon[], int al, int f) {
       Coordinates c=new Coordinates(0,0);

       MainActivity ma=new MainActivity();
       // ma.setResultToToast("distance " +Coordinates. distFrom(lat[1], lon[1], lat[0], lon[0]));
       Log.e(TAG, "distance " +Coordinates. distFrom(lat[1], lon[1], lat[0], lon[0]));


       long r = Math.abs(Math.round(2.0 * al * Math.tan(f / 2.0)));
       Log.e(TAG, "r " + r);
       //ma.setResultToToast("r " + r);


       int x = (int) ((1000 * Coordinates.distFrom(lat[1], lon[1], lat[1], lon[0])) / r);
       Log.e(TAG, "distance x " + Coordinates.distFrom(lat[1], lon[1], lat[1], lon[0]));
       int y = (int) ((1000 * Coordinates.distFrom(lat[1], lon[1], lat[0], lon[1])) / r);
       Log.e(TAG, "distance y " + Coordinates.distFrom(lat[1], lon[1], lat[0], lon[1]));
       Log.e(TAG, "y " + y);
       Log.e(TAG,"Corner 1 " + new Coordinates(lon[1], lat[1]).toString());
       Log.e(TAG,"Corner 1 " + new Coordinates(lon[0], lat[1]).toString());

       Log.e(TAG, "y " + y);

       if (y > 1)
           y = y - 1;

       if (x == 0 || y == 0) {



           return null;

       }
       Log.e(TAG, "x " + x);
       //ma.setResultToToast("x " + x);
       Coordinates[] points = new Coordinates[(2 * y)];
       Coordinates[][] midpoints = new Coordinates[2][x];






       //points_2line(new Coordinates( lon[0],lat[1]),new Coordinates( lon[1],lat[1]),r,x,true);
       //points_2line(new Coordinates(lon[1], lat[1]), new Coordinates(lon[0], lat[1]), r, y, false);
       //points_2line(new Coordinates( lon[0],lat[0]),new Coordinates( lon[1],lat[0]),r,x,true);
       /// points_2line(new Coordinates(lon[0], lat[0]), new Coordinates(lon[1], lat[0]), r, y, false);

       Coordinates t1D[]=new Coordinates[1];
       Coordinates t2D[][]=new Coordinates[1][1];
       int  sy=get_gps_distance(lon[0], lat[0],Coordinates. bearing(lon[0], lat[0], lon[0], lat[1]), y, r, 1, 2,points,t2D,0);
       get_gps_distance(lon[0], lat[1],Coordinates. bearing(lon[0], lat[1], lon[1], lat[1]), x, r, 1, 1,t1D,midpoints,0);//create points for mid points

       get_gps_distance(lon[1], lat[0], Coordinates.bearing(lon[1], lat[0], lon[1], lat[1]), y, r, 0, 2,points,t2D,sy);
       get_gps_distance(lon[0], lat[0],Coordinates. bearing(lon[0], lat[0], lon[1], lat[0]), x, r, 0, 1,t1D,midpoints,0);//create points for mid points


       Log.e(TAG, "extend distance before  " + 1000*Coordinates.distFrom(midpoints[1][midpoints[1].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]));

       while (1000*Coordinates.distFrom(midpoints[1][midpoints[0].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]) > r) {
           Log.e(TAG, "extend distance   " + 1000*Coordinates.distFrom(midpoints[1][midpoints[1].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]));
           int l = (int) ((1000 * c.distFrom(midpoints[1][midpoints[0].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1])) / r);
           Coordinates[][] emid = new Coordinates[2][midpoints[0].length + l];
           emid = extend_mikos(r, new Coordinates(lon[1], lat[0]), new Coordinates(lon[1], lat[1]), midpoints);
           x = emid[0].length;
           midpoints = new Coordinates[2][x];
           midpoints=c.twoDimensionalArrayClone(emid);

       }
       int em = midpoints[0].length;
       Log.e(TAG, "extend after   " + 1000*Coordinates.distFrom(midpoints[1][midpoints[1].length - 1].lon, midpoints[1][midpoints[0].length - 1].lat, lat[1], lon[1]));

       if (points[points.length-1].lon>lat[1]){
           points=c.decrease_y(new Coordinates(lon[0], lat[1]), new Coordinates(lon[1], lat[1]),points);
           y=points.length/2;
       }


       Coordinates drone_pointS[] = new Coordinates[points.length + (midpoints[0].length * midpoints.length) + 4];

       Log.e(TAG, "length   " + drone_pointS.length);


       int m=0,dps=1;

       drone_pointS[0]=new Coordinates(lon[0], lat[0]);

       for (int i = 0; i < midpoints[0].length; i++) {


           drone_pointS[dps] = midpoints[0][i];
           dps++;

       }

       drone_pointS[dps++] = new Coordinates(lon[1], lat[0]);


       for (int i = points.length / 2; i < points.length; i++) {


           drone_pointS[dps]=points[i];
           dps++;

       }
       drone_pointS[dps++] = new Coordinates(lon[1], lat[1]);

       for (int i = midpoints[0].length - 1; i >= 0; i--) {


           drone_pointS[dps] = midpoints[1][i];
           dps++;
       }

       drone_pointS[dps++] = new Coordinates(lon[0], lat[1]);


       for (int i = points.length / 2 - 1; i >= 0; i--) {


           drone_pointS[dps]=points[i];
           dps++;
       }
       return  drone_pointS;
   }

}

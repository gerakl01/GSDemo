package googlemap.gsdemo.dji.com.gsdemo;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

/**
 * Created by george on 28/03/2017.
 */

public class ReadWrite {

    protected static void write(String Directory,boolean initial,Coordinates co,int speed,int height)  {
        File dir = new File (Directory);

        File file;

        file = new File(dir, "data.txt");


        try {
            FileOutputStream fop=new FileOutputStream(file,true);
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);

            if (initial)
                bw.write("LatLong                    |      Height    |     U \n");
            else{
                bw.write(co.toString()+"             |    "+height+"  |"+speed+"  \n");
            }
            bw.flush();
            bw.close();
        }
        catch (Exception e) {
            Log.e("ERRR", "Could not create file",e);
        }


    }

}

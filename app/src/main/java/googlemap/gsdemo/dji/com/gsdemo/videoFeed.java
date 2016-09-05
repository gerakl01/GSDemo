package googlemap.gsdemo.dji.com.gsdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by ckyrkou on 27/11/2015.
 */

public class videoFeed extends SurfaceView {

    private Bitmap bmp;
    private SurfaceHolder holder;

    public videoFeed(Context context, Bitmap bmap) {
        super(context);
        holder = getHolder();
        bmp = bmap;
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas c = holder.lockCanvas(null);
                onDraw(c);
                holder.unlockCanvasAndPost(c);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bmp, 10, 10, null);
    }
}
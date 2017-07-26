package trente.asia.thankscard.services.posted.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.FloatProperty;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

/**
 * Created by tien on 7/26/2017.
 */

public class ImageShow extends AppCompatImageView {
    private float frameWidth;
    private float frameHeight;
    private float translateX;
    private float translateY;
    private float scaleRatio;
    private float width;
    private float height;
    private float[] centerPoint = new float[2];

    private Matrix matrix = new Matrix();
    private Paint paint = new Paint();
    private Bitmap bitmap;

    public ImageShow(Context context, AttributeSet attrs) {
        super(context, attrs);
        PreferencesSystemUtil preference = new PreferencesSystemUtil(context);
        frameWidth = Float.valueOf(preference.get(TcConst.PREF_FRAME_WIDTH));
        frameHeight = Float.valueOf(preference.get(TcConst.PREF_FRAME_HEIGHT));
    }

    public void restoreImage(String imagePath, final float locationX, final float locationY, final float scale) {
        Picasso.with(getContext()).load(BuildConfig.HOST + imagePath).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmapLoad, Picasso.LoadedFrom from) {
                bitmap = bitmapLoad;
                width = bitmap.getWidth();
                height = bitmap.getHeight();
                translateX = locationX * frameWidth - width / 2;
                translateY = locationY * frameHeight - height / 2;
                scaleRatio = scale * frameHeight / height;
                invalidate();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) {
            return;
        }
        matrix.reset();
        centerPoint[0] = width / 2;
        centerPoint[1] = height / 2;
        matrix.postTranslate(translateX, translateY);
        matrix.mapPoints(centerPoint);
        matrix.postScale(scaleRatio, scaleRatio, centerPoint[0], centerPoint[1]);

        canvas.drawBitmap(bitmap, matrix, paint);
    }
}

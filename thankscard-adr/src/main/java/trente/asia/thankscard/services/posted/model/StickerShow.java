package trente.asia.thankscard.services.posted.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import trente.asia.thankscard.BuildConfig;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

/**
 * Created by tien on 7/26/2017.
 */

public class StickerShow extends AppCompatImageView{

	private float	frameWidth;
	private float	frameHeight;
    private float width;
    private float height;
    private float scale;
    private float rotation;
    private float translateX;
    private float translateY;

	private Bitmap	bitmap;

    private float[] centerPoint = new float[2];
    private Matrix matrix = new Matrix();
    private Paint paint = new Paint();

	public StickerShow(Context context){
		super(context);
		PreferencesSystemUtil preference = new PreferencesSystemUtil(context);
		frameWidth = Float.valueOf(preference.get(TcConst.PREF_FRAME_WIDTH));
		frameHeight = Float.valueOf(preference.get(TcConst.PREF_FRAME_HEIGHT));
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public void restoreSticker(String stickerPath, final float locationX, final float locationY, final float scaleImage, final float degree){
        Picasso.with(getContext()).load(BuildConfig.HOST + stickerPath).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmapSticker, Picasso.LoadedFrom from) {
                bitmap = bitmapSticker;
                width = bitmap.getWidth();
                height = bitmap.getHeight();
                translateX = locationX * frameWidth - width / 2;
                translateY = locationY * frameHeight - height / 2;
                scale = scaleImage * frameHeight / height;
                rotation = degree;
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
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
        if (bitmap == null) {
            return;
        }
        matrix.reset();
        centerPoint[0] = width / 2;
        centerPoint[1] = height / 2;
        matrix.postTranslate(translateX, translateY);
        matrix.mapPoints(centerPoint);
        matrix.postScale(scale, scale, centerPoint[0], centerPoint[1]);
        matrix.postRotate(-rotation, centerPoint[0], centerPoint[1]);

        canvas.drawBitmap(bitmap, matrix, paint);
	}
}

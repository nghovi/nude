package trente.asia.thankscard.services.posted.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;

import trente.asia.thankscard.R;
import trente.asia.thankscard.commons.defines.TcConst;
import trente.asia.welfare.adr.pref.PreferencesSystemUtil;

/**
 * Created by tien on 7/17/2017.
 */

public class PhotoViewPost extends AppCompatImageView{

	private Matrix	matrix		= new Matrix();
	private Paint	paint		= new Paint();
	private float	x			= 0;
	private float	y			= 0;
	private Bitmap	bitmap;
	private int		width, height;
	private float[]	centerPoint	= new float[2];
	private float	scale		= 1f;
	private float	saveScale	= 1f;
	private float	frameWidth;
	private float	frameHeight;

	public PhotoViewPost(Context context){
		super(context);
	}

	public PhotoViewPost(Context context, @Nullable AttributeSet attrs){
		super(context, attrs);
		PreferencesSystemUtil preference = new PreferencesSystemUtil(context);
		this.frameWidth = Float.valueOf(preference.get(TcConst.PREF_FRAME_WIDTH));
		this.frameHeight = Float.valueOf(preference.get(TcConst.PREF_FRAME_HEIGHT));
	}

	public String getPhotoLocationX(){
		return String.valueOf(centerPoint[0] / frameWidth);
	}

	public String getPhotoLocationY(){
		return String.valueOf(centerPoint[1] / frameHeight);
	}

	public String getPhotoScale(){
		return String.valueOf(height * scale / frameHeight);
	}

	public void setImage(String imagePath, String position){
		bitmap = BitmapFactory.decodeFile(imagePath);
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		x = frameWidth / 4 - width / 2;
		if (TcConst.POSITION_CENTER.equals(position) || TcConst.POSITION_LEFT.equals(position)) {
			y = frameHeight / 2 - height / 2;
		} else if (TcConst.POSITION_TOP.equals(position)){
			y = 50;
		} else if (TcConst.POSITION_BOTTOM.equals(position)){
			y = frameHeight - height - 50;
		}
		scale = 1f;
		saveScale = 1f;
		invalidate();
	}

	public void clearImage(){
		bitmap = null;
		invalidate();
	}

	public boolean hasImage(){
		return bitmap != null;
	}

	public void move(float moveX, float moveY){
		if(bitmap == null){
			return;
		}
		x += moveX;
		y += moveY;
		invalidate();
	}

	public void scale(float scale){
		if(bitmap == null){
			return;
		}
		this.scale = saveScale * scale;
		invalidate();
	}

	public void endScale(){
		if(bitmap == null){
			return;
		}
		saveScale = scale;
	}

	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(bitmap == null){
			canvas.drawARGB(255, 255, 255, 255);
			return;
		}
		matrix.reset();
		centerPoint[0] = width / 2;
		centerPoint[1] = height / 2;

		matrix.setTranslate(x, y);

		matrix.mapPoints(centerPoint);

		matrix.postScale(scale, scale, centerPoint[0], centerPoint[1]);

		canvas.drawBitmap(bitmap, matrix, paint);
	}

	private void log(String msg){
		Log.e("ImageShow", msg);
	}
}

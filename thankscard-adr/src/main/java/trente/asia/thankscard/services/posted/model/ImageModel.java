package trente.asia.thankscard.services.posted.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;

import trente.asia.thankscard.R;

/**
 * Created by tien on 7/17/2017.
 */

public class ImageModel extends AppCompatImageView{

	private Matrix	matrix		= new Matrix();
	private Paint	paint		= new Paint();
	private float	x			= 0;
	private float	y			= 0;
	private Bitmap	bitmap;
	private int		width, height;
	private float[]	centerPoint	= new float[2];
	private float	scale		= 1f;
	private float	saveScale	= 1f;

	public ImageModel(Context context){
		super(context);
	}

	public ImageModel(Context context, @Nullable AttributeSet attrs){
		super(context, attrs);
	}

	public void setImage(String imagePath){
		bitmap = BitmapFactory.decodeFile(imagePath);
		width = bitmap.getWidth();
		height = bitmap.getHeight();
		invalidate();
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

package trente.asia.welfare.adr.utils;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.welfare.adr.models.BitmapModel;
import trente.asia.welfare.adr.view.SelectableRoundedImageView;

/**
 * Load image from url
 * 
 * @author TuVD
 */
public class WfPicassoHelper{

	/**
	 * Load image from url with progressBar
	 *
	 * @param context is Context wrapper
	 * @param imageUrl is url of image
	 * @param imageView which contain photos
	 * @param pgrLoading is progress bar
	 */
	public static void loadImage(Context context, String imageUrl, final ImageView imageView, final ProgressBar pgrLoading){

		if(context == null || CCStringUtil.isEmpty(imageUrl) || imageView == null){
			return;
		}

		// fix bug load wrong image into ImageView inside listview items
		WfPicassoHelper.cancelLoadImage(context, imageView);

		if(pgrLoading != null) pgrLoading.setVisibility(View.VISIBLE);

		Picasso.with(context).load(imageUrl).into(imageView, new Callback.EmptyCallback() {

			@Override
			public void onSuccess(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
			}

			@Override
			public void onError(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
			}
		});
	}


	public static void loadImageFit(Context context, String imageUrl, final ImageView imageView, final ProgressBar pgrLoading){

		if(context == null || CCStringUtil.isEmpty(imageUrl) || imageView == null){
			return;
		}

		// fix bug load wrong image into ImageView inside listview items
		WfPicassoHelper.cancelLoadImage(context, imageView);

		if(pgrLoading != null) pgrLoading.setVisibility(View.VISIBLE);

		Picasso.with(context).load(imageUrl).fit().into(imageView, new Callback.EmptyCallback() {

			@Override
			public void onSuccess(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
			}

			@Override
			public void onError(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * Load image from url with progressBar
	 *
	 * @param context is Context wrapper
	 * @param imageUrl is url of image
	 * @param imageView which contain photos
	 * @param pgrLoading is progress bar
	 */
	public static void loadImage(Context context, String imageUrl, final ImageView imageView, final ProgressBar pgrLoading, final BitmapModel bitmapModel){

		if(context == null || CCStringUtil.isEmpty(imageUrl) || imageView == null){
			return;
		}

		if(pgrLoading != null) pgrLoading.setVisibility(View.VISIBLE);
		bitmapModel.started = true;

		Picasso.with(context).load(imageUrl).into(imageView, new Callback.EmptyCallback() {

			@Override
			public void onSuccess(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
                if(imageView instanceof SelectableRoundedImageView){
                    bitmapModel.bitmap = ((SelectableRoundedImageView.SelectableRoundedCornerDrawable)imageView.getDrawable()).getSourceBitmap();
                }else{
                    bitmapModel.bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
                }
			}

			@Override
			public void onError(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
			}
		});
	}

	public static void loadImageFit(Context context, String imageUrl, final ImageView imageView, final ProgressBar pgrLoading, final BitmapModel bitmapModel){

		if(context == null || CCStringUtil.isEmpty(imageUrl) || imageView == null){
			return;
		}

		if(pgrLoading != null) pgrLoading.setVisibility(View.VISIBLE);
		bitmapModel.started = true;

		Picasso.with(context).load(imageUrl).fit().into(imageView, new Callback.EmptyCallback() {

			@Override
			public void onSuccess(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
				if(imageView instanceof SelectableRoundedImageView){
					bitmapModel.bitmap = ((SelectableRoundedImageView.SelectableRoundedCornerDrawable)imageView.getDrawable()).getSourceBitmap();
				}else{
					bitmapModel.bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
				}
			}

			@Override
			public void onError(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
			}
		});
	}

	public static void cancelLoadImage(Context context, ImageView imageView){
		Picasso.with(context).cancelRequest(imageView);
	}

	/**
	 * Load image from url with progressBar
	 *
	 * @param context is Context wrapper
	 * @param imageUrl is url of image
	 * @param imageView which contain photos
	 * @param pgrLoading is progress bar
	 */
	public static void loadImageNoCache(Context context, String imageUrl, final ImageView imageView, final ProgressBar pgrLoading){

		if(context == null || CCStringUtil.isEmpty(imageUrl) || imageView == null){
			return;
		}

		if(pgrLoading != null) pgrLoading.setVisibility(View.VISIBLE);

		Picasso.with(context).load(imageUrl).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView, new Callback.EmptyCallback() {

			@Override
			public void onSuccess(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
			}

			@Override
			public void onError(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * Load image from url with progressBar and resize image
	 *
	 * @param context
	 * @param imageUrl
	 * @param imageView
	 * @param width
	 * @param height
	 * @param pgrLoading
	 */
	public static void loadImageWithSize(Context context, String imageUrl, final ImageView imageView, int width, int height, final ProgressBar pgrLoading){

		if(context == null || CCStringUtil.isEmpty(imageUrl) || imageView == null){
			return;
		}

		if(pgrLoading != null) pgrLoading.setVisibility(View.VISIBLE);

		Picasso.with(context).load(imageUrl).resize(width, height).into(imageView, new Callback.EmptyCallback() {

			@Override
			public void onSuccess(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
			}

			@Override
			public void onError(){
				if(pgrLoading != null) pgrLoading.setVisibility(View.GONE);
			}
		});
	}

	public static void loadImage2(Context context, String host, ImageView imgView, String imagePath){
		if(!CCStringUtil.isEmpty(imagePath)){
			loadImage(context, host + imagePath, imgView, null);
		}
	}

	public static void loadImageWithDefaultIcon(Context context, String host, ImageView imgView, String imagePath, int defaultIconId){
		if(!CCStringUtil.isEmpty(imagePath)){
			loadImage(context, host + imagePath, imgView, null);
		}else{
			imgView.setImageResource(defaultIconId);
		}
	}
}

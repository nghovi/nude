package trente.asia.android.view;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import trente.asia.android.R;
import trente.asia.android.util.CsMsgUtil;

/**
 * Created by TrungND on 16/09/2014.
 */
public class ChiaseImageView extends ImageView{

	public File		srcFile;

	public String	filePath;

	public boolean	existData;

	public String	imageId;

	public ChiaseImageView(Context context){
		super(context);
	}

	public ChiaseImageView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.viewControl, R.attr.filePath};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CsMsgUtil.convertArray2List(attrsResourceIdArray);
		filePath = t.getString(list.indexOf(R.attr.filePath));
	}

	public ChiaseImageView(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	public File getSrcFile(){
		return srcFile;
	}

	public void setSrcFile(File srcFile){
		this.srcFile = srcFile;
	}

	public String getFilePath(){
		return filePath;
	}

	public void setFilePath(String filePath){
		this.filePath = filePath;
	}

	@Override
	public void setEnabled(boolean enabled){
		super.setEnabled(enabled);
		if(enabled){
			this.setAlpha((float)1);
		}else{
			this.setAlpha((float)0.5);
		}
	}
}

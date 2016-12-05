package trente.asia.android.view;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import trente.asia.android.R;
import trente.asia.android.util.CAMsgUtil;

/**
 * Created by TrungND on 16/09/2014.
 */
public class ChiaseImageView extends ImageView{

	public String	viewControl;

	public File		srcFile;

	public String filePath;

	public ChiaseImageView(Context context){
		super(context);
	}

	public ChiaseImageView(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// get view control value
		int attrsResourceIdArray[] = {R.attr.viewControl, R.attr.filePath};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CAMsgUtil.convertArray2List(attrsResourceIdArray);
		viewControl = t.getString(list.indexOf(R.attr.viewControl));
		filePath = t.getString(list.indexOf(R.attr.filePath));
	}

	public ChiaseImageView(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

	public String getViewControl(){
		return viewControl;
	}

	public void setViewControl(String viewControl){
		this.viewControl = viewControl;
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

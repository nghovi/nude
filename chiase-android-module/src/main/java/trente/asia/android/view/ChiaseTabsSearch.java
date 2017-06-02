package trente.asia.android.view;

import java.util.Map;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import trente.asia.android.R;

/**
 * Created by TrungND on 16/09/2014.
 */
public class ChiaseTabsSearch{

	private Context				context;

	private ViewGroup			viewGroup;

	private Map<String, String>	textMap;

	public ChiaseTabsSearch(Context context, ViewGroup viewGroup, Map<String, String> textMap){
		this.context = context;
		this.viewGroup = viewGroup;
		this.textMap = textMap;

		initTabsSearch();
		setBackgroundColorTextView();
	}

	private void initTabsSearch(){
		boolean isFirst = true;
		for(String key : textMap.keySet()){
			ChiaseTextView textView = new ChiaseTextView(context);
			textView.setValue(key);
			textView.setText(textMap.get(key));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			params.setMargins(10, 10, 10, 10);
			textView.setLayoutParams(params);
			textView.setPadding(3, 3, 3, 3);
			textView.setGravity(Gravity.RIGHT);
			if(isFirst){
				textView.setIsPressed(true);
				isFirst = false;
			}
			viewGroup.addView(textView);
		}
	}

	public void removePressedState(){
		for(int i = 0; i < viewGroup.getChildCount(); i++){
			final View view = viewGroup.getChildAt(i);
			if(view instanceof ChiaseTextView){
				((ChiaseTextView)view).setIsPressed(false);
			}
		}
	}

	public void setBackgroundColorTextView(){
		for(int i = 0; i < viewGroup.getChildCount(); i++){
			final View view = viewGroup.getChildAt(i);
			if(view instanceof TextView){
				view.setBackgroundColor(context.getResources().getColor(R.color.chiase_filechooser_txt_normal));
				if(view instanceof ChiaseTextView && ((ChiaseTextView)view).getIsPressed()){
					view.setBackgroundColor(context.getResources().getColor(R.color.chiase_filechooser_txt_actived));
				}
			}
		}
	}

	public String getActivedTextView(){
		for(int i = 0; i < viewGroup.getChildCount(); i++){
			final View view = viewGroup.getChildAt(i);
			if(view instanceof ChiaseTextView){
				if(((ChiaseTextView)view).getIsPressed()){
					return ((ChiaseTextView)view).getValue();
				}
			}
		}
		return "";
	}
}

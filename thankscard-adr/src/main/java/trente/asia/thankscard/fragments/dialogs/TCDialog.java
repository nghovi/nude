package trente.asia.thankscard.fragments.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by viet on 3/16/2016.
 */
public abstract class TCDialog extends DialogFragment{

	protected View.OnClickListener	btnPositiveListener;
	protected View.OnClickListener	btnNegativeListener;

	public void setListeners(View.OnClickListener btnPositiveListener, View.OnClickListener btnNegativeListener){
		this.btnPositiveListener = btnPositiveListener;
		this.btnNegativeListener = btnNegativeListener;
	}

	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}

	public abstract int getDialogLayoutId();

	/**
	 * living on the fringes of the society
	 * they throgh their close
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		// getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(getDialogLayoutId(), container);
		buildDialogLayout(view);
		return view;
	}

	@Override
	public void show(FragmentManager manager, String tag){
		if(!isAdded()){// prevent exception when double click on button to show dialog
			setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Dialog);
			super.show(manager, tag);
		}
	}

	public abstract void buildDialogLayout(View rootView);
}

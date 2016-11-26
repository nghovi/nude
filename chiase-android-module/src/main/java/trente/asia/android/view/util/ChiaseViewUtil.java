package trente.asia.android.view.util;

import android.widget.EditText;

import trente.asia.android.view.ChiaseEditText;
import trente.asia.android.view.ChiaseTextView;

/**
 * Created by takyas on 17/10/15.
 */
public class ChiaseViewUtil{

	public static boolean isEmpty(ChiaseEditText text){
		if(text.getText().toString().trim().length() == 0){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isEmpty(ChiaseTextView text){
		if(text.getText().toString().trim().length() == 0){
			return true;
		}else{
			return false;
		}
	}

	public static boolean isEmpty(EditText text){
		if(text.getText().toString().trim().length() == 0){
			return true;
		}else{
			return false;
		}
	}

}

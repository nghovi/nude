package nguyenhoangviet.vpcorp.android.view.util;

import android.widget.EditText;

import nguyenhoangviet.vpcorp.android.view.ChiaseEditText;
import nguyenhoangviet.vpcorp.android.view.ChiaseTextView;

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

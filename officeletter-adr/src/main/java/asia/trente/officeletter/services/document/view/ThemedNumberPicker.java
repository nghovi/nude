package asia.trente.officeletter.services.document.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.widget.NumberPicker;

import asia.trente.officeletter.R;


/**
 * Created by viet on 2/19/2016.
 */
public class ThemedNumberPicker extends NumberPicker{

	public ThemedNumberPicker(Context context){
		this(context, null);
	}

	public ThemedNumberPicker(Context context, AttributeSet attrs){
		// wrap the current context in the style we defined before
		super(new ContextThemeWrapper(context, R.style.NumberPickerTextColorStyle), attrs);
	}
}

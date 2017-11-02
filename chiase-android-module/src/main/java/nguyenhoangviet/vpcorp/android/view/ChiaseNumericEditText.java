package nguyenhoangviet.vpcorp.android.view;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

import asia.chiase.core.define.CCConst;
import asia.chiase.core.util.CCNumberUtil;
import asia.chiase.core.util.CCStringUtil;
import nguyenhoangviet.vpcorp.android.R;
import nguyenhoangviet.vpcorp.android.util.CsMsgUtil;

/**
 * Created by VietNH on 2015/07/17.
 */
public class ChiaseNumericEditText extends ChiaseEditText{

	private static final int MAX_LENGHT_FORMAT_VALUE = 7;

	public String mUnitName; // $, Day, ...

	private String mFormatString;

	private TextWatcher mTextWatcher;

    private NumberFormat formatter = NumberFormat.getInstance(Locale.JAPAN);

	public void setmTextWatcher(TextWatcher mTextWatcher){
		this.mTextWatcher = mTextWatcher;
	}

	//
	// private DecimalFormat mFormatter;
	//
	// private String mStr;

	public ChiaseNumericEditText(Context context){
		super(context);
	}

	public ChiaseNumericEditText(Context context, AttributeSet attributeSet){
		super(context, attributeSet);

		// Enter only number for this edittext.
		this.setInputType(InputType.TYPE_CLASS_NUMBER);

		/* max value 9999 */
		this.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGHT_FORMAT_VALUE)});
		// get view control value
		int attrsResourceIdArray[] = {R.attr.unitName, R.attr.formatString};
		TypedArray t = context.obtainStyledAttributes(attributeSet, attrsResourceIdArray);

		List<Integer> list = CsMsgUtil.convertArray2List(attrsResourceIdArray);
		mUnitName = CCStringUtil.toString(t.getString(list.indexOf(R.attr.unitName)));
		mFormatString = t.getString(list.indexOf(R.attr.formatString));

		// mFormatter = new DecimalFormat(mFormatString);

		this.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){
			}

			@Override
			public void afterTextChanged(Editable s){
				String mStr = s.toString();
				int numberEndCharacter = mStr.length() - getSelectionStart();
				mStr = removeFormat(mStr);
				removeTextChangedListener(this);
				if(!CCStringUtil.isEmpty(mStr)) {
					mStr = format(mStr);
					setText(mStr);
					if(!CCStringUtil.isEmpty(mStr)) {
						if(numberEndCharacter <= mUnitName.length()) {
							setSelection(mStr.length() - mUnitName.length());
						}else{
							if(mStr.length() < numberEndCharacter) {
								setSelection(0);
							}else{
								setSelection(mStr.length() - numberEndCharacter);
							}
						}
					}
				}else{
					setText("");
				}
				addTextChangedListener(this);
				if(mTextWatcher != null) {
					mTextWatcher.afterTextChanged(s);
				}
			}
		});
	}

	public String removeFormat(String text){
		if(!CCStringUtil.isEmpty(text)) {
			text = text.replace(mUnitName, "");
			text = text.replaceAll(",", "");
			return text;
		}
		return "";
	}

	public String format(String text){
		if(!CCStringUtil.isEmpty(text)) {
			text = formatter.format(CCNumberUtil.toInteger(text));
//			if(CCConst.NONE.equals(text)) {
//				return "";
//			}
			text = text + mUnitName;
			return text;
		}
		return "";
	}

	public String getNumericString(){
		String text = CCStringUtil.toString(getText());
		if(!CCStringUtil.isEmpty(text)) {
			return removeFormat(text);
		}
		return "";
	}

	public String getNumericStringWithDefault(){
		String text = CCStringUtil.toString(getText());
		if(!CCStringUtil.isEmpty(text)) {
			return removeFormat(text);
		}
		return CCConst.NONE;
	}

	public ChiaseNumericEditText(Context context, AttributeSet attributeSet, int defStyle){
		super(context, attributeSet, defStyle);
	}

}

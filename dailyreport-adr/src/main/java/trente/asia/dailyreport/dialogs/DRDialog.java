package trente.asia.dailyreport.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.android.view.ChiaseDialog;
import trente.asia.dailyreport.R;
import trente.asia.dailyreport.services.report.model.ReportModel;
import trente.asia.dailyreport.services.report.view.DRLikeListAdapter;
import trente.asia.welfare.adr.utils.WelfareUtil;
import trente.asia.welfare.adr.view.WfSpinner;

/**
 * QkChiaseDialog
 *
 * @author TrungND
 */
public class DRDialog extends ChiaseDialog{

	private static final int	MAX_CHARACTER_COUNT	= 9999;
	private static final int	MAX_CHARACTER_SALE	= 9999999;

	private Context				mContext;

	public DRDialog(Context context){
		super(context);
		this.mContext = context;
	}

	public DRDialog(Context context, int theme){
		super(context, theme);
		this.mContext = context;
	}

	public interface OnSendCommentListener{

		public void onClickSend(String comment);
	}

	public static TextWatcher getAmountTextWatcher(final EditText editText){
		TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){

			}

			@Override
			public void afterTextChanged(Editable editable){
				editText.removeTextChangedListener(this);
				String reFormattedAmount = WelfareUtil.formatAmount(WelfareUtil.getAmount(editable.toString()));
				editText.setText(reFormattedAmount);
				editText.setSelection(reFormattedAmount.length());
				editText.addTextChangedListener(this);
			}
		};
		return textWatcher;
	}

	public class InputFilterMinMax implements InputFilter{

		private int min, max;

		public InputFilterMinMax(int min, int max){
			this.min = min;
			this.max = max;
		}

		public InputFilterMinMax(String min, String max){
			this.min = Integer.parseInt(min);
			this.max = Integer.parseInt(max);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend){
			try{
				int input = Integer.parseInt(WelfareUtil.getAmount(dest.toString() + source.toString()));
				if(isInRange(min, max, input)) return null;
			}catch(NumberFormatException nfe){
			}
			return "";
		}

		private boolean isInRange(int a, int b, int c){
			return b > a ? c >= a && c <= b : c >= b && c <= a;
		}
	}

	// http://stackoverflow.com/questions/5357455/limit-decimal-places-in-android-edittext
	public class DecimalDigitsInputFilter implements InputFilter{

		Pattern mPattern;

		public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero){
			mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero) + "}+(" + "(\\.[0-9]{0," + (digitsAfterZero) + "})?)||(\\.)?");
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend){
			Matcher matcher = mPattern.matcher((dest.subSequence(0, dstart).toString() + source.subSequence(start, end) + dest.subSequence(dend, dest.length())));
			if(!matcher.matches()) return "";
			return null;
		}

	}

	public DRDialog setDialogConfirm(String strMessage, String strPositive, String strNegative, final View.OnClickListener listenerPositive, final View.OnClickListener listenerNegative){
		this.setContentView(R.layout.dialog_confirm);
		if(!CCStringUtil.isEmpty(strMessage)){
			TextView txtMessage = (TextView)this.findViewById(R.id.txt_id_msg);
			txtMessage.setText(strMessage);
		}

		TextView txtPositive = (TextView)this.findViewById(R.id.txt_id_positive);
		if(!CCStringUtil.isEmpty(strPositive)){
			txtPositive.setText(strPositive);
		}
		txtPositive.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dismiss();
				if(listenerPositive != null){
					listenerPositive.onClick(view);
				}
			}
		});

		TextView txtNegative = (TextView)this.findViewById(R.id.txt_id_negative);
		txtNegative.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v){
				dismiss();
				listenerNegative.onClick(v);
			}
		});
		if(!CCStringUtil.isEmpty(strNegative)){
			txtNegative.setText(strNegative);
		}else{
			txtNegative.setVisibility(View.GONE);
		}

		return this;
	}

	public DRDialog setLikersDialog(Activity activity, ReportModel reportModel, String title, boolean isLikers){
		this.setContentView(R.layout.dialog_likers);
		TextView txtTitle = (TextView)this.findViewById(R.id.dialog_likers_title);
		txtTitle.setText(title);
		ImageView imgClose = (ImageView)this.findViewById(R.id.dialog_liker_close);
		imgClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view){
				dismiss();
			}
		});
		ListView lstFollowers = (ListView)this.findViewById(R.id.fragment_followers_list);
		DRLikeListAdapter drLikeListAdapter = new DRLikeListAdapter(activity, R.layout.item_user, reportModel, isLikers);
		lstFollowers.setAdapter(drLikeListAdapter);
		return this;
	}

	@Override
	public void show(){
		super.show();
	}

	@Override
	public void dismiss(){
		super.dismiss();
	}
}

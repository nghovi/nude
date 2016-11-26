package trente.asia.android.activity;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;

/**
 * Created by viet on 1/8/2016.
 */
public class ChiaseParseError extends ParseError{

	public String mStringHtml;

	public ChiaseParseError(){
	}

	public ChiaseParseError(NetworkResponse networkResponse){
		super(networkResponse);
	}

	public ChiaseParseError(Throwable cause){
		super(cause);
	}

	public ChiaseParseError(Throwable cause, String html){
		super(cause);
		this.mStringHtml = html;
	}

	public String getmStringHtml(){
		return mStringHtml;
	}
}

package trente.asia.android.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.content.Context;

/**
 * Created by takyas on 28/9/14.
 */
public class CustomUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

	private Context							mContext;
	private Thread.UncaughtExceptionHandler	mDefaultUncaughtExceptionHandler;

	public CustomUncaughtExceptionHandler(Context context){
		mContext = context;

		// デフォルト例外ハンドラを保持する。
		mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex){

		StringWriter stringWriter = new StringWriter();
		ex.printStackTrace(new PrintWriter(stringWriter));
		String stackTrace = stringWriter.toString();

		// Log.e(stackTrace);

		// // スタックトレースを SharedPreferences に保存します。
		// SharedPreferences preferences = mContext.getSharedPreferences(
		// MainActivity.PREF_NAME_SAMPLE, Context.MODE_PRIVATE);
		// preferences.edit().putString(MainActivity.EX_STACK_TRACE, stackTrace)
		// .commit();

		// デフォルト例外ハンドラを実行し、強制終了します。
		mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);

	}
}

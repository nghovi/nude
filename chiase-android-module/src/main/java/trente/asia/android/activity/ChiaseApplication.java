package trente.asia.android.activity;

import android.content.Context;

/**
 * Created by TrungND-PC on 1/30/2015.
 */
public class ChiaseApplication extends com.activeandroid.app.Application{

	@Override
	protected void attachBaseContext(Context base){
		super.attachBaseContext(base);
		// MultiDex.install(this);
	}

}

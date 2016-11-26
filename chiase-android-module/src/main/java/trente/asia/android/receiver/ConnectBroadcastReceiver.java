package trente.asia.android.receiver;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by TrungND on 10/21/2014.
 */
public class ConnectBroadcastReceiver extends BroadcastReceiver{

	protected List<NetworkStateReceiverListener>	listeners;

	protected Boolean								connected;

	public ConnectBroadcastReceiver(){
		listeners = new ArrayList<NetworkStateReceiverListener>();
		connected = null;
	}

	@Override
	public void onReceive(Context context, Intent intent){
		if(intent == null || intent.getExtras() == null){
			return;
		}

		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = manager.getActiveNetworkInfo();

		if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED){
			connected = true;
		}else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)){
			connected = false;
		}

		notifyStateToAll();
	}

	private void notifyStateToAll(){
		for(NetworkStateReceiverListener listener : listeners){
			notifyState(listener);
		}
	}

	private void notifyState(NetworkStateReceiverListener listener){
		if(connected == null || listener == null){
			return;
		}

		if(connected == true){
			listener.networkAvailable();
		}else{
			listener.networkUnavailable();
		}

	}

	public void addListener(NetworkStateReceiverListener l){
		listeners.add(l);
		notifyState(l);
	}

	public void removeListener(NetworkStateReceiverListener l){
		listeners.remove(l);
	}
}

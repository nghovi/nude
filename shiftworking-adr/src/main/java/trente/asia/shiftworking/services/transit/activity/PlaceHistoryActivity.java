package trente.asia.shiftworking.services.transit.activity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.welfare.adr.activity.WelfareActivity;

/**
 * PlaceHistoryActivity
 *
 * @author TrungND
 */

public class PlaceHistoryActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);

        Bundle bundle = getIntent().getExtras();
        List<String> lstHistory = (ArrayList<String>)bundle.getSerializable(SwConst.KEY_HISTORY_LIST);
        String placeHistory = bundle.getString(SwConst.KEY_HISTORY_NAME);
        PlaceHistoryFragment fragment = new PlaceHistoryFragment();
        fragment.setLstHistory(lstHistory);
        fragment.setPlaceHistory(placeHistory);
		addFragment(fragment);
	}
}

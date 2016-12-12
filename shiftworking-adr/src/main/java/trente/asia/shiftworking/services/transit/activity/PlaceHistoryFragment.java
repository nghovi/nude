package trente.asia.shiftworking.services.transit.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import asia.chiase.core.util.CCStringUtil;
import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.defines.SwConst;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.services.transit.view.PlaceHistoryAdapter;

/**
 * PlaceHistoryFragment
 *
 * @author TrungND
 */
public class PlaceHistoryFragment extends AbstractSwFragment{

	private List<String>		lstHistory;
	private String				placeHistory;

	private EditText			edtPlace;
	private ListView			lsvPlace;
	private PlaceHistoryAdapter	adapter;

	public void setLstHistory(List<String> lstHistory){
		this.lstHistory = lstHistory;
	}

    public void setPlaceHistory(String placeHistory) {
        this.placeHistory = placeHistory;
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		if(mRootView == null){
			mRootView = inflater.inflate(R.layout.fragment_place_history, container, false);
		}
		return mRootView;
	}

	@Override
	public void initView(){
		// super.initView();
		super.initHeader(R.drawable.sw_back_white, "Place entry", null);
		lnrContentId = null;

		edtPlace = (EditText)getView().findViewById(R.id.edt_id_place);
		lsvPlace = (ListView)getView().findViewById(R.id.lsv_id_place);
		adapter = new PlaceHistoryAdapter(activity, lstHistory);
		lsvPlace.setAdapter(adapter);

        if(!CCStringUtil.isEmpty(placeHistory)){
            edtPlace.setText(placeHistory);
            adapter.getFilter().filter(placeHistory);
        }

		edtPlace.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){
				// When user changed the Text
				adapter.getFilter().filter(s);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){

			}

			@Override
			public void afterTextChanged(Editable s){
			}
		});

        lsvPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String place = (String)parent.getItemAtPosition(position);
                setPlace(place);
            }
        });
	}

	@Override
	public int getFooterItemId(){
		return 0;
	}

	@Override
	public void onClick(View v){

	}

    private void setPlace(String place){
        Intent intent = activity.getIntent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SwConst.KEY_HISTORY_NAME, place);
        intent.putExtras(bundle);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    @Override
	protected void onClickBackBtn(){
		setPlace(CCStringUtil.toString(edtPlace.getText()));
	}
}

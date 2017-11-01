package trente.asia.addresscard.commons.activities;

import android.os.Bundle;

import trente.asia.addresscard.R;
import trente.asia.addresscard.services.user.PhotoPageContainerFragment;
import trente.asia.welfare.adr.activity.WelfareActivity;

public class AddressCardActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_card);
		addFragment(new PhotoPageContainerFragment());
	}
}

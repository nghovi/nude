package nguyenhoangviet.vpcorp.nude.commons.activities;

import android.os.Bundle;

import nguyenhoangviet.vpcorp.nude.R;
import nguyenhoangviet.vpcorp.nude.services.user.PhotoPageContainerFragment;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;

public class MainActivity extends WelfareActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_card);
		addFragment(new PhotoPageContainerFragment());
	}
}

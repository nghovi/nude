package asia.trente.officeletter.commons.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import asia.trente.officeletter.R;
import nguyenhoangviet.vpcorp.welfare.adr.activity.WelfareActivity;

/**
 * Created by tien on 8/18/2017.
 */

public class SplashActivity extends WelfareActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run(){
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        }, 1000);
    }
}

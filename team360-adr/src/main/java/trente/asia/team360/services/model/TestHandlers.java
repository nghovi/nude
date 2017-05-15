package trente.asia.team360.services.model;

import android.util.Log;
import android.view.View;

import trente.asia.team360.BR;

/**
 * Created by takano-yasuhiro on 2017/05/15.
 */

public class TestHandlers {

    private static final String TAG = "Handlers";

    TestModel mTestModel;

    public TestHandlers(TestModel mTestModel) {
        this.mTestModel = mTestModel;
    }

    public void onClickEvent(View view) {
        Log.d(TAG, "onClickText");
        mTestModel.testText = "changed text";
        mTestModel.notifyPropertyChanged(BR.testText);

    }

    public void onClickImage(View view) {
        Log.d(TAG, "onClickImage");
        mTestModel.testSrc = "@drawable/wf_attachment";
        mTestModel.notifyPropertyChanged(BR.testSrc);
    }

}

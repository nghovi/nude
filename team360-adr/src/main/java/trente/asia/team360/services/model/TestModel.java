package trente.asia.team360.services.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;


/**
 * Created by takano-yasuhiro on 2017/05/15.
 */

public class TestModel extends BaseObservable {

    public String testText = "init text";

    public String testSrc = "@drawable/wf_camera";


    public TestModel() {
    }

    @Bindable
    public String getTestText() {
        return testText;
    }

    public void setTestText(String testText) {
        this.testText = testText;
    }

    @Bindable
    public String getTestSrc() {
        return testSrc;
    }

    public void setTestSrc(String testSrc) {
        this.testSrc = testSrc;
    }


}

package nguyenhoangviet.vpcorp.messenger.services.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.fragment.AbstractMsgFragment;

/**
 * Created by tien on 8/18/2017.
 */

public class NewNoteFragment extends AbstractMsgFragment implements View.OnClickListener{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_new_note, container, false);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(R.drawable.wf_back_white, getString(R.string.msg_new_note), R.drawable.cs_row_check);
        getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_id_header_right_icon:
                getFragmentManager().popBackStack();
                break;
        }
    }
}

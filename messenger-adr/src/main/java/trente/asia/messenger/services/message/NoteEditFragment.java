package nguyenhoangviet.vpcorp.messenger.services.message;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.databinding.FragmentNoteEditBinding;
import nguyenhoangviet.vpcorp.messenger.fragment.AbstractMsgFragment;

/**
 * Created by tien on 8/17/2017.
 */

public class NoteEditFragment extends AbstractMsgFragment implements View.OnClickListener{
    private FragmentNoteEditBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_edit, container, false);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(R.drawable.wf_back_white, getString(R.string.msg_note_edit), R.drawable.cs_row_check);
        getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
        binding.btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_id_header_right_icon:
                getFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
                break;
            case R.id.btn_delete:
                getFragmentManager().popBackStack();
                getFragmentManager().popBackStack();
                break;
        }
    }
}

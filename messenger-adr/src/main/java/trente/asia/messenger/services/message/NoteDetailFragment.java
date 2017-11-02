package nguyenhoangviet.vpcorp.messenger.services.message;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.messenger.databinding.FragmentNoteDetailBinding;
import nguyenhoangviet.vpcorp.messenger.fragment.AbstractMsgFragment;
import nguyenhoangviet.vpcorp.messenger.services.message.view.CommentAdapter;

/**
 * Created by tien on 8/17/2017.
 */

public class NoteDetailFragment extends AbstractMsgFragment implements View.OnClickListener{
    private FragmentNoteDetailBinding binding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_detail, container, false);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(R.drawable.wf_back_white, getString(R.string.msg_note_detail), R.drawable.ic_edit);
        binding.rlvComment.setAdapter(new CommentAdapter());
        binding.rlvComment.setLayoutManager(new LinearLayoutManager(getContext()));
        getView().findViewById(R.id.img_id_header_right_icon).setOnClickListener(this);
        binding.txtLikeCount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_id_header_right_icon:
                gotoFragment(new NoteEditFragment());
                break;
            case R.id.txt_like_count:
                gotoFragment(new LikeListFragment());
                break;
        }
    }
}

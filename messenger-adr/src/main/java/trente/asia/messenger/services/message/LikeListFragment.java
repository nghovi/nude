package trente.asia.messenger.services.message;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.messenger.R;
import trente.asia.messenger.databinding.FragmentLikeListBinding;
import trente.asia.messenger.fragment.AbstractMsgFragment;
import trente.asia.messenger.services.message.view.LikeUserAdapter;

/**
 * Created by tien on 8/18/2017.
 */

public class LikeListFragment extends AbstractMsgFragment {
    private FragmentLikeListBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_like_list, container, false);
            mRootView = binding.getRoot();
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        initHeader(R.drawable.wf_back_white, getString(R.string.msg_like_list_title), null);
        binding.rlvLikeUser.setAdapter(new LikeUserAdapter());
        binding.rlvLikeUser.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}

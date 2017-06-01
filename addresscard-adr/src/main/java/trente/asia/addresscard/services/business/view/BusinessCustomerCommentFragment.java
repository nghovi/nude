package trente.asia.addresscard.services.business.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import asia.chiase.core.util.CCJsonUtil;
import trente.asia.addresscard.ACConst;
import trente.asia.addresscard.R;
import trente.asia.addresscard.commons.fragments.AbstractAddressCardFragment;
import trente.asia.addresscard.databinding.FragmentCustomerCommentBinding;
import trente.asia.addresscard.services.business.model.CommentModel;
import trente.asia.addresscard.services.business.model.CustomerModel;
import trente.asia.addresscard.services.business.presenter.CustomerCommentAdapter;
import trente.asia.android.view.util.CAObjectSerializeUtil;

/**
 * Created by tien on 5/12/2017.
 */

public class BusinessCustomerCommentFragment extends AbstractAddressCardFragment
    implements CustomerCommentAdapter.OnCustomerCommentAdapterListener {

    private FragmentCustomerCommentBinding binding;
    private CustomerModel customer;
    private CustomerCommentAdapter adapter;

    public static BusinessCustomerCommentFragment newInstance(CustomerModel customer) {
        BusinessCustomerCommentFragment fragment = new BusinessCustomerCommentFragment();
        fragment.customer = customer;
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("customerId", customer.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestLoad(ACConst.AC_BUSINESS_COMMENT_LIST, jsonObject, true);
    }

    @Override
    protected void successLoad(JSONObject response, String url) {
        super.successLoad(response, url);
        if (ACConst.AC_BUSINESS_COMMENT_LIST.equals(url)) {
            List<CommentModel> comments = CCJsonUtil.convertToModelList(
                    response.optString("comments"), CommentModel.class);
            adapter.setComments(comments);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            binding = DataBindingUtil.inflate(inflater,
                    R.layout.fragment_customer_comment, container, false);
            mRootView = binding.getRoot();
            adapter = new CustomerCommentAdapter(this);
            binding.listComments.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.listComments.setAdapter(adapter);
            binding.btnAddComment.setOnClickListener(this);
        }
        return mRootView;
    }

    @Override
    protected void initView() {
        super.initView();
        super.initHeader(R.drawable.ac_back_white, customer.customerName, null);
    }

    @Override
    public int getFooterItemId() {
        return R.id.lnr_view_footer_card;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_comment:
                addComment();
                break;
            default:
                break;
        }
    }

    private void addComment() {
        JSONObject jsonObject = CAObjectSerializeUtil.serializeObject(binding.lnrContent, null);
        try {
            jsonObject.put("customerId", customer.key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(ACConst.AC_BUSINESS_COMMENT_UPDATE, jsonObject, true);
    }

    @Override
    protected void successUpdate(JSONObject response, String url) {
        super.successUpdate(response, url);
        if (ACConst.AC_BUSINESS_COMMENT_UPDATE.equals(url)) {
            initData();
            binding.edtMessage.setText("");
        }
    }

    @Override
    public void deleteComment(int commentId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", commentId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestUpdate(ACConst.AC_BUSINESS_COMMENT_DELETE, jsonObject, true);
    }
}

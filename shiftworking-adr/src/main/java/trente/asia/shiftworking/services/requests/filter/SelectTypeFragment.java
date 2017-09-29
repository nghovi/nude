package trente.asia.shiftworking.services.requests.filter;

import java.util.List;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.shiftworking.R;
import trente.asia.shiftworking.common.fragments.AbstractSwFragment;
import trente.asia.shiftworking.common.interfaces.OnTypeAdapterListener;
import trente.asia.shiftworking.databinding.FragmentSelectTypeBinding;
import trente.asia.shiftworking.services.requests.adapter.TypeAdapter;
import trente.asia.welfare.adr.models.ApiObjectModel;

/**
 * Created by tien on 9/21/2017.
 */

public class SelectTypeFragment extends AbstractSwFragment {
    private TypeAdapter adapter = new TypeAdapter();

    public void setData(OnTypeAdapterListener callback, List<ApiObjectModel> types, ApiObjectModel selectedType) {
        adapter.setDepartments(types, selectedType);
        adapter.setCallback(callback);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_select_type, container, false);
        }
        return mRootView;
    }

    @Override
    public int getFooterItemId() {
        return 0;
    }

    @Override
    protected void initView() {
        super.initView();
        FragmentSelectTypeBinding binding = DataBindingUtil.bind(mRootView);
        binding.btnCancel.setOnClickListener(this);

        binding.listTypes.setAdapter(adapter);
        binding.listTypes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setFragmentManager(getFragmentManager());

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.showSearchList(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                getFragmentManager().popBackStack();
                break;
        }
    }
}

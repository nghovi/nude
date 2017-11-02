package nguyenhoangviet.vpcorp.messenger.services.user.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import nguyenhoangviet.vpcorp.messenger.R;
import nguyenhoangviet.vpcorp.welfare.adr.models.DeptModel;

/**
 * Created by tien on 8/16/2017.
 */

public class DeptAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<DeptModel>  depts;

    public void setDepts(List<DeptModel> depts) {
        this.depts = depts;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_dept, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return depts == null ? 4 : depts.size();
    }
}

package asia.trente.officeletter.services.salary.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.holder.ViewHolder;
import asia.trente.officeletter.databinding.ItemSalaryBinding;
import asia.trente.officeletter.services.salary.model.MonthModel;
import asia.trente.officeletter.services.salary.model.SalaryModel;

/**
 * Created by tien on 8/21/2017.
 */

public class SalaryAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<MonthModel> months;
    private OnSalaryAdapterListener callback;

    public void setCallback(OnSalaryAdapterListener callback) {
        this.callback = callback;
    }

    public void setMonths(List<MonthModel> months) {
        this.months = months;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salary, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemSalaryBinding binding = (ItemSalaryBinding) holder.getBinding();
        final MonthModel monthModel = months.get(position);
        binding.salaryName.setText(monthModel.getName());

        if (monthModel.year != 0) {
            binding.lnrSalary.setBackgroundColor(ContextCompat.getColor(binding.lnrSalary.getContext(), R.color.wf_login_background_color));
            binding.imgArrowRight.setVisibility(View.INVISIBLE);
        } else {
            binding.lnrSalary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback != null) {
                        callback.onSalaryClick(monthModel);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return months == null ? 0 : months.size();
    }

    public interface OnSalaryAdapterListener {
        void onSalaryClick(MonthModel salary);
    }
}

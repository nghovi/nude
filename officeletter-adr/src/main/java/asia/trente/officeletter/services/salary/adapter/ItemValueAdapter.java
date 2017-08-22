package asia.trente.officeletter.services.salary.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.holder.ViewHolder;
import asia.trente.officeletter.databinding.ItemGroupSalaryBinding;
import asia.trente.officeletter.services.salary.model.ItemValueModel;

/**
 * Created by tien on 8/22/2017.
 */

public class ItemValueAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<ItemValueModel> itemValues;

    public void setItemValues(List<ItemValueModel> itemValues) {
        this.itemValues = itemValues;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_salary, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ItemGroupSalaryBinding binding = (ItemGroupSalaryBinding) holder.getBinding();
        final ItemValueModel itemValue = itemValues.get(position);
        if (itemValue.isGroup) {
            binding.rltItemValue.setVisibility(View.GONE);
            binding.lnrGroupValue.setVisibility(View.VISIBLE);
            binding.groupName.setText(itemValue.groupName);
            binding.checkboxShowSub.setChecked(itemValue.showSub);
            binding.checkboxShowSub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (ItemValueModel itemValueModel : itemValues) {
                        if (itemValueModel.groupName.equals(itemValue.groupName)) {
                            itemValueModel.showSub = ((CheckBox) v).isChecked();
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            binding.rltItemValue.setVisibility(View.VISIBLE);
            binding.lnrGroupValue.setVisibility(View.GONE);
            binding.itemName.setText(itemValue.itemName);
            binding.itemSub.setText(itemValue.itemSub);
            binding.itemValue.setText(itemValue.value);
            binding.itemSub.setVisibility(itemValue.showSub ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return itemValues == null ? 0 : itemValues.size();
    }
}

package trente.asia.messenger.services.message.view;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import trente.asia.messenger.R;
import trente.asia.messenger.databinding.ItemNoteBinding;
import trente.asia.messenger.services.user.view.ViewHolder;

/**
 * Created by tien on 8/17/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<ViewHolder> {

    private OnNoteAdapterListener callback;

    public void setCallback(OnNoteAdapterListener callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ItemNoteBinding binding = (ItemNoteBinding) holder.getBinding();
        binding.lnrLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        binding.lnrAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onNoteClick(position);
                }
            }
        });
        binding.lnrNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null) {
                    callback.onNoteClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public interface OnNoteAdapterListener {
        void onNoteClick(int position);
    }

    private void log(String msg) {
        Log.e("NoteAdapter", msg);
    }
}

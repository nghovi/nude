package asia.trente.officeletter.services.wiki.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import asia.trente.officeletter.R;
import asia.trente.officeletter.commons.holder.ViewHolder;
import asia.trente.officeletter.databinding.ItemWikiBinding;
import asia.trente.officeletter.services.wiki.listener.OnWikiAdapterListener;
import asia.trente.officeletter.services.wiki.model.WikiModel;

/**
 * Created by tien on 8/24/2017.
 */

public class WikiAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<WikiModel> wikis;
    private OnWikiAdapterListener callback;

    public void setCallback(OnWikiAdapterListener callback) {
        this.callback = callback;
    }

    public void setWikis(List<WikiModel> wikis) {
        this.wikis = wikis;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wiki, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemWikiBinding binding = (ItemWikiBinding) holder.getBinding();
        final WikiModel wikiModel = wikis.get(position);
        binding.wikiTitle.setText(wikiModel.wikiTitle);
        binding.wikiDate.setText(wikiModel.wikiDate.substring(0, 10).replace("/", "."));
        binding.rltWiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onWikiClick(wikiModel.key);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return wikis == null ? 0 : wikis.size();
    }
}

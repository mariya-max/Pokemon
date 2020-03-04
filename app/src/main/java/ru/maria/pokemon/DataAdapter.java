package ru.maria.pokemon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ru.maria.pokemon.Model.PokemonPreview;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private static ClickListener clickListener;
    private List<PokemonPreview> dataList;
    private Context context;

    ILoadMore loadMore;
    boolean isLoading;
    int visibleThreshold = 30;//видимый порог
    int firstVisibleItems;
    int totalItemCount;

    String TAG = "myLogs";

    int VIEW_STANDARD = 0;
    int VIEW_HIGHLIGHT = 1;
    boolean vision = false;

    public DataAdapter(RecyclerView recyclerView, final List<PokemonPreview> dataList, boolean vision) {

        this.dataList = dataList;
        this.vision = vision;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleThreshold = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                firstVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleThreshold + firstVisibleItems) >= totalItemCount) {
                        isLoading = true;
                        if (loadMore != null) {
                            loadMore.onLoadMore();
                        }
                    }

                }
            }
        });

    }

    public void setLoadMore(ILoadMore loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public int getItemViewType(int position) {
        if (vision) {
            if (position == 0) {
                return VIEW_HIGHLIGHT;
            } else if (position == dataList.size()) {

            }else {
                return VIEW_STANDARD;
            }
        }
        return VIEW_STANDARD;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_HIGHLIGHT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_highlight, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        }
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PokemonPreview data = dataList.get(position);

        holder.nameTextView.setText("Name: " + data.getName());
        Glide.with(context)
                .load(data.getUrlImg())
                .into(holder.imageView);

    }

    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public int getItemCount() {

        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull final ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        int layoutPosition = holder.getLayoutPosition();
        Log.d(TAG, "onViewAttachedToWindow: getLayoutPosition = " + layoutPosition);

        layoutPosition = holder.getAdapterPosition();
        Log.d(TAG, "onViewAttachedToWindow: getAdapterPosition = " + layoutPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            clickListener.onItemClick(view, getAdapterPosition());

        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        DataAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }
}

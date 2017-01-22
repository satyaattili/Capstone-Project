package in.mobileappdev.news.adapters;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;
import in.mobileappdev.news.models.Source;

public class NewsSourcesGridAdapter extends RecyclerView.Adapter<NewsSourcesGridAdapter.ViewHolder> {
    private ArrayList<Source> newsSources;
    private Context context;
    private OnClickListener onClickListener;

    public NewsSourcesGridAdapter(Context context, ArrayList<Source> android) {
        this.newsSources = android;
        this.context = context;
    }

    @Override
    public NewsSourcesGridAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_grid_news_sources, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsSourcesGridAdapter.ViewHolder viewHolder, int i) {

        Glide.with(context)
                .load(newsSources.get(i).getUrlsToLogos().getLarge())
                .asBitmap()
                .placeholder(R.drawable.source_thumbnail_blue)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        viewHolder.sourceImage.setImageBitmap(resource); // Possibly runOnUiThread()
                    }
                });

        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(viewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsSources.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.source_img)
        ImageView sourceImage;
        @BindView(R.id.source_item_parent)
        RelativeLayout parent;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}


package in.mobileappdev.news.adapters;

/**
 * Created by satyanarayana.avv on 09-12-2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.mobileappdev.news.R;
import in.mobileappdev.news.models.Article;
import in.mobileappdev.news.utils.Utils;

public class NewsArticleslistAdapter extends RecyclerView.Adapter<NewsArticleslistAdapter.ViewHolder> {
    private ArrayList<Article> newsArticles;
    private Context context;
    private OnClickListener onClickListener;

    public NewsArticleslistAdapter(Context context, ArrayList<Article> articles) {
        this.newsArticles = articles;
        this.context = context;
    }

    @Override
    public NewsArticleslistAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_news_article, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsArticleslistAdapter.ViewHolder viewHolder, int i) {
        Article article = newsArticles.get(i);
        viewHolder.newsTitle.setText(article.getTitle());
        //glide
        Glide.with(context)
                .load(article.getUrlToImage())
                .placeholder(R.drawable.source_thumbnail_blue)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(viewHolder.thumbnail);

        viewHolder.publishedAt.setText(Utils.getTimeString(article.getPublishedAt()));

        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(view, viewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_title)
        TextView newsTitle;
        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        @BindView(R.id.published_at)
        TextView publishedAt;
        @BindView(R.id.parent)
        RelativeLayout parent;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}


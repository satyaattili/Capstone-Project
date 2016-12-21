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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.mobileappdev.news.R;
import in.mobileappdev.news.models.Source;

public class NewsSourcesGridAdapter extends RecyclerView.Adapter<NewsSourcesGridAdapter.ViewHolder> {
  private ArrayList<Source> newsSources;
  private Context context;

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
  public void onBindViewHolder(NewsSourcesGridAdapter.ViewHolder viewHolder, int i) {

    viewHolder.sourceName.setText(newsSources.get(i).getName());
    //glide
    Glide.with(context)
        .load(newsSources.get(i).getUrlsToLogos().getSmall())
        .placeholder(R.drawable.source_thumbnail)
        .crossFade()
        .into(viewHolder.sourceImage);
  }

  @Override
  public int getItemCount() {
    return newsSources.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView sourceName;
    private ImageView sourceImage;

    public ViewHolder(View view) {
      super(view);

      sourceName = (TextView) view.findViewById(R.id.source_title);
      sourceImage = (ImageView) view.findViewById(R.id.source_img);
    }
  }
}


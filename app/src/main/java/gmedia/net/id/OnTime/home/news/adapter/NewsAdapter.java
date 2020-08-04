package gmedia.net.id.OnTime.home.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.home.news.model.NewsModel;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    Context context;
    List<NewsModel> newsModels;

    public NewsAdapter(Context context, List<NewsModel> newsModels){
        this.context = context;
        this.newsModels = newsModels;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_news, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        NewsModel item = newsModels.get(position);
        holder.tvTitleNews.setText(item.getJudul());
        holder.tvDateNews.setText(item.getTanggal());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, item.getJudul(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDateNews,tvTitleNews;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDateNews = itemView.findViewById(R.id.tv_date_news);
            tvTitleNews = itemView.findViewById(R.id.tv_titile_news);
        }
    }
}

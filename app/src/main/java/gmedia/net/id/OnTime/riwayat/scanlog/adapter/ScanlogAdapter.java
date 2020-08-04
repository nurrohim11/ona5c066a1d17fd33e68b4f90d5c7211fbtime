package gmedia.net.id.OnTime.riwayat.scanlog.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.home.news.adapter.NewsAdapter;
import gmedia.net.id.OnTime.riwayat.scanlog.model.ScanlogModel;

public class ScanlogAdapter extends RecyclerView.Adapter<ScanlogAdapter.ViewHolder> {

    Context context;
    List<ScanlogModel> scanlogModels;

    public ScanlogAdapter(Context context, List<ScanlogModel> scanlogModels){
        this.context = context;
        this.scanlogModels = scanlogModels;
    }

    @NonNull
    @Override
    public ScanlogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_scanlog, parent, false);
        return new ScanlogAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanlogAdapter.ViewHolder holder, int position) {
        ScanlogModel item = scanlogModels.get(position);
        if(position == 0){
            holder.tvKeterangan.setTextColor(Color.parseColor("#1093C9"));
            holder.tvWaktu.setTextColor(Color.parseColor("#1093C9"));
            holder.tvKeterangan.setTypeface(null, Typeface.BOLD);
            holder.tvWaktu.setTypeface(null, Typeface.BOLD);
            holder.tvKeterangan.setText(item.getKeterangan());
            holder.tvWaktu.setText(item.getScan_time());
        }else{
            holder.tvKeterangan.setTextColor(Color.parseColor("#393939"));
            holder.tvWaktu.setTextColor(Color.parseColor("#393939"));
            holder.tvKeterangan.setTypeface(null, Typeface.NORMAL);
            holder.tvWaktu.setTypeface(null, Typeface.NORMAL);
            holder.tvKeterangan.setText(item.getKeterangan());
            holder.tvWaktu.setText(item.getScan_time());
        }
    }

    @Override
    public int getItemCount() {
        return scanlogModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvKeterangan, tvWaktu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
            tvWaktu = itemView.findViewById(R.id.tv_waktu);
        }
    }
}

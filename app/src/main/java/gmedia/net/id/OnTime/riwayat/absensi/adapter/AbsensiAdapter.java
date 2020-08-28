package gmedia.net.id.OnTime.riwayat.absensi.adapter;

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
import gmedia.net.id.OnTime.riwayat.absensi.model.AbsensiModel;
import gmedia.net.id.OnTime.riwayat.scanlog.adapter.ScanlogAdapter;
import gmedia.net.id.OnTime.riwayat.scanlog.model.ScanlogModel;

public class AbsensiAdapter extends RecyclerView.Adapter<AbsensiAdapter.ViewHolder> {

    List<AbsensiModel> models;
    Context context;

    public AbsensiAdapter(Context context, List<AbsensiModel> models){
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public AbsensiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_rekap_absensi, parent, false);
        return new AbsensiAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsensiAdapter.ViewHolder holder, int position) {
        AbsensiModel item = models.get(position);
        holder.tvKeterangan.setText(item.getKeterangan());
        holder.tvTgl.setText(item.getTgl());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvKeterangan, tvTgl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
            tvTgl = itemView.findViewById(R.id.tv_tgl);
        }
    }
}

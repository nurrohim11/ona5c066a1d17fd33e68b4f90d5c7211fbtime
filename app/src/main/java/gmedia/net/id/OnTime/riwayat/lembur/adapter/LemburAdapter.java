package gmedia.net.id.OnTime.riwayat.lembur.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.jadwal.model.JadwalModel;
import gmedia.net.id.OnTime.riwayat.lembur.model.LemburModel;
import gmedia.net.id.OnTime.utils.Utils;

public class LemburAdapter extends RecyclerView.Adapter<LemburAdapter.ViewHolder> {

    List<LemburModel> models;
    Context context;

    public LemburAdapter(Context context, List<LemburModel> models){
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public LemburAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_riwayat_lembur, parent, false);
        return new LemburAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LemburAdapter.ViewHolder holder, int position) {
        LemburModel item = models.get(position);
        holder.tvMulai.setText(item.getTgl_mulai());
        holder.tvSelesai.setText(item.getTgl_selesai());
        holder.tvKeterangan.setText(item.getKeterangan());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMulai, tvSelesai, tvKeterangan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMulai = itemView.findViewById(R.id.tv_mulai);
            tvSelesai = itemView.findViewById(R.id.tv_selesai);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
        }
    }
}

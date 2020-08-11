package gmedia.net.id.OnTime.riwayat.jadwal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.cuti.model.RiwayatCutiModel;
import gmedia.net.id.OnTime.riwayat.jadwal.model.JadwalModel;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.FormatItem;

public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.ViewHolder> {

    List<JadwalModel> models;
    Context context;

    public JadwalAdapter(Context context, List<JadwalModel> models){
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public JadwalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_riwayat_jadwal, parent, false);
        return new JadwalAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalAdapter.ViewHolder holder, int position) {
        JadwalModel item = models.get(position);
        String awal = Utils.formatDate("yyyy-MM-dd", "dd MMMM yyyy",item.getTgl());
        holder.tvHari.setText(item.getHari());
        holder.tvTgl.setText(awal);
        holder.tvKeterangan.setText(item.getKeterangan());
        holder.tvShift.setText(item.getShift());
        holder.tvJamMulai.setText(item.getJam_mulai());
        holder.tvJamSelesai.setText(item.getJam_selesai());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHari, tvTgl, tvKeterangan, tvShift, tvJamMulai, tvJamSelesai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHari = itemView.findViewById(R.id.tv_hari);
            tvTgl = itemView.findViewById(R.id.tv_tgl);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
            tvShift = itemView.findViewById(R.id.tv_shift);
            tvJamMulai = itemView.findViewById(R.id.tv_jam_mulai);
            tvJamSelesai = itemView.findViewById(R.id.tv_jam_selesai);
        }
    }
}

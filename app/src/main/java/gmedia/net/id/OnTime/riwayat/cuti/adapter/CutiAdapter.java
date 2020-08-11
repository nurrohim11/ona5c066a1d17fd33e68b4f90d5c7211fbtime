package gmedia.net.id.OnTime.riwayat.cuti.adapter;

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
import gmedia.net.id.OnTime.approval.cuti.model.CutiModel;
import gmedia.net.id.OnTime.riwayat.absensi.model.AbsensiModel;
import gmedia.net.id.OnTime.riwayat.cuti.model.RiwayatCutiModel;
import gmedia.net.id.OnTime.utils.Utils;

public class CutiAdapter extends RecyclerView.Adapter<CutiAdapter.ViewHolder> {

    List<RiwayatCutiModel> models;
    Context context;

    public CutiAdapter(Context context, List<RiwayatCutiModel> models){
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public CutiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_riwayat_cuti, parent, false);
        return new CutiAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CutiAdapter.ViewHolder holder, int position) {
        RiwayatCutiModel item = models.get(position);
        String awal = Utils.formatDate("yyyy-MM-dd","dd/MM/yyyy",item.getTglAwal());
        String akhir = Utils.formatDate("yyyy-MM-dd","dd/MM/yyyy",item.getTglAkhir());
        holder.tvKeterangan.setText(item.getAlasan());
        holder.tvTglAkhir.setText(akhir);
        holder.tvTglAwal.setText(awal);
        holder.tvStatus.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTglAwal, tvTglAkhir, tvKeterangan, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTglAwal = itemView.findViewById(R.id.tv_tgl_awal);
            tvTglAkhir = itemView.findViewById(R.id.tv_tgl_akhir);
        }
    }
}

package gmedia.net.id.OnTime.riwayat.ijin.adapter;

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
import gmedia.net.id.OnTime.riwayat.ijin.model.RiwayatIjinModel;
import gmedia.net.id.OnTime.utils.Utils;

public class IjinAdapter extends RecyclerView.Adapter<IjinAdapter.ViewHolder> {

    List<RiwayatIjinModel> models;
    Context context;

    public IjinAdapter(Context context, List<RiwayatIjinModel> models){
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public IjinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_riwayat_ijin, parent, false);
        return new IjinAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IjinAdapter.ViewHolder holder, int position) {
        RiwayatIjinModel item = models.get(position);
        String tgl = Utils.formatDate("yyyy-MM-dd","dd/MM/yyyy",item.getTanggal());
        holder.tvKeterangan.setText(item.getAlasan());
        holder.tvTgl.setText(tgl);
        holder.tvJam.setText(item.getJam());
        holder.tvStatus.setText(item.getStatus());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTgl, tvJam, tvKeterangan, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKeterangan = itemView.findViewById(R.id.tv_alasan);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTgl = itemView.findViewById(R.id.tv_tgl);
            tvJam = itemView.findViewById(R.id.tv_jam);
        }
    }
}

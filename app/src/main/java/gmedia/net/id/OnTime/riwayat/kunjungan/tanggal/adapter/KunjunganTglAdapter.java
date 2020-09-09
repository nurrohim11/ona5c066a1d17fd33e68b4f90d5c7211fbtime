package gmedia.net.id.OnTime.riwayat.kunjungan.tanggal.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.jadwal.adapter.JadwalAdapter;
import gmedia.net.id.OnTime.riwayat.kunjungan.KunjunganModel;
import gmedia.net.id.OnTime.utils.Utils;

public class KunjunganTglAdapter extends RecyclerView.Adapter<KunjunganTglAdapter.ViewHolder> {

    List<KunjunganModel> kunjunganModels;
    Context context;

    public KunjunganTglAdapter(Context context, List<KunjunganModel> models){
        this.kunjunganModels = models;
        this.context = context;
    }

    @NonNull
    @Override
    public KunjunganTglAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_kunjungan_tgl, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KunjunganTglAdapter.ViewHolder holder, int position) {
        KunjunganModel item = kunjunganModels.get(position);
        holder.tvTgl.setText(Utils.formatDate("yyyy-MM-dd", "dd MMMM yyyy",item.getTgl()));
        holder.tvKeterangan.setText(item.getKeterangan());
        holder.tvLokasi.setText(item.getLokasi());
        holder.ivRute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:"+item.getLatitude()+","+item.getLongitude());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return kunjunganModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTgl, tvLokasi, tvKeterangan;
        private ImageView ivRute;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTgl = itemView.findViewById(R.id.tv_tgl);
            tvLokasi = itemView.findViewById(R.id.tv_lokasi);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
            ivRute = itemView.findViewById(R.id.iv_rute);
        }
    }
}

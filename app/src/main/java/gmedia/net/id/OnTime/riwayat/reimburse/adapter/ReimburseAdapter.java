package gmedia.net.id.OnTime.riwayat.reimburse.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alkhattabi.kalert.KAlertDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.reimburse.detail.DetailReimburseActivity;
import gmedia.net.id.OnTime.riwayat.reimburse.model.ReimburseModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.FormatItem;

public class ReimburseAdapter extends RecyclerView.Adapter<ReimburseAdapter.ViewHolder>   {

    private List<ReimburseModel> reimburseModels;
    private Context context;
    private String status = "0";
    KAlertDialog pDialog;
    KAlertDialog pDialogApprove;

    public ReimburseAdapter(Context context, List<ReimburseModel> models){
        this.context =context;
        this.reimburseModels = models;
    }

    @NonNull
    @Override
    public ReimburseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_riwayat_reimburse, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SyntheticAccessor")
    @Override
    public void onBindViewHolder(@NonNull final ReimburseAdapter.ViewHolder holder, int position) {
        final ReimburseModel model = reimburseModels.get(position);

        if(model.getStatus().equals("1")){
            holder.tvStatus.setText("Distujui");
        }else  if(model.getStatus().equals("2")){
            holder.tvStatus.setText("Ditolak");
        }else{
            holder.tvStatus.setText("Proses");
        }
        holder.tvKeterangan.setText(model.getKet());
        holder.tvTanggal.setText(Utils.formatDate(FormatItem.formatDateTime, FormatItem.formatDateDisplay,model.getInsert_at()));

        Integer nominal = Integer.parseInt(model.getNominal());
        holder.tvNominal.setText(String.format("Rp %,d", nominal));

        holder.ivDetail.setOnClickListener(v->{
            Intent intent = new Intent(holder.itemView.getContext(), DetailReimburseActivity.class);
            intent.putExtra(DetailReimburseActivity.REIMBURSE_ITEM, new Gson().toJson(model));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reimburseModels.size();
    }

    @Override
    public long getItemId(int position) {
        long id = Long.parseLong(reimburseModels.get(position).getId());
        return id;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvStatus, tvTanggal, tvNominal, tvKeterangan;
        private RelativeLayout rlStatus;
        private ImageView ivDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDetail = itemView.findViewById(R.id.iv_detail);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvTanggal = itemView.findViewById(R.id.tv_tgl);
            tvNominal = itemView.findViewById(R.id.tv_nominal);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
        }
    }
}

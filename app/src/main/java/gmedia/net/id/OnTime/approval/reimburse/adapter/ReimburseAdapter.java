package gmedia.net.id.OnTime.approval.reimburse.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alkhattabi.sweetdialog.SweetDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.approval.reimburse.detail.DetailApprovalReimburseActivity;
import gmedia.net.id.OnTime.approval.reimburse.model.ReimburseModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.FormatItem;

public class ReimburseAdapter extends RecyclerView.Adapter<ReimburseAdapter.ViewHolder>   {

    private List<ReimburseModel> reimburseModels;
    private Context context;
    private String status = "0";
    SweetDialog pDialog;
    SweetDialog pDialogApprove;

    public ReimburseAdapter(Context context, List<ReimburseModel> models){
        this.context =context;
        this.reimburseModels = models;
    }

    @NonNull
    @Override
    public ReimburseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_approval_reimburse, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SyntheticAccessor")
    @Override
    public void onBindViewHolder(@NonNull final ReimburseAdapter.ViewHolder holder, int position) {
        final ReimburseModel model = reimburseModels.get(position);
        holder.tvNik.setText(model.getNik());
        holder.tvInsertAt.setText(Utils.formatDate(FormatItem.formatDateTime, FormatItem.formatDateDisplay,model.getInsert_at()));
        holder.tvNama.setText(model.getNama());
        holder.tvKeterangan.setText(model.getKet());
//        holder.tvTanggal.setText(Utils.formatDate(FormatItem.formatDateTime, FormatItem.formatDateDisplay,model.getTgl_pembayaran()));

        Integer nominal = Integer.parseInt(model.getNominal());
        holder.tvNominal.setText(String.format("Rp %,d", nominal));
        holder.llItem.setOnClickListener(v->{
            Intent intent = new Intent(holder.itemView.getContext(), DetailApprovalReimburseActivity.class);
            intent.putExtra(DetailApprovalReimburseActivity.REIMBURSE_ITEM, new Gson().toJson(model));
            context.startActivity(intent);
//            pDialogApprove = new SweetDialog(context, SweetDialog.CUSTOM_IMAGE_TYPE);
//            pDialogApprove.setTitleText("Are you sure ?");
//            pDialogApprove.setContentText("Apakah anda yakin untuk menyutujui pengajuan ini ?");
//            pDialogApprove.setCustomImage(R.drawable.gambaraproval);
//            pDialogApprove.setCancelable(true);
//            pDialogApprove.setCloseDialog(true);
//            pDialogApprove.setConfirmText("Setujui"); //Do not call this if you don't want to show confirm button
//            pDialogApprove.setCancelText("Tolak");//Do not call this if you don't want to show cancel button
//            pDialogApprove.setConfirmClickListener(new SweetDialog.KAlertClickListener() {
//                @Override
//                public void onClick(SweetDialog sweetDialog) {
//                    pDialog = new SweetDialog(context, SweetDialog.PROGRESS_TYPE);
//                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
//                    pDialog.setCancelable(false);
//                    pDialog.show();
//                    approveReimburse(model.getId(),"1",position);
//                }
//            });
//            pDialogApprove.setCancelClickListener(new SweetDialog.KAlertClickListener() {
//                @Override
//                public void onClick(SweetDialog sweetDialog) {
//                    pDialog = new SweetDialog(context, SweetDialog.PROGRESS_TYPE);
//                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
//                    pDialog.setCancelable(false);
//                    pDialog.show();
//                    approveReimburse(model.getId(),"2",position);
//                }
//            });
//            pDialogApprove.show();
        });

//        holder.ivDetail.setOnClickListener(v->{
//        });

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
        private TextView tvInsertAt, tvNama, tvNominal, tvKeterangan, tvNik;
//        ImageView ivDetail;
        private LinearLayout llItem;
        private RelativeLayout rlStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llItem = itemView.findViewById(R.id.ll_item);
            tvNik = itemView.findViewById(R.id.tv_nik);
//            ivDetail = itemView.findViewById(R.id.iv_detail);
            tvInsertAt = itemView.findViewById(R.id.tv_insert_at);
            tvNama = itemView.findViewById(R.id.tv_dari);
//            tvTanggal = itemView.findViewById(R.id.tv_tgl);
            tvNominal = itemView.findViewById(R.id.tv_nominal);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
        }
    }

    private void approveReimburse(String id_reimburse,String status, int position){
        JSONObject params = new JSONObject();
        try {
            params.put("id_reimburse",id_reimburse);
            params.put("status_apv",status);
        }catch (Exception e){
            e.printStackTrace();
        }
        new ApiVolley(context, params,"POST", ServerUrl.urlPostApproval,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogApprove.dismiss();
                        pDialog.dismiss();
                        Toasty.success(context,message, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                reimburseModels.remove(position);
                                notifyDataSetChanged();
                                pDialogApprove.dismiss();
                            }
                        },500);
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialog.dismiss();
                        Toasty.error(context,message,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(String message) {
                        pDialog.dismiss();
                        Toasty.error(context,message,Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }
}

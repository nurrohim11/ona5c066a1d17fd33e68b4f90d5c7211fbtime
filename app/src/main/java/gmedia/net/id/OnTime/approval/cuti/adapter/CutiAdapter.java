package gmedia.net.id.OnTime.approval.cuti.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rohimdev.sweetdialog.SweetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.approval.cuti.model.CutiModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

import static gmedia.net.id.coremodul.FormatItem.formatDate;
import static gmedia.net.id.coremodul.FormatItem.formatDateDisplay;
import static gmedia.net.id.coremodul.FormatItem.formatTimestamp;

public class CutiAdapter extends RecyclerView.Adapter<CutiAdapter.ViewHolder> {
    Context context;
    List<CutiModel> cutiModels;
    private String status = "0";
    SweetDialog pDialog;
    SweetDialog pDialogApprove;

    public CutiAdapter(Context context, List<CutiModel> cutiModelList){
        this.cutiModels = cutiModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public CutiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_approval_cuti, parent, false);
        return new CutiAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CutiAdapter.ViewHolder holder, int position) {
        CutiModel item = cutiModels.get(position);
        holder.tvNik.setText(item.getNik());
        holder.tvDari.setText(item.getNama());
        holder.tvKeterangan.setText(item.getAlasan());
        holder.tvTglMulai.setText(Utils.formatDate(formatDate,formatDateDisplay,item.getAwal()));
        holder.tvTglSelesai.setText(Utils.formatDate(formatDate,formatDateDisplay,item.getAkhir()));
        holder.tvInsert.setText(Utils.formatDate(formatTimestamp,formatDateDisplay,item.getInsert_at()));
        holder.itemView.setOnClickListener(v->{
            pDialogApprove = new SweetDialog(context, SweetDialog.CUSTOM_IMAGE_TYPE);
            pDialogApprove.setTitleText("Are you sure ?");
            pDialogApprove.setContentText("Apakah anda yakin untuk menyutujui pengajuan ini ?");
            pDialogApprove.setCustomImage(R.drawable.gambaraproval);
            pDialogApprove.setCancelable(true);
            pDialogApprove.setCloseDialog(true);
            pDialogApprove.setConfirmText("Setujui"); //Do not call this if you don't want to show confirm button
            pDialogApprove.setCancelText("Tolak");//Do not call this if you don't want to show cancel button
            pDialogApprove.setConfirmClickListener(new SweetDialog.SweetClickListener() {
                @Override
                public void onClick(SweetDialog sweetDialog) {
                    pDialog = new SweetDialog(context, SweetDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
                    pDialog.setCancelable(false);
                    pDialog.show();

                    status = "2";
                    if (!status.equals("0")) {
                        approveCuti(item.getId(),position);
                    }
                }
            });
            pDialogApprove.setCancelClickListener(new SweetDialog.SweetClickListener() {
                @Override
                public void onClick(SweetDialog sweetDialog) {
                    pDialog = new SweetDialog(context, SweetDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
                    pDialog.setCancelable(false);
                    pDialog.show();

                    status = "4";
                    if (!status.equals("0")) {
                        approveCuti(item.getId(),position);
                    }
                }
            });
            pDialogApprove.show();
        });
    }

    @Override
    public int getItemCount() {
        return cutiModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvInsert, tvDari, tvKeterangan, tvTglMulai, tvTglSelesai, tvNik;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNik = itemView.findViewById(R.id.tv_nik);
            tvInsert = itemView.findViewById(R.id.tv_insert_at);
            tvDari = itemView.findViewById(R.id.tv_dari);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
            tvTglMulai = itemView.findViewById(R.id.tv_tgl_mulai);
            tvTglSelesai = itemView.findViewById(R.id.tv_tgl_selesai);
        }
    }

    private void approveCuti(int id,int position){

        JSONObject jBody = new JSONObject();
        try {
            jBody.put("id", id);
            jBody.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiVolley(context, jBody,"POST", ServerUrl.approvalCuti,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogApprove.dismiss();
                        pDialog.dismiss();
                        Toasty.success(context,message, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                cutiModels.remove(position);
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

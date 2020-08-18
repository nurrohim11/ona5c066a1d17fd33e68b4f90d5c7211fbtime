package gmedia.net.id.OnTime.approval.ijin.adapter;

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

import com.alkhattabi.kalert.KAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.approval.cuti.adapter.CutiAdapter;
import gmedia.net.id.OnTime.approval.cuti.model.CutiModel;
import gmedia.net.id.OnTime.approval.ijin.model.IjinModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

import static gmedia.net.id.coremodul.FormatItem.formatDate;
import static gmedia.net.id.coremodul.FormatItem.formatDateDisplay;
import static gmedia.net.id.coremodul.FormatItem.formatTimestamp;

public class IjinAdapter extends RecyclerView.Adapter<IjinAdapter.ViewHolder> {
    Context context;
    List<IjinModel> ijinModels;
    private String status = "0";
    KAlertDialog pDialog;
    KAlertDialog pDialogApprove;

    public IjinAdapter(Context context, List<IjinModel> ijinModels){
        this.ijinModels = ijinModels;
        this.context = context;
    }

    @NonNull
    @Override
    public IjinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_approval_ijin, parent, false);
        return new IjinAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IjinAdapter.ViewHolder holder, int position) {
        IjinModel item = ijinModels.get(position);
        holder.tvNik.setText(item.getNik());
        holder.tvNama.setText(item.getNama());
        holder.tvKeterangan.setText(item.getAlasan());
        holder.tvTgl.setText(Utils.formatDate(formatDate,formatDateDisplay,item.getTgl()));
        holder.tvInsertAt.setText(Utils.formatDate(formatTimestamp,formatDateDisplay,item.getInsert_at()));
        holder.itemView.setOnClickListener(v->{
            pDialogApprove = new KAlertDialog(context, KAlertDialog.CUSTOM_IMAGE_TYPE);
            pDialogApprove.setTitleText("Are you sure ?");
            pDialogApprove.setContentText("Apakah anda yakin untuk menyutujui pengajuan ini ?");
            pDialogApprove.setCustomImage(R.drawable.gambaraproval);
            pDialogApprove.setCancelable(true);
            pDialogApprove.setCloseDialog(true);
            pDialogApprove.setConfirmText("Setujui"); //Do not call this if you don't want to show confirm button
            pDialogApprove.setCancelText("Tolak");//Do not call this if you don't want to show cancel button
            pDialogApprove.setConfirmClickListener(new KAlertDialog.KAlertClickListener() {
                @Override
                public void onClick(KAlertDialog kAlertDialog) {
                    pDialog = new KAlertDialog(context,KAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
                    pDialog.setCancelable(false);
                    pDialog.show();

                    status = "2";
                    if (!status.equals("0")) {
                        ApproveCuti(item.getId(),position);
                    }
                }
            });
            pDialogApprove.setCancelClickListener(new KAlertDialog.KAlertClickListener() {
                @Override
                public void onClick(KAlertDialog kAlertDialog) {
                    pDialog = new KAlertDialog(context,KAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
                    pDialog.setCancelable(false);
                    pDialog.show();

                    status = "4";
                    if (!status.equals("0")) {
                        ApproveCuti(item.getId(),position);
                    }
                }
            });
            pDialogApprove.show();
        });
    }

    @Override
    public int getItemCount() {
        return ijinModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvInsertAt, tvTgl, tvKeterangan, tvNama, tvNik;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNik = itemView.findViewById(R.id.tv_nik);
            tvInsertAt = itemView.findViewById(R.id.tv_insert_at);
            tvTgl = itemView.findViewById(R.id.tv_tgl);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
            tvNama = itemView.findViewById(R.id.tv_dari);
        }
    }

    private void ApproveCuti(String id,int position){

        JSONObject jBody = new JSONObject();
        try {
            jBody.put("id", id);
            jBody.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new ApiVolley(context, jBody,"POST", ServerUrl.approvalIjin,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogApprove.dismiss();
                        pDialog.dismiss();
                        Toasty.success(context,message, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ijinModels.remove(position);
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

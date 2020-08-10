package gmedia.net.id.OnTime.approval.reimburse;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.approval.cuti.adapter.CutiAdapter;
import gmedia.net.id.OnTime.approval.cuti.model.CutiModel;
import gmedia.net.id.OnTime.approval.reimburse.adapter.ReimburseAdapter;
import gmedia.net.id.OnTime.approval.reimburse.model.ReimburseModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

public class ReimburseFragment extends Fragment {

    View view;
    @BindView(R.id.rv_reimburse)
    RecyclerView rvReimburse;
    @BindView(R.id.ll_warning)
    LinearLayout llWarning;
    @BindView(R.id.tv_warning)
    TextView tvWarning;
    SessionManager sessionManager;

    private int start =0,count =10;
    List<ReimburseModel> reimburseModels = new ArrayList<>();
    ReimburseAdapter adapter;
    LinearLayoutManager linearLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reimburse, container, false);
        sessionManager = new SessionManager(getContext());
        ButterKnife.bind(this,view);
        start=0;
        count=10;
        if(sessionManager.getKeyApprovalReimburs().equals("1")){
            rvReimburse.setVisibility(View.VISIBLE);
            linearLayoutManager = new LinearLayoutManager(getContext());
            setupListReimburse();
            setupListScrollListenerReimburse();
        }else{
            llWarning.setVisibility(View.VISIBLE);
            tvWarning.setText("Anda tidak mempunyai akses untuk approval reimburse");
        }

        return view;
    }

    private void setupListReimburse() {
        adapter = new ReimburseAdapter(getContext(), reimburseModels);
        rvReimburse.setLayoutManager(linearLayoutManager);
        rvReimburse.setAdapter(adapter);
    }

    private void setupListScrollListenerReimburse() {
        rvReimburse.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (! recyclerView.canScrollVertically(1)){
                    start += count;
                    loadReimburse();
                }
            }
        });
    }

    private void loadReimburse(){

//        pDialog.dismiss();
        if(start == 0){
            reimburseModels.clear();
        }
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("status", "0");
            jBody.put("start", String.valueOf(start));
            jBody.put("count", String.valueOf(count));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("reimbursefragment","Params "+jBody);

        new ApiVolley(getContext(),jBody,"POST", ServerUrl.urlListApprovalReimburs,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        Log.d("reimbusefragment",response);
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0; i < arr.length(); i++){
                                JSONObject isi = arr.getJSONObject(i);
                                reimburseModels.add(new ReimburseModel(
                                        isi.getString("id"),
                                        isi.getString("nama"),
                                        isi.getString("tgl_pembayaran"),
                                        isi.getString("foto_pembayaran"),
                                        isi.getString("nominal"),
                                        isi.getString("ket"),
                                        isi.getString("approval"),
                                        isi.getString("insert_at"),
                                        isi.getString("status")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onEmpty(String message) {
                        Log.d("reimbursefragment",message);
                    }

                    @Override
                    public void onFail(String message) {

                    }
                })
        );
    }
    @Override
    public void onResume() {
        super.onResume();
        start =0;
        count=10;
        if(sessionManager.getKeyApprovalReimburs().equals("1")){
            rvReimburse.setVisibility(View.VISIBLE);
            loadReimburse();
            adapter.notifyDataSetChanged();
        }else{
            llWarning.setVisibility(View.VISIBLE);
            tvWarning.setText("Anda tidak mempunyai akses untuk approval reimburse");
        }
    }

}
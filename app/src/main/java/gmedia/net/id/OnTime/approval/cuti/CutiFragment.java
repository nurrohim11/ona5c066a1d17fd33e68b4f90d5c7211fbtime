package gmedia.net.id.OnTime.approval.cuti;

import android.graphics.Color;
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
import android.widget.Toast;

import com.alkhattabi.kalert.KAlertDialog;

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
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

public class CutiFragment extends Fragment {

    View view;
    @BindView(R.id.rv_cuti)
    RecyclerView rvCuti;
    @BindView(R.id.ll_warning)
    LinearLayout llWarning;
    @BindView(R.id.tv_warning)
    TextView tvWarning;
    SessionManager sessionManager;

    private int start =0,count =10;
//    KAlertDialog pDialog;

    List<CutiModel> cutiModels = new ArrayList<>();
    CutiAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cuti, container, false);
        sessionManager = new SessionManager(getContext());
        ButterKnife.bind(this,view);
        start=0;
        count=10;

//        pDialog = new KAlertDialog(getContext(),KAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
//        pDialog.setCancelable(false);
//        pDialog.show();

        if(sessionManager.getKeyApprovalCuti().equals("1")){
            rvCuti.setVisibility(View.VISIBLE);
            linearLayoutManager = new LinearLayoutManager(getContext());
            setupListCuti();
            setupListScrollListenerCuti();
        }else{
            llWarning.setVisibility(View.VISIBLE);
            tvWarning.setText("Anda tidak mempunyai akses untuk approval cuti");
        }
        return view;
    }

    private void setupListCuti() {
        adapter = new CutiAdapter(getContext(), cutiModels);
        rvCuti.setLayoutManager(linearLayoutManager);
        rvCuti.setAdapter(adapter);
    }

    private void setupListScrollListenerCuti() {
        rvCuti.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (! recyclerView.canScrollVertically(1)){
//                    pDialog.show();
                    start += count;
                    loadCuti();
                }
            }
        });
    }

    private void loadCuti(){
//        pDialog.dismiss();
        if(start == 0){
            cutiModels.clear();
        }
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("id", "");
            jBody.put("start", String.valueOf(start));
            jBody.put("count", String.valueOf(count));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("cutifragment","Params "+jBody);

        new ApiVolley(getContext(),jBody,"POST", ServerUrl.listApprovalCuti,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        Log.d("cutifragment",response);
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0; i < arr.length(); i++){
                                JSONObject isi = arr.getJSONObject(i);
                                cutiModels.add(new CutiModel(
                                        isi.getInt("id"),
                                        isi.getInt("id_karyawan"),
                                        isi.getString("nik"),
                                        isi.getString("nama"),
                                        isi.getString("awal"),
                                        isi.getString("akhir"),
                                        isi.getString("alasan"),
                                        isi.getString("status"),
                                        isi.getString("keterangan"),
                                        isi.getString("jumlah"),
                                        isi.getString("insert_at")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onEmpty(String message) {
                        Log.d("cutifragment",message);
//                        rvCuti.setVisibility(View.GONE);
//                        llWarning.setVisibility(View.VISIBLE);
//                        tvWarning.setText("Data cuti tidak ada");
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
        if(sessionManager.getKeyApprovalCuti().equals("1")){
            rvCuti.setVisibility(View.VISIBLE);
            loadCuti();
            adapter.notifyDataSetChanged();
        }else{
            llWarning.setVisibility(View.VISIBLE);
            tvWarning.setText("Anda tidak mempunyai akses untuk approval cuti");
        }
    }
}
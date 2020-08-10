package gmedia.net.id.OnTime.approval.ijin;

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
import gmedia.net.id.OnTime.approval.ijin.adapter.IjinAdapter;
import gmedia.net.id.OnTime.approval.ijin.model.IjinModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

public class IjinFragment extends Fragment {

    @BindView(R.id.rv_ijin)
    RecyclerView rvIjin;
    @BindView(R.id.ll_warning)
    LinearLayout llWarning;
    @BindView(R.id.tv_warning)
    TextView tvWarning;
    SessionManager sessionManager;

    private int start =0,count =10;
    LinearLayoutManager linearLayoutManager;
    List<IjinModel> ijinModels = new ArrayList<>();
//    KAlertDialog pDialogProcess;
    IjinAdapter adapter;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ijin, container, false);
        ButterKnife.bind(this,view);
        sessionManager = new SessionManager(getContext());
//        pDialogProcess = new KAlertDialog(getContext(),KAlertDialog.PROGRESS_TYPE);
//        pDialogProcess.getProgressHelper().setBarColor(R.color.colorGreen);
//        pDialogProcess.setCancelable(false);
//        pDialogProcess.setTitleText("Please wait...!");
//        pDialogProcess.show();

        if(sessionManager.getKeyApprovalIjin().equals("1")){
            rvIjin.setVisibility(View.VISIBLE);
            linearLayoutManager = new LinearLayoutManager(getContext());
            setupListIjin();
            setupListScrollListenerIjin();
        }else{
            llWarning.setVisibility(View.VISIBLE);
            tvWarning.setText("Anda tidak mempunyai akses untuk approval cuti");
        }
        return  view;
    }

    private void setupListIjin() {
        adapter = new IjinAdapter(getContext(), ijinModels);
        rvIjin.setLayoutManager(linearLayoutManager);
        rvIjin.setAdapter(adapter);
    }

    private void setupListScrollListenerIjin() {
        rvIjin.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (! recyclerView.canScrollVertically(1)){
//                    pDialogProcess.show();
                    start += count;
                    loadIjin();
                }
            }
        });
    }

    private void loadIjin(){
//        pDialogProcess.dismiss();
        if(start == 0){
            ijinModels.clear();
        }
        JSONObject jBody = new JSONObject();
        try {
            jBody.put("id", "");
            jBody.put("start", String.valueOf(start));
            jBody.put("count", String.valueOf(count));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("ijinfragment","Params "+jBody);

        new ApiVolley(getContext(),jBody,"POST", ServerUrl.listApprovalIjin,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        Log.d("cutifragment",response);
                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i=0; i < arr.length(); i++){
                                JSONObject isi = arr.getJSONObject(i);
                                ijinModels.add(new IjinModel(
                                        isi.getString("id"),
                                        isi.getString("id_karyawan"),
                                        isi.getString("nik"),
                                        isi.getString("nama"),
                                        isi.getString("tgl"),
                                        isi.getString("jam"),
                                        isi.getString("alasan"),
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
                        Log.d("ijinfragment",message);
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
        if(sessionManager.getKeyApprovalIjin().equals("1")){
            rvIjin.setVisibility(View.VISIBLE);
            loadIjin();
            adapter.notifyDataSetChanged();
        }else{
            llWarning.setVisibility(View.VISIBLE);
            tvWarning.setText("Anda tidak mempunyai akses untuk approval ijin");
        }
    }

}
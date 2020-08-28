package gmedia.net.id.OnTime.riwayat.cuti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.alkhattabi.sweetdialog.SweetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.cuti.adapter.CutiAdapter;
import gmedia.net.id.OnTime.riwayat.cuti.model.RiwayatCutiModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

public class RiwayatCutiActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_riwayat_cuti)
    RecyclerView rvRiwayatCuti;

    CutiAdapter adapter;
    List<RiwayatCutiModel> cutiModels = new ArrayList<>();
    String tgl_awal ="";
    String tgl_akhir ="";
    int start =0, count =20;
    LinearLayoutManager linearLayoutManager;
    SweetDialog pDialogPrcess;

    // flag 1 = scroll

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_cuti);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        start =0;count=20;

        pDialogPrcess = new SweetDialog(this, SweetDialog.PROGRESS_TYPE);
        pDialogPrcess.setCancelable(false);
        pDialogPrcess.show();

        linearLayoutManager = new LinearLayoutManager(this);
        setupListRiwayatCuti();
        setupListScrollListenerRiwayatCuti();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupListScrollListenerRiwayatCuti(){
        rvRiwayatCuti.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (! recyclerView.canScrollVertically(1)){
                    pDialogPrcess.dismiss();
                    start += count;
                    loadRiwayatCuti();
                }
            }
        });
    }

    private void setupListRiwayatCuti(){
        adapter = new CutiAdapter(RiwayatCutiActivity.this, cutiModels);
        rvRiwayatCuti.setLayoutManager(linearLayoutManager);
        rvRiwayatCuti.setAdapter(adapter);
    }

    private void loadRiwayatCuti(){
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

        Log.d("Riwayatcuti", "params "+ String.valueOf(jBody));

        new ApiVolley(RiwayatCutiActivity.this,jBody,"POST", ServerUrl.historyCuti,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogPrcess.dismiss();
                        Log.d("Riwayatcuti", "response "+response);

                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject isi = arr.getJSONObject(i);
                                cutiModels.add(new RiwayatCutiModel(
                                        isi.getString("awal"),
                                        isi.getString("akhir"),
                                        isi.getString("alasan"),
                                        isi.getString("keterangan")
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onEmpty(String message) {
                        pDialogPrcess.dismiss();
                    }

                    @Override
                    public void onFail(String message) {
                        pDialogPrcess.dismiss();
                        if(message.equals("Unauthorized")){
                            try {
                                SessionManager session = new SessionManager(RiwayatCutiActivity.this);
                                session.logoutUser(Utils.loginActivity(RiwayatCutiActivity.this));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toasty.error(RiwayatCutiActivity.this,message, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        start =0;
        count =20;
        cutiModels.clear();
        loadRiwayatCuti();
        adapter.notifyDataSetChanged();
    }
}
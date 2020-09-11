package gmedia.net.id.OnTime.riwayat.ijin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.rohimdev.sweetdialog.SweetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.riwayat.ijin.adapter.IjinAdapter;
import gmedia.net.id.OnTime.riwayat.ijin.model.RiwayatIjinModel;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;
import gmedia.net.id.coremodul.SessionManager;

public class RiwayatIjinActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_riwayat_ijin)
    RecyclerView rvRiwayatIjin;

    IjinAdapter adapter;
    List<RiwayatIjinModel> ijinModels = new ArrayList<>();
    int start =0, count =20;
    LinearLayoutManager linearLayoutManager;
    SweetDialog pDialogPrcess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_ijin);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        start =0;count=20;

        linearLayoutManager = new LinearLayoutManager(this);
        setupListRiwayatIjin();
        setupListScrollListenerRiwayatIjin();

        pDialogPrcess = new SweetDialog(this, SweetDialog.PROGRESS_TYPE);
        pDialogPrcess.setCancelable(false);
        pDialogPrcess.getProgressHelper().setBarColor(R.color.colorProcess);
        pDialogPrcess.show();
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

    private void setupListScrollListenerRiwayatIjin(){
        rvRiwayatIjin.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (! recyclerView.canScrollVertically(1)){
                    pDialogPrcess.dismiss();
                    start += count;
                    loadRiwayatIjin();
                }
            }
        });
    }

    private void setupListRiwayatIjin(){
        adapter = new IjinAdapter(RiwayatIjinActivity.this, ijinModels);
        rvRiwayatIjin.setLayoutManager(linearLayoutManager);
        rvRiwayatIjin.setAdapter(adapter);
    }

    private void loadRiwayatIjin(){
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

        Log.d("Riwayatijin", "params "+ String.valueOf(jBody));

        new ApiVolley(RiwayatIjinActivity.this,jBody,"POST", ServerUrl.historyIjin,
                new AppRequestCallback(new AppRequestCallback.ResponseListener() {
                    @Override
                    public void onSuccess(String response, String message) {
                        pDialogPrcess.dismiss();
                        Log.d("Riwayatijin", "response "+response);

                        try {
                            JSONArray arr = new JSONArray(response);
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject isi = arr.getJSONObject(i);
                                ijinModels.add(new RiwayatIjinModel(
                                        isi.getString("tgl"),
                                        isi.getString("jam"),
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
                                SessionManager session = new SessionManager(RiwayatIjinActivity.this);
                                session.logoutUser(Utils.loginActivity(RiwayatIjinActivity.this));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Toasty.error(RiwayatIjinActivity.this,message, Toast.LENGTH_SHORT).show();
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
        loadRiwayatIjin();
        adapter.notifyDataSetChanged();
    }
}
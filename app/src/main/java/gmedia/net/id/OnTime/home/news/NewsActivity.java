package gmedia.net.id.OnTime.home.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
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
import gmedia.net.id.OnTime.home.news.adapter.NewsAdapter;
import gmedia.net.id.OnTime.home.news.model.NewsModel;
import gmedia.net.id.OnTime.utils.GridSpacingItemDecoration;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

import static gmedia.net.id.OnTime.utils.Utils.formatTgl;

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rv_pengumuman)
    RecyclerView rvPengumuman;

    List<NewsModel> newsModels = new ArrayList<>();
    NewsAdapter newsAdapter;
    SweetDialog pDialogProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        pDialogProcess = new SweetDialog(NewsActivity.this, SweetDialog.PROGRESS_TYPE);
        pDialogProcess.getProgressHelper().setBarColor(Color.parseColor("#18C3F3"));
        pDialogProcess.setCancelable(false);
        pDialogProcess.show();

        // news
        newsAdapter = new NewsAdapter(NewsActivity.this, newsModels);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(NewsActivity.this, 1);
        rvPengumuman.setLayoutManager(layoutManager);
        rvPengumuman.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dpToPx(getApplicationContext(),0), true));
        rvPengumuman.setItemAnimator(new DefaultItemAnimator());
        rvPengumuman.setAdapter(newsAdapter);
        rvPengumuman.setNestedScrollingEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        newsModels.clear();
        prepareNews();
    }

    private void prepareNews(){
        JSONObject jbody = new JSONObject();
        new ApiVolley(NewsActivity.this,jbody,"POST", ServerUrl.listNews,new AppRequestCallback(new AppRequestCallback.ResponseListener() {
            @Override
            public void onSuccess(String response, String message) {
                Log.d(">>>>",response);
                pDialogProcess.dismiss();
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject isi = arr.getJSONObject(i);
                        newsModels.add(new NewsModel(
                                isi.getString("id"),
                                formatTgl(isi.getString("tgl")),
                                isi.getString("judul"),
                                isi.getString("teks"),
                                isi.getString("gambar")
                        ));
                    }
                    newsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEmpty(String message) {
                pDialogProcess.dismiss();
            }

            @Override
            public void onFail(String message) {
                pDialogProcess.dismiss();
                Toasty.error(NewsActivity.this,message, Toast.LENGTH_SHORT).show();
            }
        }));
    }

}
package gmedia.net.id.OnTime.home.news;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.home.news.model.NewsModel;

public class DetailNewsActivity extends AppCompatActivity {

    public static String DETAIL_NEWS_ACTIVITY = "detail-news";
    private Gson gson = new Gson();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_tgl)
    TextView tvTgl;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_teks)
    TextView tvTeks;
    @BindView(R.id.iv_gambar)
    ImageView ivGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView(){

        String news_item = getIntent().getStringExtra(DETAIL_NEWS_ACTIVITY);
        final NewsModel item = gson.fromJson(news_item, NewsModel.class);
        tvTitle.setText(item.getJudul());
        tvTgl.setText(item.getTanggal());
        tvTeks.setText(item.getDeskripsi());
        if(!item.getGambar().equals("")){
            Glide
                    .with(DetailNewsActivity.this)
                    .load(item.getGambar())
                    .into(ivGambar);
        }
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
}
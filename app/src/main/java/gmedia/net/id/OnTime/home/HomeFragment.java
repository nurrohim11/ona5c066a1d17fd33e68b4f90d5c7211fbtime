package gmedia.net.id.OnTime.home;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.home.menu.adapter.MenuHomeAdapter;
import gmedia.net.id.OnTime.home.menu.model.MenuHomeModel;
import gmedia.net.id.OnTime.home.news.NewsActivity;
import gmedia.net.id.OnTime.home.news.adapter.NewsAdapter;
import gmedia.net.id.OnTime.home.news.model.NewsModel;
import gmedia.net.id.OnTime.utils.GridSpacingItemDecoration;
import gmedia.net.id.OnTime.utils.ServerUrl;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.ApiVolley;
import gmedia.net.id.coremodul.AppRequestCallback;

import static gmedia.net.id.OnTime.utils.Utils.formatTgl;

public class HomeFragment extends Fragment {

    private MenuHomeAdapter adapter;
    private List<MenuHomeModel> homeModelList;

    private List<NewsModel> newsModels;
    NewsAdapter newsAdapter;

    View view;
    RecyclerView rvMenuHome, rvNews;
    @BindView(R.id.tv_see_news)
    TextView tvSeeNews;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        initUi();
        return view;
    }

    private void initUi(){
        // menu
        rvMenuHome = view.findViewById(R.id.rv_menu_home);
        homeModelList = new ArrayList<>();
        adapter = new MenuHomeAdapter(getContext(), homeModelList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rvMenuHome.setLayoutManager(mLayoutManager);
        rvMenuHome.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
        rvMenuHome.setItemAnimator(new DefaultItemAnimator());
        rvMenuHome.setAdapter(adapter);
        rvMenuHome.setNestedScrollingEnabled(false);
        prepareMenu();

        // news
        rvNews = view.findViewById(R.id.rv_news);
        newsModels = new ArrayList<>();
        newsAdapter = new NewsAdapter(getContext(), newsModels);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        rvNews.setLayoutManager(layoutManager);
        rvNews.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(0), true));
        rvNews.setItemAnimator(new DefaultItemAnimator());
        rvNews.setAdapter(newsAdapter);
        rvNews.setNestedScrollingEnabled(false);
        prepareNews();

        tvSeeNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewsActivity.class));
            }
        });

    }

    private void prepareMenu() {

        MenuHomeModel menuHomeModels = new MenuHomeModel("001","Absen Masuk", Utils.package_name(getContext())+".home.menu.AbsenMasukActivity", R.drawable.absenmasuk, R.drawable.bg_red);
        homeModelList.add(menuHomeModels);

        menuHomeModels =new MenuHomeModel("002","Absen Pulang",Utils.package_name(getContext())+".home.menu.AbsenPulangActivity", R.drawable.absenpulang,R.drawable.bg_maroon);
        homeModelList.add(menuHomeModels);

        menuHomeModels =new MenuHomeModel("003","Istirahat",Utils.package_name(getContext())+".home.menu.IstirahatActivity", R.drawable.istirahat, R.drawable.bg_magenta);
        homeModelList.add(menuHomeModels);

        menuHomeModels=new MenuHomeModel("004","Kembali Kerja",Utils.package_name(getContext())+".home.menu.KembaliKerjaActivity", R.drawable.mulaikerja,R.drawable.bg_darkred);
        homeModelList.add(menuHomeModels);

        menuHomeModels=new MenuHomeModel("005","Mulai Lembur",Utils.package_name(getContext())+".home.menu.MulaiLemburActivity", R.drawable.mulailembur,R.drawable.bg_blueviolet);
        homeModelList.add(menuHomeModels);

        menuHomeModels=new MenuHomeModel("006","Selesai Lembur",Utils.package_name(getContext())+".home.menu.SelesaiLemburActivity", R.drawable.selesailembur,R.drawable.bg_purple);
        homeModelList.add(menuHomeModels);

        menuHomeModels=new MenuHomeModel("007","Check In",Utils.package_name(getContext())+".home.menu.LockInActivity", R.drawable.lockin, R.drawable.bg_royalblue);
        homeModelList.add(menuHomeModels);

        menuHomeModels=new MenuHomeModel("008","Check Out",Utils.package_name(getContext())+".home.menu.LockOutActivity", R.drawable.lockout, R.drawable.bg_navy);
        homeModelList.add(menuHomeModels);

        adapter.notifyDataSetChanged();
    }

    private void prepareNews(){
        JSONObject jbody = new JSONObject();
        try {
            jbody.put("count","3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ApiVolley(getContext(),jbody,"POST", ServerUrl.listNews,new AppRequestCallback(new AppRequestCallback.ResponseListener() {
            @Override
            public void onSuccess(String response, String message) {
                Log.d(">>>>",response);
                try {
                    JSONArray arr = new JSONArray(response);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject isi = arr.getJSONObject(i);
                        newsModels.add(new NewsModel(
                                isi.getString("id"),
                                formatTgl(isi.getString("tgl")),
                                isi.getString("judul")
                        ));
                    }
                    newsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onEmpty(String message) {

            }

            @Override
            public void onFail(String message) {

            }
        }));
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
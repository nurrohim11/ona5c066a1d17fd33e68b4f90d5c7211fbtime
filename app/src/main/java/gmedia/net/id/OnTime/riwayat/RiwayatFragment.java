package gmedia.net.id.OnTime.riwayat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.pengajuan.MenuPengajuanAdapter;
import gmedia.net.id.OnTime.utils.GridSpacingItemDecoration;
import gmedia.net.id.OnTime.utils.Utils;
import gmedia.net.id.coremodul.CustomModel;

public class RiwayatFragment extends Fragment {

    View view;
    List<CustomModel> customModels;
    MenuRiwayatAdapter adapter;

    @BindView(R.id.rv_menu_riwayat)
    RecyclerView rvMenuRiwayat;

    public RiwayatFragment() {
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
        view =  inflater.inflate(R.layout.fragment_riwayat, container, false);
        ButterKnife.bind(this,view);
        initUi();
        return view;
    }

    private void initUi(){
        // menu
        customModels = new ArrayList<>();
        adapter = new MenuRiwayatAdapter(getContext(), customModels);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rvMenuRiwayat.setLayoutManager(mLayoutManager);
        rvMenuRiwayat.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dpToPx(getContext(),0), true));
        rvMenuRiwayat.setItemAnimator(new DefaultItemAnimator());
        rvMenuRiwayat.setAdapter(adapter);
        rvMenuRiwayat.setNestedScrollingEnabled(false);
        prepareMenu();
    }

    private void prepareMenu(){

        CustomModel customModel = new CustomModel("001","Scanlog",Utils.package_name(getContext())+".riwayat.scanlog.ScanlogActivity", String.valueOf(R.drawable.scanlog), String.valueOf(R.drawable.bg_red));
        customModels.add(customModel);

        customModel=new CustomModel("002","Absensi",Utils.package_name(getContext())+".riwayat.absensi.RekapAbsensiActivity", String.valueOf(R.drawable.rekapabsensi), String.valueOf(R.drawable.bg_magenta));
        customModels.add(customModel);

        customModel=new CustomModel("003","Reimburse",Utils.package_name(getContext())+".riwayat.reimburse.RekapReimburseActivity", String.valueOf(R.drawable.reimbuse), String.valueOf(R.drawable.bg_blueviolet));
        customModels.add(customModel);

        customModel=new CustomModel("004","Cuti/Ijin",Utils.package_name(getContext())+".riwayat.cutiijin.CutiijinActivity", String.valueOf(R.drawable.cuti_ijin), String.valueOf(R.drawable.bg_royalblue));
        customModels.add(customModel);

        adapter.notifyDataSetChanged();
    }
}
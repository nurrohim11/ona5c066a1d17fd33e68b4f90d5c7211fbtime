package gmedia.net.id.OnTime.pengajuan;

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

import gmedia.net.id.coremodul.CustomModel;
import gmedia.net.id.OnTime.R;
import gmedia.net.id.OnTime.utils.GridSpacingItemDecoration;
import gmedia.net.id.OnTime.utils.Utils;

public class PengajuanFragment extends Fragment {

    MenuPengajuanAdapter adapter;
    List<CustomModel> customModels;
    View view;
    RecyclerView rvMenuPengajuan;

    public PengajuanFragment() {
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
        view = inflater.inflate(R.layout.fragment_pengajuan,container, false);
        initUi();
        return view;
    }

    private void initUi(){
        rvMenuPengajuan = view.findViewById(R.id.rv_menu_pengajuan);

        // menu
        customModels = new ArrayList<>();
        adapter = new MenuPengajuanAdapter(getContext(), customModels);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rvMenuPengajuan.setLayoutManager(mLayoutManager);
        rvMenuPengajuan.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dpToPx(getContext(),0), true));
        rvMenuPengajuan.setItemAnimator(new DefaultItemAnimator());
        rvMenuPengajuan.setAdapter(adapter);
        rvMenuPengajuan.setNestedScrollingEnabled(false);
        prepareMenu();
    }

    private void prepareMenu(){

        CustomModel customModel = new CustomModel("001","Cuti",Utils.package_name(getContext())+".pengajuan.cuti.CutiActivity", String.valueOf(R.drawable.cuti), String.valueOf(R.drawable.bg_red));
        customModels.add(customModel);

        customModel=new CustomModel("002","Ijin",Utils.package_name(getContext())+".pengajuan.ijin.IjinActivity", String.valueOf(R.drawable.ijin), String.valueOf(R.drawable.bg_magenta));
        customModels.add(customModel);

        customModel=new CustomModel("003","Reimburse",Utils.package_name(getContext())+".pengajuan.reimburse.ReimburseActivity", String.valueOf(R.drawable.pengajuanreimbuse), String.valueOf(R.drawable.bg_blueviolet));
        customModels.add(customModel);

        customModel=new CustomModel("004","Lembur",Utils.package_name(getContext())+".pengajuan.lembur.LemburActivity", String.valueOf(R.drawable.jamputih), String.valueOf(R.drawable.bg_rust));
        customModels.add(customModel);

        adapter.notifyDataSetChanged();
    }
}
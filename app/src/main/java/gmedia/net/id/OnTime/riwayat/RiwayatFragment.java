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

        customModel=new CustomModel("002","Absensi",Utils.package_name(getContext())+".riwayat.absensi.RiwayatAbsensiActivity", String.valueOf(R.drawable.rekapabsensi), String.valueOf(R.drawable.bg_magenta));
        customModels.add(customModel);

        customModel=new CustomModel("004","Cuti",Utils.package_name(getContext())+".riwayat.cuti.RiwayatCutiActivity", String.valueOf(R.drawable.cuti_ijin), String.valueOf(R.drawable.bg_royalblue));
        customModels.add(customModel);

        customModel=new CustomModel("005","Ijin",Utils.package_name(getContext())+".riwayat.ijin.RiwayatIjinActivity", String.valueOf(R.drawable.cuti_ijin), String.valueOf(R.drawable.bg_navy));
        customModels.add(customModel);

        customModel=new CustomModel("003","Reimburse",Utils.package_name(getContext())+".riwayat.reimburse.RiwayatReimburseActivity", String.valueOf(R.drawable.reimbuse), String.valueOf(R.drawable.bg_blueviolet));
        customModels.add(customModel);

        customModel=new CustomModel("006","Gaji",Utils.package_name(getContext())+".riwayat.gaji.GajiActivity", String.valueOf(R.drawable.pengajuangaji), String.valueOf(R.drawable.bg_navy));
        customModels.add(customModel);

        customModel=new CustomModel("007","Jadwal Kerja",Utils.package_name(getContext())+".riwayat.jadwal.JadwalActivity", String.valueOf(R.drawable.pengajuanjadwal), String.valueOf(R.drawable.bg_orange));
        customModels.add(customModel);

        customModel=new CustomModel("008","Lembur",Utils.package_name(getContext())+".riwayat.lembur.RiwayatLemburActivity", String.valueOf(R.drawable.jamputih), String.valueOf(R.drawable.bg_rust));
        customModels.add(customModel);

        customModel=new CustomModel("009","Tanggal Kerja di Luar Kantor",Utils.package_name(getContext())+".riwayat.kunjungan.tanggal.KunjunganTglActivity", String.valueOf(R.drawable.pengajuanjadwal), String.valueOf(R.drawable.bg_celery));
        customModels.add(customModel);

        customModel=new CustomModel("01o","Hari Kerja di Luar Kantor",Utils.package_name(getContext())+".riwayat.kunjungan.hari.KunjunganHariActivity", String.valueOf(R.drawable.pengajuanjadwal), String.valueOf(R.drawable.bg_green));
        customModels.add(customModel);

        adapter.notifyDataSetChanged();
    }
}
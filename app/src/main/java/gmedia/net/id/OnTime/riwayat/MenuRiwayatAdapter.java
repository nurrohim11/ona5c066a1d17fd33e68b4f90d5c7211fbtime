package gmedia.net.id.OnTime.riwayat;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import gmedia.net.id.OnTime.R;
import gmedia.net.id.coremodul.CustomModel;

public class MenuRiwayatAdapter extends RecyclerView.Adapter<MenuRiwayatAdapter.ViewHolder>{

    private Context mContext;
    private List<CustomModel> customModels;

    public MenuRiwayatAdapter(Context context, List<CustomModel> menuHomeModels){
        this.mContext= context;
        this.customModels = menuHomeModels;
    }

    @NonNull
    @Override
    public MenuRiwayatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_menu_home, parent, false);
        return new MenuRiwayatAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuRiwayatAdapter.ViewHolder holder, int position) {
        final CustomModel item = customModels.get(position);
        holder.tvMenu.setText(item.getItem2());
        Glide.with(mContext).load(Integer.parseInt(item.getItem4())).into(holder.ivMenu);
        holder.rlBgMenu.setBackground(ContextCompat.getDrawable(mContext, Integer.parseInt(item.getItem5())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.getItem3().equals("")){
                    try {
                        mContext.startActivity(new Intent(mContext, Class.forName(item.getItem3())));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(mContext, item.getItem2(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return customModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cvMenu;
        private RelativeLayout rlBgMenu;
        private ImageView ivMenu;
        private TextView tvMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cvMenu = itemView.findViewById(R.id.cv_menu);
            rlBgMenu = itemView.findViewById(R.id.rl_bg_menu);
            ivMenu = itemView.findViewById(R.id.iv_icon);
            tvMenu = itemView.findViewById(R.id.tv_icon_name);
        }
    }
}
package gmedia.net.id.OnTime.home.menu.adapter;

import android.app.Activity;
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
import gmedia.net.id.OnTime.home.menu.model.MenuHomeModel;

public class MenuHomeAdapter extends RecyclerView.Adapter<MenuHomeAdapter.MenuHomeViewHolder>{

    private Context mContext;
    private List<MenuHomeModel> menuHomeModels;

    public MenuHomeAdapter(Context context, List<MenuHomeModel> menuHomeModels){
        this.mContext= context;
        this.menuHomeModels = menuHomeModels;
    }

    @NonNull
    @Override
    public MenuHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_menu_home, parent, false);
        return new MenuHomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHomeViewHolder holder, int position) {
        final MenuHomeModel item = menuHomeModels.get(position);
        holder.tvMenu.setText(item.getNameIcon());
        holder.rlBgMenu.setBackground(ContextCompat.getDrawable(mContext, item.getBgMenu()));
        Glide.with(mContext).load(item.getImgIcon()).into(holder.ivMenu);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!item.getLinkMenu().equals("")){
                    Intent intent = null;
                    try {
                        intent = new Intent(mContext, Class.forName(item.getLinkMenu()));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    mContext.startActivity(intent);
                    ((Activity)mContext).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    Toast.makeText(mContext, item.getLinkMenu(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return menuHomeModels.size();
    }

    public class MenuHomeViewHolder extends RecyclerView.ViewHolder {
        private CardView cvMenu;
        private RelativeLayout rlBgMenu;
        private ImageView ivMenu;
        private TextView tvMenu;
        public MenuHomeViewHolder(@NonNull View itemView) {
            super(itemView);
            cvMenu = itemView.findViewById(R.id.cv_menu);
            rlBgMenu = itemView.findViewById(R.id.rl_bg_menu);
            ivMenu = itemView.findViewById(R.id.iv_icon);
            tvMenu = itemView.findViewById(R.id.tv_icon_name);
        }
    }
}

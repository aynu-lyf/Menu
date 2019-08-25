package lyf.aynu.menu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import lyf.aynu.menu.R;
import lyf.aynu.menu.activity.StepActivity;
import lyf.aynu.menu.bean.DishBean;

/**
 * Created by asus on 2019/5/22.
 */

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private List<DishBean> dbl;
    private Context context;

    public HomeListAdapter(Context context){
        this.context = context;
    }

    public void setData(List<DishBean> dbl) {
        this.dbl = dbl;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_image;
        public TextView tv_title,tv_imtro;
        private View listView;

        public ViewHolder(View view) {
            super(view);
            listView = view;
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            tv_title = (TextView)view.findViewById(R.id.tv_title);
            tv_imtro = (TextView)view.findViewById(R.id.tv_imtro);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DishBean dish = dbl.get(position);
        holder.tv_title.setText(dish.getTitle());
        holder.tv_imtro.setText(dish.getImtro());
        Glide.with(context)
                .load(dish.getAlbums().get(0))
                .error(R.mipmap.ic_launcher)
                .into(holder.iv_image);
        holder.listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,StepActivity.class);
                intent.putExtra("dish",dish);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dbl == null ? 0:dbl.size();
    }
}

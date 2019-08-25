package lyf.aynu.menu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import lyf.aynu.menu.IItem;
import lyf.aynu.menu.R;
import lyf.aynu.menu.bean.StepBean;

/**
 * Created by asus on 2019/5/24.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private List<StepBean> stepBeanList;
    private Context context;
    private IItem iItem;

    public StepAdapter(Context context) {
        this.context = context;
    }
    public void setiItem(IItem iItem){
        this.iItem = iItem;
    }

    public void setData(List<StepBean> stepBeanList) {
        this.stepBeanList = stepBeanList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_img;
        public TextView tv_step;
        public ViewHolder(View view) {
            super(view);
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
            tv_step = (TextView) view.findViewById(R.id.tv_step);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        StepBean stepBean = stepBeanList.get(position);
        holder.tv_step.setText(stepBean.getStep());
        Glide.with(context)
                .load(stepBean.getImg())
                .error(R.mipmap.ic_launcher)
                .into(holder.iv_img);
        holder.iv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iItem.setOnItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepBeanList == null ? 0: stepBeanList.size();
    }

}

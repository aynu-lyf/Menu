package lyf.aynu.menu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import lyf.aynu.menu.IItem;
import lyf.aynu.menu.R;
import lyf.aynu.menu.bean.InnerBean;

/**
 * Created by Administrator on 2019/6/6 0006.
 */

public class InnerAdapter extends RecyclerView.Adapter<InnerAdapter.ViewHolder> {

    private List<InnerBean> ibl;
    private Context context;
    private IItem iItem;
    private int selectedIndex;        //记录当前选中的条目索引

    public InnerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<InnerBean> ibl) {
        this.ibl = ibl;
        setSelectedIndex(0);
        notifyDataSetChanged();
    }

    public void setiItem(IItem iItem){
        this.iItem = iItem;
    }

    public void setSelectedIndex(int position) {
        this.selectedIndex = position;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.outer_inner_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final InnerBean bean = ibl.get(position);
        holder.tv_label.setText(bean.getName());
        if (selectedIndex == position) {
            holder.tv_label.setTextColor(context.getResources().getColor(R.color.rdTextColorPress));           //选中状态
        } else {                                                            //非选中状态
            holder.tv_label.setTextColor(context.getResources().getColor(R.color.rdTextColorNormal));
        }
        holder.listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iItem.setOnItem(position);
                setSelectedIndex(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ibl == null ? 0 : ibl.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_label;
        private View listView;
        public ViewHolder(View itemView) {
            super(itemView);
            listView = itemView;
            tv_label = (TextView) itemView.findViewById(R.id.tv_label);
        }
    }
}

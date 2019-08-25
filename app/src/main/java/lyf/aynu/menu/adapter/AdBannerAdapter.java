package lyf.aynu.menu.adapter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import lyf.aynu.menu.bean.DishBean;
import lyf.aynu.menu.fragment.AdBannerFragment;
import lyf.aynu.menu.fragment.HomeFragment;

public class AdBannerAdapter extends FragmentStatePagerAdapter implements View.OnTouchListener {
    private Handler mHandler;
    private List<DishBean> abl;
    public AdBannerAdapter(FragmentManager fm, Handler handler) {
        super(fm);
        mHandler = handler;
        abl = new ArrayList<DishBean>();
    }
    /**
     *  设置数据更新界面
     */
    public void setData(List<DishBean> abl) {
        this.abl = abl;
        notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int index) {
        Bundle args = new Bundle();
        if (abl.size() > 0)
            args.putSerializable("ad", abl.get(index % abl.size()));
        return AdBannerFragment.newInstance(args);
    }
    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
    /**
     * 返回数据集的真实容量大小
     */
    public int getSize() {
        return abl == null ? 0 : abl.size();
    }
    /**
     * 获取广告名称
     */
    public String getTitle(int index) {
        return abl == null ? null : abl.get(index).getTitle();
    }
    @Override
    public int getItemPosition(Object object) {
        //防止刷新结果显示列表的时候出现缓存数据,重载这个函数,使之默认返回POSITION_NONE
        return POSITION_NONE;
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mHandler.removeMessages(HomeFragment.MSG_AD_SLID);
        return false;
    }
}

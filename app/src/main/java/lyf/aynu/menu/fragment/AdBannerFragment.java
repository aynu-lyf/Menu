package lyf.aynu.menu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import lyf.aynu.menu.R;
import lyf.aynu.menu.activity.StepActivity;
import lyf.aynu.menu.bean.DishBean;

/**
 * Created by asus on 2019/4/6.
 */

public class AdBannerFragment extends Fragment {
    private DishBean db;   //广告
    private ImageView iv;  //图片
    public static AdBannerFragment newInstance(Bundle args) {
        AdBannerFragment af = new AdBannerFragment();
        af.setArguments(args);
        return af;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        db = (DishBean) arg.getSerializable("ad"); //获取一个新闻对象
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            //调用Glide框架加载图片
            Glide
                    .with(getActivity())
                    .load(db.getAlbums().get(0))
                    .error(R.mipmap.ic_launcher)
                    .into(iv);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iv = new ImageView(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        iv.setLayoutParams(lp);                           //设置图片宽高参数
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //iv.setScaleType(ImageView.ScaleType.FIT_XY); //把图片填满整个控件
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db == null) return;
                Intent intent = new Intent(getActivity(), StepActivity.class);
                intent.putExtra("dish", db);
                getActivity().startActivity(intent);
            }
        });
        return iv;
    }
}

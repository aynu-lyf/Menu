package lyf.aynu.menu.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lyf.aynu.menu.R;
import lyf.aynu.menu.adapter.AdBannerAdapter;
import lyf.aynu.menu.adapter.HomeListAdapter;
import lyf.aynu.menu.bean.DishBean;
import lyf.aynu.menu.utils.Constant;
import lyf.aynu.menu.utils.JsonParse;
import lyf.aynu.menu.utils.UtilsHelper;
import lyf.aynu.menu.view.ViewPagerIndicator;
import lyf.aynu.menu.view.WrapRecyclerView;

/**
 * Created by asus on 2019/5/20.
 */

public class HomeFragment extends Fragment {

    private static final int RN = 8;
    private SwipeRefreshLayout swipeRefresh;
    private WrapRecyclerView recyclerView;
    private ViewPager adPager;         //广告
    private ViewPagerIndicator vpi;  //小圆点
    private TextView tvAdName;        //广告名称
    private View adBannerLay;         //广告条容器
    private AdBannerAdapter ada; //适配器
    public static final int MSG_AD_SLID = 1;  //广告自动滑动
    public static final int MSG_AD_OK = 2;    //获取广告数据
    private MHandler mHandler;                  //事件捕获
    private OkHttpClient okHttpClient;
    private HomeListAdapter adapter;
    private List<DishBean> dbl;

    private LinearLayout ll_loading;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        okHttpClient = new OkHttpClient();
        mHandler = new MHandler();
        getADData();
        View view = initView(inflater, container);
        new getDataTask().execute();
        return view;
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ll_loading = (LinearLayout)view.findViewById(R.id.ll_loading);

        recyclerView = (WrapRecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        View adbannerView = inflater.inflate(R.layout.main_adbanner, container, false);
        recyclerView.addHeaderView(adbannerView);
        adapter = new HomeListAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getADData();
                dbl.clear();
                new getDataTask().execute();
            }
        });

        adBannerLay = adbannerView.findViewById(R.id.adbanner_layout);
        adPager = (ViewPager) adbannerView.findViewById(R.id.slidingAdvertBanner);
        vpi = (ViewPagerIndicator) adbannerView.findViewById(R.id.advert_indicator);
        tvAdName = (TextView) adbannerView.findViewById(R.id.tv_advert_title);
        adPager.setLongClickable(false);
        ada = new AdBannerAdapter(getActivity().getSupportFragmentManager(), mHandler);
        adPager.setAdapter(ada);
        adPager.setOnTouchListener(ada);
        adPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                if (ada.getSize() > 0) {
                    if (ada.getTitle(index % ada.getSize()) != null) {
                        tvAdName.setText(ada.getTitle(index % ada.getSize()));
                    }
                    vpi.setCurrentPostion(index % ada.getSize());
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        resetSize();
        new AdAutoSlidThread().start();
        return view;
    }

    private void getADData() {

        Request request = new Request.Builder()
                .url(Constant.WEB_SITE + Constant.REQUEST_AD_URL)
                .build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Response response) throws IOException {
                String res = res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_AD_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

    /**
     * 通过菜谱ID请求数据
     * @param id
     * @return
     */
    private DishBean getDishInfo(String id) {
        Request request = new Request.Builder()
                .url(Constant.REQUEST_QUERYID_URL + id + Constant.MENU_KEY)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String dishResult = response.body().string();
            DishBean dishBean = JsonParse.getInstance().getDishInfo(dishResult);
            if (dishBean != null) {
                return dishBean;
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    class getDataTask extends AsyncTask<Void,Void,List<DishBean>>{

        @Override
        protected void onPreExecute() {
            ll_loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<DishBean> doInBackground(Void... params) {
            List<DishBean> dishBeanList = new ArrayList<>();
            Random random = new Random();
            int id = random.nextInt(1000) + 1;
            for (int i = 0; i < RN; i++) {
                int sub = random.nextInt(100) + 1;
                id += sub;
                DishBean db = getDishInfo(id + "");
                if (db == null){
                    break;
                }
                dishBeanList.add(db);
            }
            return dishBeanList;
        }
        @Override
        protected void onPostExecute(List<DishBean> dishBeanList) {
            ll_loading.setVisibility(View.GONE);
            dbl = dishBeanList;
            swipeRefresh.setRefreshing(false);
            if (dbl != null) {
                if (dbl.size() > 0) {
                    adapter.setData(dbl);
                }else{
                    Toast.makeText(getContext(), "很抱歉，由于API请求次数上限，无法再显示菜谱信息", Toast.LENGTH_LONG).show();
                }
            } else {
                adapter.setData(null);
                Toast.makeText(getContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * 事件捕获
     */
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_AD_SLID:
                    if (ada.getCount() > 0) {
                        adPager.setCurrentItem(adPager.getCurrentItem() + 1);
                    }
                    break;
                case MSG_AD_OK:
                    if (msg.obj != null) {
                        String adResult = (String) msg.obj;
                        List<DishBean> adl = JsonParse.getInstance().getAdList(adResult);
                        if (adl != null) {
                            if (adl.size() > 0) {
                                ada.setData(adl);
                                tvAdName.setText(adl.get(0).getTitle());
                                vpi.setCount(adl.size());
                                vpi.setCurrentPostion(0);
                            }
                        }
                    }
                    break;
            }
        }
    }

    class AdAutoSlidThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mHandler != null)
                    mHandler.sendEmptyMessage(MSG_AD_SLID);
            }
        }
    }

    /**
     * 计算控件大小
     */
    private void resetSize() {
        int sw = UtilsHelper.getScreenWidth(getActivity());
        int adLheight = sw / 2; //广告条高度
        ViewGroup.LayoutParams adlp = adBannerLay.getLayoutParams();
        adlp.width = sw;
        adlp.height = adLheight;
        adBannerLay.setLayoutParams(adlp);
    }
}

package lyf.aynu.menu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import lyf.aynu.menu.IItem;
import lyf.aynu.menu.R;
import lyf.aynu.menu.adapter.HomeListAdapter;
import lyf.aynu.menu.adapter.InnerAdapter;
import lyf.aynu.menu.adapter.OuterAdapter;
import lyf.aynu.menu.bean.DishBean;
import lyf.aynu.menu.bean.InnerBean;
import lyf.aynu.menu.bean.OuterBean;
import lyf.aynu.menu.utils.Constant;
import lyf.aynu.menu.utils.JsonParse;

/**
 * Created by asus on 2019/5/30.
 */

public class SortFragment extends Fragment {

    public static final int MSG_OUTER_OK = 1;  //外围数据获取
    public static final int MSG_SHOW_OK = 2;    //展示数据获取
    private OkHttpClient okHttpClient;
    private MHandler mHandler;
    private List<OuterBean> obl;
    private List<InnerBean> ibl;
    private List<DishBean> dbl;
    private OuterAdapter outerAdapter;
    private RecyclerView outer_recycler;
    private RecyclerView inner_recycler;
    private InnerAdapter innerAdapter;
    private RecyclerView show_recycler;
    private HomeListAdapter showAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        okHttpClient = new OkHttpClient();
        mHandler = new MHandler();
        getSortData();

        View view = initView(inflater, container);

        return view;
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_sort, container, false);

        outer_recycler = view.findViewById(R.id.outer_recycler);
        LinearLayoutManager outerLayoutManager = new LinearLayoutManager(getActivity());
        outerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        outer_recycler.setLayoutManager(outerLayoutManager);
        outerAdapter = new OuterAdapter(getContext());
        outer_recycler.setAdapter(outerAdapter);

        inner_recycler = view.findViewById(R.id.inner_recycler);
        LinearLayoutManager innerLayoutManager = new LinearLayoutManager(getActivity());
        inner_recycler.setLayoutManager(innerLayoutManager);
        innerAdapter = new InnerAdapter(getContext());
        inner_recycler.setAdapter(innerAdapter);

        show_recycler = view.findViewById(R.id.show_recycler);
        LinearLayoutManager showLayoutManager = new LinearLayoutManager(getActivity());
        show_recycler.setLayoutManager(showLayoutManager);
        showAdapter = new HomeListAdapter(getContext());
        show_recycler.setAdapter(showAdapter);

        outerAdapter.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {
                ibl = obl.get(position).getList();
                innerAdapter.setData(ibl);
                getShowData(ibl.get(0).getId());
                inner_recycler.setAdapter(innerAdapter);

            }
        });

        innerAdapter.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {
                String id = ibl.get(position).getId();
                getShowData(id);
                show_recycler.setAdapter(showAdapter);
            }
        });


        return view;
    }

    public void getShowData(String id) {
        Request request = new Request.Builder()
                .url(Constant.REQUEST_INDEX_URL + id + Constant.MENU_KEY)
                .build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Response response) throws IOException {
                String res = res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_SHOW_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

    public void getSortData() {
        Request request = new Request.Builder()
                .url(Constant.REQUEST_CATEGORY_URL + Constant.MENU_KEY)
                .build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Response response) throws IOException {
                String res = res = response.body().string();
                Message msg = new Message();
                msg.what = MSG_OUTER_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }

    /**
     * 事件捕获
     */
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_OUTER_OK:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        List<OuterBean> outerList = JsonParse.getInstance().getOuterList(result);
                        if (outerList != null) {
                            if (outerList.size() > 0) {
                                outerAdapter.setData(outerList);
                                innerAdapter.setData(outerList.get(0).getList());
                                getShowData(outerList.get(0).getList().get(0).getId());
                                obl = outerList;

                            }
                        }
                    }
                    break;
                case MSG_SHOW_OK:
                    if (msg.obj != null) {
                        String result = (String) msg.obj;
                        dbl = JsonParse.getInstance().getDishsList(result);
                        if (dbl != null) {
                            if (dbl.size() > 0) {
                                showAdapter.setData(dbl);
                            }
                        }
                    }
                    break;
            }
        }
    }
}

package lyf.aynu.menu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lyf.aynu.menu.R;
import lyf.aynu.menu.adapter.CollectionAdapter;
import lyf.aynu.menu.bean.DishBean;
import lyf.aynu.menu.utils.Constant;
import lyf.aynu.menu.utils.DBUtils;
import lyf.aynu.menu.utils.JsonParse;
import lyf.aynu.menu.utils.UtilsHelper;

public class CollectionActivity extends AppCompatActivity implements
        CollectionAdapter.IonSlidingViewClickListener {
    private RecyclerView mRecyclerView;
    private CollectionAdapter mAdapter;
    private TextView tv_main_title, tv_back, tv_none;
    private RelativeLayout rl_title_bar;
    private OkHttpClient okHttpClient;
    private DBUtils db;
    private List<DishBean> dishList;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        okHttpClient = new OkHttpClient();
        db = DBUtils.getInstance(CollectionActivity.this);
        userName = UtilsHelper.readLoginUserName(CollectionActivity.this);
        initView();
        setAdapter();
    }
    private void initView() {
        dishList = new ArrayList<>();
        List<String> idList = db.getCollectionNewsInfo(userName);
        for (int i = 0;i<idList.size();i++){
            DishBean bean = getDishInfo(idList.get(i));
            dishList.add(bean);
        }
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("收藏");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.
                rdTextColorPress));
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recyclerView);
        tv_none = (TextView) findViewById(R.id.tv_none);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionActivity.this.finish();
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void setAdapter() {
        mAdapter = new CollectionAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(dishList);
        if (dishList.size() == 0) tv_none.setVisibility(View.VISIBLE);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(CollectionActivity.this, StepActivity.class);
        intent.putExtra("dish", dishList.get(position));
        intent.putExtra("position", position + "");
        startActivityForResult(intent, 1);
    }
    @Override
    public void onDeleteBtnCilck(View view, int position) {
        mAdapter.removeData(position, tv_none, userName);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String position = data.getStringExtra("position");
            mAdapter.removeData(Integer.parseInt(position), tv_none, userName);
        }
    }
}

package lyf.aynu.menu.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import lyf.aynu.menu.R;
import lyf.aynu.menu.adapter.HomeListAdapter;
import lyf.aynu.menu.bean.DishBean;
import lyf.aynu.menu.utils.Constant;
import lyf.aynu.menu.utils.JsonParse;

public class SearchActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout title_bar;
    private RecyclerView search_recycler;
    private LinearLayout ll_not_find;
    private EditText et_search;
    private TextView tvSearch;
    private String dishName;

    private HomeListAdapter adapter;
    private List<DishBean> dbl;
    private OkHttpClient okHttpClient;
    private static final int GET_OK = 1;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GET_OK && msg.obj != null){
                String dishResult = (String) msg.obj;
                dbl = JsonParse.getInstance().getDishsList(dishResult);
                if (dbl != null) {
                    if (dbl.size() > 0) {
                        adapter.setData(dbl);
                        search_bg.setVisibility(View.GONE);
                        ll_not_find.setVisibility(View.GONE);
                        search_recycler.setAdapter(adapter);
                    }
                }else{
                    adapter.setData(null);
                    search_bg.setVisibility(View.GONE);
                    ll_not_find.setVisibility(View.VISIBLE);
                    Toast.makeText(SearchActivity.this, "未搜索到结果", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(SearchActivity.this, "数据传递失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private LinearLayout search_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        okHttpClient = new OkHttpClient();
        initView();
    }

    private void initView() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("搜索菜谱");
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.this.finish();
            }
        });
        title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        search_bg = (LinearLayout) findViewById(R.id.search_bg);
        search_bg.setVisibility(View.VISIBLE);
        et_search = (EditText) findViewById(R.id.et_search);
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dishName = et_search.getText().toString();
                if (TextUtils.isEmpty(dishName)){
                    Toast.makeText(SearchActivity.this, "请输入要搜索的菜名", Toast.LENGTH_SHORT).show();
                }else{
                    getData(dishName);
                }
            }
        });

        ll_not_find = (LinearLayout) findViewById(R.id.ll_not_find);
        search_recycler = (RecyclerView) findViewById(R.id.search_recycler);
        search_recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HomeListAdapter(this);

    }

    public void getData(String dishName) {
        Request request = new Request.Builder()
                .url(Constant.REQUEST_QUERY_URL +dishName+ Constant.MENU_KEY)
                .build();
        Call call = okHttpClient.newCall(request);
        //开启异步线程访问网络
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Response response) throws IOException {
                String res = response.body().string();
                Message msg = new Message();
                msg.what = GET_OK;
                msg.obj = res;
                handler.sendMessage(msg);
            }
            @Override
            public void onFailure(Request arg0, IOException arg1) {
            }
        });
    }
}

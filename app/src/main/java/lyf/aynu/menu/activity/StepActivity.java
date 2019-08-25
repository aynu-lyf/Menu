package lyf.aynu.menu.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import lyf.aynu.menu.IItem;
import lyf.aynu.menu.R;
import lyf.aynu.menu.adapter.StepAdapter;
import lyf.aynu.menu.bean.DishBean;
import lyf.aynu.menu.bean.StepBean;
import lyf.aynu.menu.fragment.StepFragment;
import lyf.aynu.menu.utils.DBUtils;
import lyf.aynu.menu.utils.UtilsHelper;
import lyf.aynu.menu.view.WrapRecyclerView;

public class StepActivity extends AppCompatActivity {

    private TextView tv_main_title;
    private TextView tv_back;
    private RelativeLayout title_bar;
    private WrapRecyclerView step_recycler;
    private List<StepBean> stepBeanList;
    private StepAdapter adapter;
    private String position;
    private ImageView iv_collection;
    private boolean isCollection = false;
    private DBUtils db;
    private String userName;
    private DishBean bean;
    private RelativeLayout rl_bigImg;
    private ImageView iv_bigImg;

    private ImageView iv_album;
    private TextView tv_ingredients;
    private TextView tv_burden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        Intent intent = getIntent();
        bean = (DishBean) intent.getSerializableExtra("dish");
        position = getIntent().getStringExtra("position");
        stepBeanList = bean.getSteps();
        db=DBUtils.getInstance(StepActivity.this);
        userName= UtilsHelper.readLoginUserName(StepActivity.this);
        init();
    }

    private void init() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText(bean.getTitle());
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepActivity.this.finish();
            }
        });
        title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));

        iv_collection = (ImageView) findViewById(R.id.iv_collection);
        iv_collection.setVisibility(View.VISIBLE);
        if(db.hasCollectionNewsInfo(bean.getId(),userName)){
            iv_collection.setImageResource(R.drawable.collection_selected);
            isCollection=true;
        }else{
            iv_collection.setImageResource(R.drawable.collection_normal);
            isCollection=false;
        }

        iv_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UtilsHelper.readLoginStatus(StepActivity.this)) {
                    if (isCollection) {
                        iv_collection.setImageResource(R.drawable.collection_normal);
                        isCollection = false;
                        //删除保存到新闻收藏数据库中的数据
                        db.delCollectionNewsInfo(bean.getId(), userName);
                        Toast.makeText(StepActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                        Intent data = new Intent();
                        data.putExtra("position", position);
                        setResult(RESULT_OK, data);
                    } else {
                        iv_collection.setImageResource(R.drawable.collection_selected);
                        isCollection = true;
                        //把该数据保存到新闻收藏数据库中
                        db.saveCollectionNewsInfo(bean, userName);
                        Toast.makeText(StepActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(StepActivity.this, "您还未登录，请先登录",Toast.LENGTH_SHORT).
                            show();
                }
            }
        });


        step_recycler = (WrapRecyclerView) findViewById(R.id.step_recycler);
        step_recycler.setLayoutManager(new LinearLayoutManager(this));

        View step_head_view = LayoutInflater.from(this).inflate(R.layout.step_head,null,false);
        iv_album = (ImageView) step_head_view.findViewById(R.id.iv_album);
        tv_ingredients = (TextView) step_head_view.findViewById(R.id.tv_ingredients);
        tv_burden = (TextView) step_head_view.findViewById(R.id.tv_burden);

        Glide.with(this)
                .load(bean.getAlbums().get(0))
                .error(R.mipmap.ic_launcher)
                .into(iv_album);

        tv_ingredients.setText("主料："+bean.getIngredients());
        tv_burden.setText("调料："+bean.getBurden());

        step_recycler.addHeaderView(step_head_view);

        adapter = new StepAdapter(this);
        adapter.setData(stepBeanList);
        step_recycler.setAdapter(adapter);

        rl_bigImg = (RelativeLayout) findViewById(R.id.rl_bigImg);
        iv_bigImg = (ImageView) findViewById(R.id.iv_bigImg);

        adapter.setiItem(new IItem() {
            @Override
            public void setOnItem(int position) {
                rl_bigImg.setVisibility(View.VISIBLE);
                Glide.with(StepActivity.this)
                        .load(stepBeanList.get(position).getImg())
                        .error(R.mipmap.ic_launcher)
                        .into(iv_bigImg);
            }
        });

        rl_bigImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_bigImg.setVisibility(View.GONE);
            }
        });
    }
}

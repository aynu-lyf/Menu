package lyf.aynu.menu;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lyf.aynu.menu.activity.SearchActivity;
import lyf.aynu.menu.adapter.MyFragmentPagerAdapter;
import lyf.aynu.menu.fragment.HomeFragment;
import lyf.aynu.menu.fragment.MeFragment;
import lyf.aynu.menu.fragment.SortFragment;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView tv_main_title;
    private TextView tv_search;
    private RelativeLayout rl_title_bar;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        initView();
    }
    private void initView() {
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("首页");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.rdTextColorPress));
        tv_search = (TextView) findViewById(R.id.tv_search);
        tv_search.setVisibility(View.VISIBLE);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        //RadioGroup选中状态改变监听
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        //setCurrentItem()中第二个参数控制页面切换动画,true:打开,false:关闭
                        viewPager.setCurrentItem(0, false);
                        break;
                    case R.id.rb_sort:
                        viewPager.setCurrentItem(1, false);
                        break;
                    case R.id.rb_me:
                        viewPager.setCurrentItem(2, false);
                        break;
                }
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        List<Fragment> alFragment = new ArrayList<Fragment>();
        HomeFragment homeFragment = new HomeFragment();
        SortFragment sortFragment = new SortFragment();
        MeFragment meFragment = new MeFragment();
        alFragment.add(homeFragment);
        alFragment.add(sortFragment);
        alFragment.add(meFragment);

        viewPager.setOffscreenPageLimit(2); //三个界面之间来回切换都不会重新加载数据。

        //ViewPager设置适配器

        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), alFragment));
        viewPager.setCurrentItem(0); //ViewPager显示第一个Fragment

        //ViewPager页面切换监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGroup.check(R.id.rb_home);
                        tv_main_title.setText("首页");
                        rl_title_bar.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        radioGroup.check(R.id.rb_sort);
                        tv_main_title.setText("分类");
                        rl_title_bar.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        radioGroup.check(R.id.rb_me);
                        rl_title_bar.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    protected long exitTime;    //记录第一次点击的时间

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出菜谱", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


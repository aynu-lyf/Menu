package lyf.aynu.menu.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import lyf.aynu.menu.activity.CollectionActivity;
import lyf.aynu.menu.activity.LoginActivity;
import lyf.aynu.menu.activity.SettingActivity;
import lyf.aynu.menu.activity.UserInfoActivity;
import lyf.aynu.menu.receiver.UpdateUserInfoReceiver;
import lyf.aynu.menu.utils.DBUtils;
import lyf.aynu.menu.utils.UtilsHelper;
import lyf.aynu.menu.R;

/**
 * Created by Administrator on 2019/5/8 0008.
 */

public class MeFragment extends Fragment implements View.OnClickListener {

    private boolean isLogin = false;

    private View view;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private RelativeLayout rl_collection;
    private RelativeLayout rl_setting;
    private CircleImageView iv_avatar;
    private UpdateUserInfoReceiver updateUserInfoReceiver;
    private IntentFilter filter;

    public MeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rl_collection = (RelativeLayout) view.findViewById(R.id.rl_collection);
        rl_setting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        iv_avatar = (CircleImageView) view.findViewById(R.id.iv_avatar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_tool_bar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ToolbarTitle);

        isLogin = UtilsHelper.readLoginStatus(getActivity());
        setLoginParams(isLogin);
        setListener();
        receiver();
    }

    private void receiver() {
        updateUserInfoReceiver = new UpdateUserInfoReceiver(new UpdateUserInfoReceiver.BaseOnReceiveMsgListener() {
            @Override
            public void onReceiveMsg(Context context, Intent intent) {
                String action = intent.getAction();
                if (UpdateUserInfoReceiver.ACTION.UPDATE_USERINFO.equals(action)){
                    String type = intent.getStringExtra(UpdateUserInfoReceiver.INTENT_TYPE.TYPE_NAME);
                    //更新头像
                    if (UpdateUserInfoReceiver.INTENT_TYPE.UPDATE_HEAD.equals(type)){
                        String head = intent.getStringExtra("head");
                        Bitmap bt = BitmapFactory.decodeFile(head);
                        if (bt != null){
                            Drawable drawable = new BitmapDrawable(bt);
                            iv_avatar.setImageDrawable(drawable);
                        }else{
                            iv_avatar.setImageResource(R.drawable.default_head);
                        }
                    }
                }
            }
        });
        filter = new IntentFilter(UpdateUserInfoReceiver.ACTION.UPDATE_USERINFO);
        getActivity().registerReceiver(updateUserInfoReceiver,filter);
    }

    /**
     * 根据登录状态设置我的界面
     * @param isLogin
     */
    private void setLoginParams(boolean isLogin) {
        if (isLogin){
            String userName = UtilsHelper.readLoginUserName(getActivity());
            collapsingToolbarLayout.setTitle(userName);
            String head = DBUtils.getInstance(getActivity()).getUserHead(userName);
            Bitmap bt = BitmapFactory.decodeFile(head);
            if (bt != null){
                Drawable drawable = new BitmapDrawable(bt);
                iv_avatar.setImageDrawable(drawable);
            }else{
                iv_avatar.setImageResource(R.drawable.default_head);
            }
        }else{
            iv_avatar.setImageResource(R.drawable.default_head);
            collapsingToolbarLayout.setTitle("点击登录");
        }
    }

    private void setListener() {
        rl_collection.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        iv_avatar.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (updateUserInfoReceiver != null){
            getActivity().unregisterReceiver(updateUserInfoReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_collection:
                if (isLogin) {
                    //跳转到收藏界面
                    Intent collection = new Intent(getActivity(), CollectionActivity.class);
                    startActivity(collection);
                } else {
                    Toast.makeText(getActivity(), "您还未登录，请先登录",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_setting:
                if (isLogin) {
                    //跳转到设置界面
                    Intent settingIntent = new Intent(getActivity(), SettingActivity.class);
                    startActivityForResult(settingIntent, 1);
                } else {
                    Toast.makeText(getActivity(), "您还未登录，请先登录",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_avatar:
                if (isLogin) {
                    //跳转到个人资料界面
                    Intent userinfo = new Intent(getActivity(), UserInfoActivity.class);
                    startActivity(userinfo);
                } else {
                    //跳转到登录界面
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(login, 1);
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            boolean isLogin = data.getBooleanExtra("isLogin", false);
            setLoginParams(isLogin);
            this.isLogin = isLogin;
        }
    }
}

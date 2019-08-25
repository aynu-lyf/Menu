package lyf.aynu.menu.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import lyf.aynu.menu.bean.DishBean;
import lyf.aynu.menu.bean.OuterBean;

/**
 * Created by Administrator on 2019/3/27 0027.
 */

public class JsonParse {
    private static JsonParse instance;

    public JsonParse() {
    }

    public static JsonParse getInstance() {
        if (instance == null) {
            instance = new JsonParse();
        }
        return instance;
    }

    public List<DishBean> getAdList(String json) {
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        //创建一个TypeToken的匿名子类对象，并调用对象的getType()方法
        Type listType = new TypeToken<List<DishBean>>() {}.getType();
        //把获取到的信息集合存到adList中
        List<DishBean> adList = gson.fromJson(json, listType);
        return adList;
    }

    public List<DishBean> getDishsList(String json) {
        //使用gson库解析JSON数据
        Gson gson = new Gson();
        try {
            //实例化json对象
            JSONObject jsonObject = new JSONObject(json);
            //从就送json对象中获取result对象
            JSONObject result = jsonObject.getJSONObject("result");
            //从result对象中获取data数组
            JSONArray data = result.getJSONArray("data");
            //创建一个TypeToken的匿名子类对象，并调用对象的getType()方法
            Type listType = new TypeToken<List<DishBean>>() {}.getType();
            //把获取到的信息集合存到adList中
            List<DishBean> dbList = gson.fromJson(data.toString(), listType);
            return dbList;
        } catch (JSONException e) {
            System.out.println("当前API可请求的次数不足");
            return null;
        }
    }
    public DishBean getDishInfo(String json) {
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray data = result.getJSONArray("data");
            Type listType = new TypeToken<List<DishBean>>() {}.getType();
            List<DishBean> dishBean = gson.fromJson(data.toString(), listType);
            return dishBean.get(0);
        } catch (JSONException e) {
            System.out.println("当前API可请求的次数不足");
            return null;
        }
    }

    public List<OuterBean> getOuterList(String json){
        Gson gson = new Gson();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("result");
            Type listType = new TypeToken<List<OuterBean>>() {}.getType();
            List<OuterBean> outerList = gson.fromJson(result.toString(), listType);
            return outerList;
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("当前API可请求的次数不足");
            return null;
        }
    }
}

package lyf.aynu.menu.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lyf.aynu.menu.bean.DishBean;
import lyf.aynu.menu.bean.UserBean;
import lyf.aynu.menu.sqlite.SQLiteHelper;

public class DBUtils {
    private static DBUtils instance = null;
    private static SQLiteHelper helper;
    private static SQLiteDatabase db;

    public DBUtils(Context context) {
        helper = new SQLiteHelper(context);
        db = helper.getWritableDatabase();
    }

    public static DBUtils getInstance(Context context) {
        if (instance == null) {
            instance = new DBUtils(context);
        }
        return instance;
    }

    /*
 * 根据登录名获取用户头像
 */
    public String getUserHead(String userName) {
        String sql = "SELECT head FROM " + SQLiteHelper.U_USERINFO + " WHERE userName=?";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        String head = "";
        while (cursor.moveToNext()) {
            head = cursor.getString(cursor.getColumnIndex("head"));
        }
        cursor.close();
        return head;
    }

    /**
     * 保存个人资料信息
     */
    public void saveUserInfo(UserBean bean) {
        ContentValues cv = new ContentValues();
        cv.put("userName", bean.getUserName());
        cv.put("nickName", bean.getNickName());
        cv.put("sex", bean.getSex());
        cv.put("signature", bean.getSignature());
        db.insert(SQLiteHelper.U_USERINFO, null, cv);
    }

    /**
     * 获取个人资料信息
     */
    public UserBean getUserInfo(String userName) {
        String sql = "SELECT * FROM " + SQLiteHelper.U_USERINFO + " WHERE userName=?";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        UserBean bean = null;
        while (cursor.moveToNext()) {
            bean = new UserBean();
            bean.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
            bean.setNickName(cursor.getString(cursor.getColumnIndex("nickName")));
            bean.setSex(cursor.getString(cursor.getColumnIndex("sex")));
            bean.setSignature(cursor.getString(cursor.getColumnIndex("signature")));
            bean.setHead(cursor.getString(cursor.getColumnIndex("head")));
        }
        cursor.close();
        return bean;
    }

    /**
     * 修改个人资料
     */
    public void updateUserInfo(String key, String value, String userName) {
        ContentValues cv = new ContentValues();
        cv.put(key, value);
        db.update(SQLiteHelper.U_USERINFO, cv, "userName=?", new String[]{userName});
    }

    /**
     * 保存收藏信息
     */
    public void saveCollectionNewsInfo(DishBean bean, String userName) {
        ContentValues cv = new ContentValues();
        cv.put("id", bean.getId());
        cv.put("userName", userName);
        db.insert(SQLiteHelper.COLLECTION_DISH_INFO, null, cv);
    }

    /**
     * 获取收藏信息
     */
    public List<String> getCollectionNewsInfo(String userName) {
        String sql = "SELECT * FROM " + SQLiteHelper.COLLECTION_DISH_INFO
                + " WHERE  userName=? ";
        Cursor cursor = db.rawQuery(sql, new String[]{userName});
        List<String> idList = new ArrayList<>();
        String id = null;
        while (cursor.moveToNext()) {
            id = new String();
            id = cursor.getString(cursor.getColumnIndex("id"));
            idList.add(id);
        }
        cursor.close();
        return idList;
    }

    /**
     * 判断一条新闻是否被收藏
     */
    public boolean hasCollectionNewsInfo(String id, String userName) {
        boolean hasNewsInfo = false;
        String sql = "SELECT * FROM " + SQLiteHelper.COLLECTION_DISH_INFO
                + " WHERE id=? AND userName=?";
        Cursor cursor = db.rawQuery(sql, new String[]{id + "", userName + ""});
        if (cursor.moveToFirst()) {
            hasNewsInfo = true;
        }
        cursor.close();
        return hasNewsInfo;
    }

    /**
     * 删除某一条收藏信息
     */
    public boolean delCollectionNewsInfo(String id, String userName) {
        boolean delSuccess = false;
        if (hasCollectionNewsInfo(id, userName)) {
            int row = db.delete(SQLiteHelper.COLLECTION_DISH_INFO,
                    " id=? AND userName=? ", new String[]{id, userName});
            if (row > 0) {
                delSuccess = true;
            }
        }
        return delSuccess;
    }
}

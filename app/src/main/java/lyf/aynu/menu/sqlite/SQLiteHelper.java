package lyf.aynu.menu.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asus on 2019/5/8.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    public static String DB_NAME = "menu.db";

    public static final String U_USERINFO = "userinfo"; //用户信息
    public static final String COLLECTION_DISH_INFO = "collection_dish_info";   //收藏信息

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * 创建用户信息表
         */
        db.execSQL("CREATE TABLE  IF NOT EXISTS " + U_USERINFO + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "userName VARCHAR, "  //用户名
                + "nickName VARCHAR, "  //昵称
                + "sex VARCHAR, "        //性别
                + "signature VARCHAR,"  //签名
                + "head VARCHAR "        //头像
                + ")");

        /**
         * 创建收藏表
         */
        db.execSQL("CREATE TABLE  IF NOT EXISTS " + COLLECTION_DISH_INFO + "( "
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id INTEGER, "         //新闻id
                + "userName VARCHAR" //用户名
                + ")");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + U_USERINFO);
        db.execSQL("DROP TABLE IF EXISTS " + COLLECTION_DISH_INFO);
        onCreate(db);
    }
}

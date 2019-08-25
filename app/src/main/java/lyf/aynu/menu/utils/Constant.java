package lyf.aynu.menu.utils;

/**
 * Created by Administrator on 2019/3/27 0027.
 */

public class Constant {
    //内网接口
    public static final String WEB_SITE = "http://47.101.134.208:80/menu";
    //首页滑动广告接口
    public static final String REQUEST_AD_URL = "/home_ad_list_data.json";
    //按菜品名称搜索菜品接口
    public static final String REQUEST_QUERY_URL = "http://apis.juhe.cn/cook/query.php?rn=25&menu=";
    //按菜品ID查找菜品接口
    public static final String REQUEST_QUERYID_URL = "http://apis.juhe.cn/cook/queryid?id=";
    //分类标签列表接口
    public static final String REQUEST_CATEGORY_URL = "http://apis.juhe.cn/cook/category?";
    //通过标签ID搜索菜品接口
    public static final String REQUEST_INDEX_URL = "http://apis.juhe.cn/cook/index?cid=";

    //接口KEY
    public static final String MENU_KEY ="&key=74e6927c0866d672f904349e0128f7c6";

}

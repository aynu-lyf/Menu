package lyf.aynu.menu.bean;

import java.util.List;

/**
 * Created by asus on 2019/6/5.
 */

public class OuterBean {
    private String parentId;
    private String name;
    private List<InnerBean> list;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InnerBean> getList() {
        return list;
    }

    public void setList(List<InnerBean> list) {
        this.list = list;
    }
}

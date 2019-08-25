package lyf.aynu.menu.bean;

import java.io.Serializable;

/**
 * Created by asus on 2019/5/23.
 */

public class StepBean implements Serializable{
    private String img;
    private String step;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }
}

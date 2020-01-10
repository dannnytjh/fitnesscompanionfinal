package fitnesscompanion.com.Model;
/**
 * Created by Soon Kok Fung
 */

public class Feedback {
    private int no;
    private String desc;
    private float rate;
    private String createDate;

    public Feedback() {
    }

    public Feedback(int no, String desc, float rate, String createDate) {
        this.no = no;
        this.desc = desc;
        this.rate = rate;
        this.createDate = createDate;
    }
    public Feedback(String desc , float rate) {
        this.desc=desc;
        this.rate=rate;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}

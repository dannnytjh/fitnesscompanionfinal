package fitnesscompanion.com.Model;

/**
 * Created by Soon Kok Fung
 */

public class GoalUI {
    private int no;
    private int type;
    private int rate;
    private int status;


    public GoalUI() {
    }

    public GoalUI(int no, int type, int rate, int status) {
        this.no = no;
        this.type = type;
        this.rate = rate;
        this.status = status;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

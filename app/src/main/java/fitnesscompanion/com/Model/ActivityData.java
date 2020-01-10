package fitnesscompanion.com.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Soon Kok Fung
 */

public class ActivityData {

    private int no;
    private int duration;
    private int hr;
    private double distance;
    private String date;
    private int activityNo;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss");

    public ActivityData() {
    }

    public ActivityData(int no, int duration,String date , int hr,int activityNo, double distance) {
        this.no = no;
        this.duration = duration;
        this.hr = hr;
        this.date =date;
        this.activityNo = activityNo;
        this.distance = distance;
    }
    public ActivityData(int duration, int hr, int activityNo) {
        this.duration = duration;
        this.hr = hr;
        this.date =dateFormat.format(new Date());
        this.activityNo = activityNo;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getHr() {
        return hr;
    }

    public void setHr(int hr) {
        this.hr = hr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getActivityNo() {
        return activityNo;
    }

    public void setActivityNo(int activityNo) {
        this.activityNo = activityNo;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}

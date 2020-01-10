package fitnesscompanion.com.Model;

import java.util.ArrayList;
/**
 * Created by Soon Kok Fung
 */

public class ActivityAssigned {
    private int no;
    private String name;
    private ArrayList<Activity> activities;

    public ActivityAssigned() {
    }

    public ActivityAssigned(int no, String name, ArrayList<Activity> activities) {
        this.no = no;
        this.name = name;
        this.activities = activities;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}

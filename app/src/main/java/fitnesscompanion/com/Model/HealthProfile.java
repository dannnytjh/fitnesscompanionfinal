package fitnesscompanion.com.Model;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Soon Kok Fung
 */

public class HealthProfile {
    private int no;
    private int weight;
    private double height;
    private String created_at;
    private User user;

    public HealthProfile() {
    }
    public HealthProfile(int weight){
        this.weight=weight;
    }

    public HealthProfile( int no, int weight, double height, String created_at) {
        this.no = no;
        this.weight = weight;
        this.height = height;
        this.created_at = created_at;
    }

    public int getNo() {
        return no;
    }

    public int getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public double getBmi() {

        //no format
        return weight/ Math.pow(height,2)*10000;
    }
    public int getBmiStatus() {
        double bmi  = getBmi();
        if(bmi<18.5)
            return 0;
        else if(bmi>=18.5 & bmi<=24.9)
            return 1;
        else if(bmi>=25)
            return 2;

        return -1;
    }
    public int getBmiString() {
        double bmi  = getBmi();
        if(bmi<18.5)
            return 0;
        else if(bmi>=18.5 & bmi<=24.9)
            return 1;
        else if(bmi>=25)
            return 2;

        return -1;
    }
    public int getBmr() {
        if(user.getGender()==0)
            return (int)Math.round(10*weight+6.25*height-5*user.getAge()+5);
        else
            return (int)Math.round(10*weight+6.25*height-5*user.getAge()-161);
    }
}

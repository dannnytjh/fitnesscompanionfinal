package fitnesscompanion.com.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Soon Kok Fung
 */

public class Activity {
    private int activityId;
    private String name;
    private String description;
    private int calories;
    private int time;
    private String image;

    public Activity() {
    }

    public Activity(int activityId, String name, String description, int calories, int time, String image) {
        this.activityId = activityId;
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.time = time;
        this.image = image;
    }

    public int getNo() {
        return activityId;
    }

    public void setNo(int no) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Bitmap getImageFromJSon(){
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}

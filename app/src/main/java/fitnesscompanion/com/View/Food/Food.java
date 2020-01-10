package fitnesscompanion.com.View.Food;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import fitnesscompanion.com.R;

public class Food {

    private int id;
    private String name;
    private String mealType;
    private String date;
    private byte [] image;

    public Food(int id, String name, String mealType, String date, byte[] image) {
        this.id = id;
        this.name = name;
        this.mealType = mealType;
        this.date = date;
        this.image = image;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmealType() {
        return mealType;
    }

    public void setmealType(String mealType) {
        this.mealType = mealType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}


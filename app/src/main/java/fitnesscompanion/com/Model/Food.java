package fitnesscompanion.com.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Soon Kok Fung
 */

public class Food {

    private int no;
    private String name;
    private String barCode;
    private int calories;
    private double protein;
    private double fat;
    private double carbohydrate;
    private String image;
    private String restaurant;

    public Food() {
    }
    public Food(int no , String name,int calories,String image){
        this.no = no;
        this.name = name;
        this.calories = calories;
        this.image = image;
    }

    public Food(int no, String name, String barCode, int calories, double protein, double fat, double carbohydrate, String image, String restaurant) {
        this.no = no;
        this.name = name;
        this.barCode = barCode;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.image = image;
        this.restaurant = restaurant;
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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(double carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }
    public Bitmap getImageFromJSon(){
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
    public void encodeImagetoString(Bitmap bitmap) {

        if(bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            image = Base64.encodeToString(byte_arr, Base64.DEFAULT);
        }
    }
}

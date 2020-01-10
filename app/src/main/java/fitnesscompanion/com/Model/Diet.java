package fitnesscompanion.com.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Soon Kok Fung
 */

public class Diet {
    private int no;
    private String name;
    private int qty;
    private int type;
    private int calories;
    private byte[] image;
    private int foodNo;

    public Diet() {
    }

    public Diet(int no, String name, int qty, int type, int calories, byte[] image,int foodNo) {
        this.no = no;
        this.name = name;
        this.qty = qty;
        this.type = type;
        this.calories = calories;
        this.image = image;
        this.foodNo=foodNo;
    }

    public int getFoodNo() {
        return foodNo;
    }

    public void setFoodNo(int foodNo) {
        this.foodNo = foodNo;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Bitmap getImageFromJSon(){
        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;

    }
}

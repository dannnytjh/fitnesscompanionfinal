package fitnesscompanion.com.Model;

import java.util.ArrayList;
/**
 * Created by Soon Kok Fung
 */

public class Restaurant {
    private String name;
    private double distance;
    private ArrayList<Food> foodList;

    public Restaurant() {
    }

    public Restaurant(String name, double distance, ArrayList<Food> foodList) {
        this.name = name;
        this.distance = distance;
        this.foodList = foodList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<Food> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<Food> foodList) {
        this.foodList = foodList;
    }
}

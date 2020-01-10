package fitnesscompanion.com.Model;

/**
 * Created by Soon Kok Fung
 */

public class Ranking {
    private String name;
    private int calories;

    public Ranking() {
    }

    public Ranking(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }
}

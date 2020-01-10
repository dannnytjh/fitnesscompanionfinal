package fitnesscompanion.com.Model;

/**
 * Created by Soon Kok Fung
 */

public class Goal {
    private int goalId;
    private int type;
    private String description;
    private int measurement;
    private int standardGoalId;

    public Goal() {
    }

    /*public Goal(int rate) {
        this.rate = rate;
    }*/

    public Goal(int goalId, int type, String description, int measurement,int standardGoalId) {
        this.goalId = goalId;
        this.type = type;
        this.description = description;
        this.measurement = measurement;
        this.standardGoalId = standardGoalId;
    }
    public Goal(int goalId, int type, String description,int standardGoalId) {
        this.goalId = goalId;
        this.type = type;
        this.description = description;
        this.standardGoalId = standardGoalId;
    }

    public int getStandardGoalId() {
        return standardGoalId;
    }

    public int getGoalId() {
        return goalId;
    }

    public int getType() {
        return type;
    }

    public int getMeasurement() {
        return measurement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public void setMeasurement(int measurement) {
        this.measurement = measurement;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setStandardGoalId(int standardGoalId) {
        this.standardGoalId = standardGoalId;
    }
}

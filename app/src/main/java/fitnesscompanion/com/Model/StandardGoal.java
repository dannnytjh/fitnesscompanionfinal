package fitnesscompanion.com.Model;

public class StandardGoal {
    private int standardGoalId,foodIntake,activityDuration;
    private String createAt,goalName;

    public StandardGoal(){

    }
    public StandardGoal(int standardGoalId, String goalName,String createAt, int foodIntake, int activityDuration) {
        this.standardGoalId = standardGoalId;
        this.goalName = goalName;
        this.createAt = createAt;
        this.foodIntake = foodIntake;
        this.activityDuration = activityDuration;
    }
public StandardGoal(int standardGoalId){
        this.standardGoalId = standardGoalId;
}
    public int getStandardGoalId() {
        return standardGoalId;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getCreateAt() {
        return createAt;
    }

    public int getFoodIntake() {
        return foodIntake;
    }

    public int getActivityDuration() {
        return activityDuration;
    }

    public void setStandardGoalId(int standardGoalId) {
        this.standardGoalId = standardGoalId;
    }

    public void setFoodIntake(int foodIntake) {
        this.foodIntake = foodIntake;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public void setActivityDuration(int activityDuration) {
        this.activityDuration = activityDuration;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }
}

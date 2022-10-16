package Models;

public class TestModel {

    private String testID;
    private int topScore;
    private int time;
    private String testName;


    public TestModel(String testID, int topScore, int time,String testName) {
        this.testID = testID;
        this.topScore = topScore;
        this.time = time;
        this.testName=testName;


    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}

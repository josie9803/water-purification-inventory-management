package WaterPurificationSystem.Model;

import java.time.LocalDate;

/**
 * Test class models the information about a test inside a unit
 * Data includes LocalDate dateCreated, testPassed, testComment
 */

public class Test implements Comparable<Test>{
    private final LocalDate date;
    private final boolean isTestPassed;
    private final String testResultComment;

    public Test(LocalDate date, boolean isTestPassed, String testResultComment) {
        this.date = date;
        this.isTestPassed = isTestPassed;
        this.testResultComment = testResultComment;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isTestPassed() {
        return isTestPassed;
    }

    public String getTestResultComment() {
        return testResultComment;
    }

    @Override
    public String toString() {
        return "Test{" +
                "date=" + date +
                ", isTestPassed=" + isTestPassed +
                ", testResultComment='" + testResultComment + '\'' +
                '}';
    }

    public int compareTo(Test mostRecentTest) {
        return this.date.compareTo(mostRecentTest.date);
    }
}

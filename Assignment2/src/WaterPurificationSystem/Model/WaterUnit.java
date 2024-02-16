package WaterPurificationSystem.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * WaterUnit class models the information about a water unit in the system
 * Data includes serialNumber, model, list of Test, LocalDate dateShipped
 */

public class WaterUnit {
    private String serialNumber;
    private String model;
    private List<Test> tests;
    private LocalDate dateShipped;

    public WaterUnit(String serialNumber, String model, List<Test> tests, LocalDate dateShipped) throws Exception {
        SerialNumberValidator.validateSerialNumber(serialNumber);
        this.serialNumber = serialNumber;
        this.model = model;
        this.tests = tests;
        this.dateShipped = dateShipped;
    }

    public LocalDate getDateShipped() {
        return dateShipped;
    }

    public void setDateShipped(LocalDate dateShipped) {
        this.dateShipped = dateShipped;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) throws Exception {
        SerialNumberValidator.validateSerialNumber(serialNumber);
        this.serialNumber = serialNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Test> getTests() {
        return tests;
    }

    public Test getMostRecentTest(){
        return this.getTests().getLast();
    }

    public void setTests(Test test) {
        if (this.tests == null){
            List<Test> emptyTest = new ArrayList<>();
            emptyTest.add(test);
            this.tests = emptyTest;
        }
        else {this.tests.add(test);}
    }

    public boolean isShipped(){
        return dateShipped != null;
    }

}

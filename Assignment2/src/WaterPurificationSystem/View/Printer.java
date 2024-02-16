package WaterPurificationSystem.View;

import WaterPurificationSystem.Model.Test;
import WaterPurificationSystem.Model.WaterUnit;

import java.util.List;

/**
 * Generic class Printer displays the list of the objects in a pre-defined format
 */

public class Printer<T> {
    private static final int EXTRA_CHARACTERS_IN_TITLE = 4;

    private static void displayRowOfStars(int length) {
        for (int i = 0; i < length + EXTRA_CHARACTERS_IN_TITLE; i++) {
            System.out.print("*");
        }
        System.out.println();
    }

    public void printTitle(String title) {
        System.out.println(title);
        displayRowOfStars(title.length());
    }

    public void printHeader(String[] values) {
        for (String value : values) {
            printFormattedValue(value, value);
        }
        System.out.println();
    }

    public void printRowOfDash(String[] columns) {
        for (String column : columns) {
            printFormattedValue("-".repeat(getColumnWidth(column)), column);
        }
        System.out.println();
    }

    public void printValues(List<T> items, String[] columns) {
        for (T item : items) {
            for (String column : columns) {
                String value = getColumnValue(item, column);
                printFormattedValue(value, column);
            }
            System.out.println();
        }
    }

    public void displayTable(List<T> items, String title, String[] columns) {
        printTitle(title);
        if (items.isEmpty()){
            System.out.println("No units found.");
            return;
        }
        printHeader(columns);
        printRowOfDash(columns);
        printValues(items, columns);
    }

    private int getColumnWidth(String column) {
        switch (column) {
            case "Serial":
                return 15;
            case "Test Comments":
                return 25;
            default:
                return 10;
        }
    }

    private void printFormattedValue(String value, String column) {
        if (column.equals("Test Comments")){
            System.out.printf("%-" + getColumnWidth(column) + "s  ", value);
        }
        else {
            System.out.printf("%" + getColumnWidth(column) + "s  ", value);
        }
    }

    private String getColumnValue(T item, String column) {
        if (item instanceof WaterUnit) {
            return getColumnValue((WaterUnit) item, column);
        } else if (item instanceof Test) {
            return getColumnValue((Test) item, column);
        }
        return "";
    }

    private String getColumnValue(WaterUnit waterUnit, String column) {
        return switch (column) {
            case "Model" -> waterUnit.getModel();
            case "Serial" -> waterUnit.getSerialNumber();
            case "# Tests" -> waterUnit.getTests() != null ? String.valueOf(waterUnit.getTests().size()) : "0";
            case "Ship Date" -> waterUnit.getDateShipped() != null ? waterUnit.getDateShipped().toString() : "-";
            case "Test Date" -> waterUnit.getMostRecentTest().getDate().toString();
            case "Test Comments" -> waterUnit.getMostRecentTest().getTestResultComment();
            default -> "";
        };
    }

    private String getColumnValue(Test test, String column) {
        return switch (column) {
            case "Date" -> test.getDate().toString();
            case "Passed" -> test.isTestPassed() ? "Passed" : "FAILED";
            case "Test Comments" -> test.getTestResultComment();
            default -> "";
        };
    }
}

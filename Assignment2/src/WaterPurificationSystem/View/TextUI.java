package WaterPurificationSystem.View;

import WaterPurificationSystem.Model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * TextUI class displays the interface of application, interacting with user
 * TextUI class contains the main loop of the application
 */

public class TextUI {
    WaterUnitsManager waterUnits;

    public TextUI(WaterUnitsManager waterUnits) {
        this.waterUnits = waterUnits;
    }

    public void start() {
        displayTitle();

        GeneralTextMenu.MenuEntry[] menuEntries = new GeneralTextMenu.MenuEntry[]{
                new GeneralTextMenu.MenuEntry("Read JSON input file.", this::readJSONFilePath),
                new GeneralTextMenu.MenuEntry("Display info on a unit.", this::displayUnitInfo),
                new GeneralTextMenu.MenuEntry("Create new unit.", this::createUnit),
                new GeneralTextMenu.MenuEntry("Test a unit.", this::testUnit),
                new GeneralTextMenu.MenuEntry("Ship a unit.", this::shipUnit),
                new GeneralTextMenu.MenuEntry("Print report.", this::printReport),
                new GeneralTextMenu.MenuEntry("Set report sort order.", this::setReportSortOrder),
                new GeneralTextMenu.MenuEntry("Exit.", null),
        };

        GeneralTextMenu mainMenu = new GeneralTextMenu("Main Menu", menuEntries);
        mainMenu.doMenu();
    }
    private void displayTitle() {
        System.out.println("**********************************");
        System.out.println("Water Purification Inventory Management");
        System.out.println("by Josie.");
        System.out.println("**********************************");
    }
    private void readJSONFilePath(){
        Scanner in = new Scanner(System.in);
        String filePath;
        do {
            System.out.print("Enter the path to the input JSON file; blank to cancel.\n" +
                    "WARNING: This will replace all current data with data from the file.\n" + "> ");
            filePath = in.nextLine();
            try {
                waterUnits.loadJSONFile(filePath);
                return;
            } catch (RuntimeException e) {
                System.out.println("Error loading file: " + e.getMessage());
            }
        } while (!filePath.isEmpty());
    }
    private WaterUnit processUnitInput(){
        if(waterUnits.isEmpty()){
            System.out.println("No units defined.\n" +
                    "Please create a unit and then re-try this option.");
            return null;
        }
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.print("Enter the serial number (0 for list, -1 for cancel): ");
            String input = in.nextLine();
            if (input.equals("0")) {
                printAll();
            } else if (input.equals("-1")) {
                return null;
            }
            else {
                WaterUnit waterUnit = waterUnits.getUnitBySerialNumber(input);
                if (waterUnit != null) {
                    return waterUnit;
                } else {
                    System.out.println("No unit found matching serial '" + input + "'");
                }
            }
        }
    }

    private void displayUnitInfo(){
        WaterUnit w = processUnitInput();
        if (w != null) {
            System.out.printf("Unit details:%n" +
                            "    Serial: %s%n" +
                            "     Model: %s%n" +
                            " Ship Date: %s%n",
                    w.getSerialNumber(), w.getModel(),
                    w.getDateShipped() != null ? w.getDateShipped() : "-");
            List<Test> tests = w.getTests();
            if (tests != null){
                Printer<Test> waterUnitPrinter = new Printer<>();
                waterUnitPrinter.displayTable(tests, "Tests:",
                                              new String[]{"Date", "Passed", "Test Comments"});
            }
        }
    }

    private void createUnit(){
        System.out.println("Enter product info; blank line to quit.");
        Scanner in = new Scanner(System.in);
        String model, serialNumber;

        System.out.print("Model: ");
        model = in.nextLine();
        if (model.isBlank()) { return; }

        while (true){
            System.out.print("Serial number: ");
            serialNumber = in.nextLine();
            if (serialNumber.isBlank()) { return; }
            try {
                SerialNumberValidator.validateSerialNumber(serialNumber);
                waterUnits.add(new WaterUnit(serialNumber, model, null, null));
                break;
            } catch (Exception e) {
                System.out.println("Unable to add the product.\n" +
                        "     'Serial Number Error: Checksum does not match.'\n" +
                        "Please try again.");
            }
        }
    }

    private void testUnit(){
        WaterUnit w = processUnitInput();
        if (w == null){
            return;
        }
        Scanner in = new Scanner(System.in);
        boolean testPassed;
        String passedTest;

        System.out.print("Pass? (Y/n): ");
        while(true){
            passedTest = in.nextLine().trim().toLowerCase();
            if (passedTest.isBlank() || passedTest.equals("y")) {
                testPassed = true;
                break;
            }
            else if (passedTest.equals("n")){
                testPassed = false;
                break;
            }
            System.out.print("Error: Please enter [Y]es or [N]o ");
        }

        System.out.print("Comment: ");
        String comment = in.nextLine();

        w.setTests(new Test(LocalDate.now(), testPassed, comment));
        System.out.println("Test recorded.");
    }

    private void shipUnit(){
        WaterUnit w = processUnitInput();
        if (w == null){
            return;
        }
        w.setDateShipped(LocalDate.now());
        System.out.println("Unit successfully shipped.");
    }

    private void printReport(){
        GeneralTextMenu.MenuEntry[] menuEntries = new GeneralTextMenu.MenuEntry[]{
                new GeneralTextMenu.MenuEntry("ALL:           All products.", this::printAll),
                new GeneralTextMenu.MenuEntry("DEFECTIVE:     Products that failed their last test.", this::printDefective),
                new GeneralTextMenu.MenuEntry("READY-TO-SHIP: Products passed tests, not shipped.", this::printReadyToShip),
                new GeneralTextMenu.MenuEntry("Cancel report request.", null),
        };

        GeneralTextMenu reportOptions = new GeneralTextMenu("Report Options", menuEntries);
        reportOptions.doOption();
    }
    private void printAll(){
        Printer<WaterUnit> waterUnitPrinter = new Printer<>();
        waterUnitPrinter.displayTable(this.waterUnits.getWaterUnits(),
                                "List of Water Purification Units:",
                                     new String[]{"Model", "Serial", "# Tests", "Ship Date"});
    }
    private void printDefective(){
        Printer<WaterUnit> waterUnitPrinter = new Printer<>();
        waterUnitPrinter.displayTable(this.waterUnits.getDefectiveUnits(),
                                "DEFECTIVE Water Purification Units:",
                                     new String[]{"Model", "Serial", "# Tests", "Test Date", "Test Comments"});
    }
    private void printReadyToShip(){
        Printer<WaterUnit> waterUnitPrinter = new Printer<>();
        waterUnitPrinter.displayTable(this.waterUnits.getReadyToShipUnits(),
                                "READY-TO-SHIP Water Purification Units:",
                                     new String[]{"Model", "Serial", "Test Date"});
    }
    private void setReportSortOrder(){
        GeneralTextMenu.MenuEntry[] menuEntries = new GeneralTextMenu.MenuEntry[]{
                new GeneralTextMenu.MenuEntry("Sort by serial number", WaterUnitsManager::sortBySerialNumber),
                new GeneralTextMenu.MenuEntry("Sort by model, then serial number.", WaterUnitsManager::sortByModelAndSerialNumber),
                new GeneralTextMenu.MenuEntry("Sort by most recent test date.", WaterUnitsManager::sortByRecentTestDate),
                new GeneralTextMenu.MenuEntry("Cancel", null),
        };

        GeneralTextMenu reportOptions = new GeneralTextMenu("Select desired report sort order:", menuEntries);
        reportOptions.doOption();
    }
}

package com.vit.hms.service;

import com.vit.hms.bean.RentalPropertyBean;
import com.vit.hms.dao.RentalPropertyDAO;
import com.vit.hms.util.InvalidCityException;
import java.util.Scanner;

import java.util.List;

public class RentalPropertyService {

    public String addRentalProperty(RentalPropertyBean bean) {
        if (bean.getCity() == null || bean.getLocation() == null) {
            return "NULL VALUES IN INPUT";
        }

        if (bean.getCity().length() == 0 || bean.getLocation().length() == 0 ||
                bean.getNoOfBedRooms() == 0 || bean.getRentalAmount() == 0) {
            return "INVALID INPUT";
        }

        try {
            validateCity(bean.getCity());
        } catch (InvalidCityException e) {
            return e.toString();
        }

        RentalPropertyDAO dao = new RentalPropertyDAO();
        String propertyId = dao.createRentalProperty(bean);

        if (propertyId != null) {
            return "SUCCESS with " + bean.getPropertyId();
        } else {
            return "FAILURE";
        }
    }

    public List<RentalPropertyBean> getPropertyByCriteria(float minRentalAmount, float maxRentalAmount, int noOfBedRooms, String location, String city) {
        RentalPropertyBean bean = new RentalPropertyBean();
        bean.setNoOfBedRooms(noOfBedRooms);
        bean.setLocation(location);
        bean.setCity(city);

        RentalPropertyDAO dao = new RentalPropertyDAO();
        return dao.findPropertyByCriteria(minRentalAmount, maxRentalAmount, bean);
    }

    public String fetchRentalProperty(float minRentalAmount, float maxRentalAmount, int noOfBedRooms, String location, String city) {
        if (minRentalAmount == 0 || maxRentalAmount == 0 || maxRentalAmount < minRentalAmount ||
                noOfBedRooms <= 0 || location == null || city == null ||
                location.length() == 0 || city.length() == 0) {
            return "INVALID VALUES";
        }

        try {
            validateCity(city);
        } catch (InvalidCityException e) {
            return e.toString();
        }

        List<RentalPropertyBean> list = getPropertyByCriteria(minRentalAmount, maxRentalAmount, noOfBedRooms, location, city);

        if (list.isEmpty()) {
            return "NO MATCHING RECORDS";
        } else {
            return "RECORDS AVAILABLE: " + list.size();
        }
    }

    public void validateCity(String city) throws InvalidCityException {
        if (!city.trim().equalsIgnoreCase("Chennai") && !city.trim().equalsIgnoreCase("Bengaluru")) {
            throw new InvalidCityException();
        }
    }



    public static void main(String[] args) {
        RentalPropertyService service = new RentalPropertyService();
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 3) {
            System.out.println("\nHouse Management System");
            System.out.println("1. Add Rental Property");
            System.out.println("2. Find Rental Properties");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    RentalPropertyBean bean = new RentalPropertyBean();
                    System.out.print("Enter Rental Amount: ");
                    bean.setRentalAmount(scanner.nextFloat());
                    System.out.print("Enter Number of Bedrooms: ");
                    bean.setNoOfBedRooms(scanner.nextInt());
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Location: ");
                    bean.setLocation(scanner.nextLine());
                    System.out.print("Enter City: ");
                    bean.setCity(scanner.nextLine());

                    String addResult = service.addRentalProperty(bean);
                    System.out.println(addResult);
                    break;

                case 2:
                    System.out.print("Enter Minimum Rental Amount: ");
                    float min = scanner.nextFloat();
                    System.out.print("Enter Maximum Rental Amount: ");
                    float max = scanner.nextFloat();
                    System.out.print("Enter Number of Bedrooms: ");
                    int bedrooms = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter Location: ");
                    String location = scanner.nextLine();
                    System.out.print("Enter City: ");
                    String city = scanner.nextLine();

                    String fetchResult = service.fetchRentalProperty(min, max, bedrooms, location, city);
                    System.out.println(fetchResult);

                    if (!fetchResult.startsWith("INVALID") && !fetchResult.startsWith("NO MATCHING")) {
                        List<RentalPropertyBean> properties = service.getPropertyByCriteria(min, max, bedrooms, location, city);
                        for (RentalPropertyBean rp : properties) {
                            System.out.println("Property ID: " + rp.getPropertyId() +
                                    ", Rental Amount: " + rp.getRentalAmount() +
                                    ", Bedrooms: " + rp.getNoOfBedRooms() +
                                    ", Location: " + rp.getLocation() +
                                    ", City: " + rp.getCity());
                        }
                    }
                    break;

                case 3:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}




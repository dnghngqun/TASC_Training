package com.tasc.hongquan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tasc.hongquan.Controllers.CustomerController;
import com.tasc.hongquan.Models.Customer;
import com.tasc.hongquan.Repositories.CustomerRepositoryImplement;
import com.tasc.hongquan.Services.CustomerService;
import com.tasc.hongquan.Views.CustomerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        CustomerRepositoryImplement customerRepositoryImplement = new CustomerRepositoryImplement();
        CustomerService customerService = new CustomerService(customerRepositoryImplement);
        CustomerController customerController = new CustomerController(customerService);
        CustomerView customerView = new CustomerView(customerController);

        long startTime = System.currentTimeMillis();
        //load all data from file before start
        customerService.loadAll();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Running time: " + duration + " milliseconds" + "(" + duration/1000 + " seconds)");


        //after thread finish, start view
        customerView.run();
    }
}

class dataGenerator{
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String DOMAIN = "@gmail.com";
    private static final Random RANDOM = new Random();
    private static final String FILEPATH = "src/main/resources/customer.json";

    //set save value used
    private static Set<String> usedNames = new HashSet<>();
    private static Set<String> usedEmails = new HashSet<>();
    private static Set<String> usedPhoneNumbers = new HashSet<>();

    public static void main(String[] args) {
        CustomerRepositoryImplement customerRepositoryImplement = new CustomerRepositoryImplement();

        System.out.println("Start add data example");
        HashMap<String, Customer> customers = generateCustomers(1000000);
        //save to file
        System.out.println("Save to file...");
        customerRepositoryImplement.saveAll(customers);

//        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Tạo Gson với định dạng đẹp
//        Collection<Customer> values = customers.values();
//        try (FileWriter writer = new FileWriter(FILEPATH, true)) {
//            gson.toJson(values, writer); // Chuyển đổi HashMap thành JSON và ghi vào file
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private static HashMap<String, Customer> generateCustomers(int count) {

        HashMap<String, Customer> customers = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String name;
            String email;
            String phoneNumber;

            do {
                name = generateRandomName();
            } while (usedNames.contains(name));
            usedNames.add(name);

            do {
                email = generateEmail(name);
            } while (usedEmails.contains(email));
            usedEmails.add(email);

            do {
                phoneNumber = generatePhoneNumber();
            } while (usedPhoneNumbers.contains(phoneNumber));
            usedPhoneNumbers.add(phoneNumber);
            customers.put(phoneNumber,new Customer(name, email, phoneNumber));
        }
        return customers;
    }
    private static String generateRandomName() {
        int nameLength = RANDOM.nextInt(3) + 3; // Tên có độ dài từ 3 đến 5 ký tự
        StringBuilder name = new StringBuilder();
        for (int j = 0; j < nameLength; j++) {
            char letter = ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()));
            name.append(letter);
        }
        return capitalize(name.toString());
    }

    private static String generateEmail(String name) {
        String emailName = name.toLowerCase().replaceAll(" ", "")
                + (RANDOM.nextInt(1000));
        return emailName + DOMAIN;
    }

    private static String generatePhoneNumber() {
        // Tạo số điện thoại ngẫu nhiên có định dạng 098xxxxxxx
        return "098" + String.format("%07d", RANDOM.nextInt(10000000));
    }

    private static String capitalize(String name) {
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

}
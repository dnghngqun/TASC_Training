package com.tasc.hongquan;

import com.tasc.hongquan.Controllers.CustomerController;
import com.tasc.hongquan.Models.Customer;
import com.tasc.hongquan.Repositories.CustomerRepositoryImplement;
import com.tasc.hongquan.Services.CustomerService;
import com.tasc.hongquan.Views.CustomerView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        CustomerRepositoryImplement customerRepositoryImplement = new CustomerRepositoryImplement();
        CustomerService customerService = new CustomerService(customerRepositoryImplement);
        CustomerController customerController = new CustomerController(customerService);
        CustomerView customerView = new CustomerView(customerController);
        //load all data from file before start
        Thread thread = new Thread(() ->{customerService.l})
        customerService.loadAll();

        //after thread finish, start view
        customerView.run();
    }
}

class dataGenerator{
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String DOMAIN = "@gmail.com";
    private static final Random RANDOM = new Random();
    private static final String FILEPATH = "src/main/resources/customer.txt";
    public static void main(String[] args) {
        List<Customer> customers = generateCustomers(100000);
        //save to file
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(FILEPATH, true))){
            for (Customer customer : customers) {
                writer.write(customer.toString());
                writer.newLine();
            }
            System.out.println("Append 100000 customers successfully!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    private static List<Customer> generateCustomers(int count) {
        List<Customer> customers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String name = generateRandomName();
            String email = generateEmail(name);
            String phoneNumber = generatePhoneNumber();
            customers.add(new Customer(name, email, phoneNumber));
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
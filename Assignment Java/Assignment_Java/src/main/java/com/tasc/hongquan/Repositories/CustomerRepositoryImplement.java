package com.tasc.hongquan.Repositories;

import com.tasc.hongquan.Models.Customer;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class CustomerRepositoryImplement implements Repository<Customer,String,Customer> {
    private final String filePath = "src/main/resources/customer.txt";

    @Override
    public void saveAll(List<Customer> t) {
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))){
            for(Customer customer : t){
                bufferedWriter.write(customer.toString());
                bufferedWriter.newLine();
            }
            System.out.println("Write to file successfully!");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public synchronized HashMap<String, Customer> LoadAll() {
        System.out.println("Start loading customers...");
        File file = new File(filePath);
        if(!file.exists()){
            System.out.println("File does not exist!");
            return new HashMap<>();
        }

        HashMap<String, Customer> customers = new HashMap<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))){
            String line = bufferedReader.readLine();
            while(line != null){
                String[] data = line.split(",");
                Customer customer = new Customer(data[0].trim(), data[1].trim(), data[2].trim());
                customers.put(customer.getPhoneNumber(), customer);
                line = bufferedReader.readLine();
            }
            System.out.println("Load customers successfully!");
        }catch (IOException e) {
            e.printStackTrace();
        }
        return customers;

    }


}

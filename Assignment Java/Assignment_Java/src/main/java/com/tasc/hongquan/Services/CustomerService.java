package com.tasc.hongquan.Services;

import com.tasc.hongquan.Models.Customer;
import com.tasc.hongquan.Repositories.CustomerRepositoryImplement;
import com.tasc.hongquan.Repositories.Repository;

import java.util.HashMap;

public class CustomerService {
    //Map to store all customers
    private HashMap<String, Customer> customers;


    private final Repository<Customer,String, Customer> customerRepository;

    public CustomerService(CustomerRepositoryImplement customerRepository) {
        this.customers = new HashMap<>();
        this.customerRepository = customerRepository;
    }

    public void loadAll() {
        customers = customerRepository.LoadAll();
    }

    public void saveAll() {
        //convert map to list
        customerRepository.saveAll(customers
                .values()
                .stream()
                .toList());
    }

    //crud

    //add
    public void addCustomer(Customer customer) {
        try {
            customers.put(customer.getPhoneNumber(), customer);
            System.out.println("Add customer successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //add  more customer
    public void addMoreCustomer(HashMap<String, Customer> customerHashMap){
        try{
            customers.putAll(customerHashMap);
            System.out.println("Add customers successfully!");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //update if change phoneNumber
    public void updateChangePhoneCustomer(String phoneNumberLast,Customer customer){
        try {
            customers.remove(phoneNumberLast);
            customers.put(customer.getPhoneNumber(), customer);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateCustomer(Customer customer){
        try {
            customers.put(customer.getPhoneNumber(), customer);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //delete
    public Customer deleteCustomer(String phoneNumber) {
        Customer customer = customers.remove(phoneNumber);
        if (customer != null) {
            return customer;
        } else {
            return null;
        }
    }

    //search by phone number
    public Customer searchByPhoneNumber(String phoneNumber){
        return customers.get(phoneNumber);
    }

    //get all
    public HashMap<String, Customer> getAllCustomer(){
        return customers;
    }

}


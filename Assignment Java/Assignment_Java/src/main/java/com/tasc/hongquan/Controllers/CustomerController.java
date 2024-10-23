package com.tasc.hongquan.Controllers;

import com.tasc.hongquan.Models.Customer;
import com.tasc.hongquan.Services.CustomerService;

import java.util.HashMap;

public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }



    //add  one customer
    public void addCustomer(Customer customer){
        customerService.addCustomer(customer);

    }

    public void addMoreCustomer(HashMap<String, Customer> customerHashMap){
        customerService.addMoreCustomer(customerHashMap);
    }

    //update if changePhoneNumber
    public void updateChangePhoneCustomer(String phoneNumberLast,Customer customer){
        customerService.updateChangePhoneCustomer(phoneNumberLast,customer);
    }

    public void updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
    }

    public HashMap<String, Customer> getAllCustomer(){
        return customerService.getAllCustomer();
    }

    public void deleteCustomer(String phoneNumber){
        Customer deleteCustomer = customerService.deleteCustomer(phoneNumber);
        if(deleteCustomer != null){
            System.out.println("Delete customer: {"+ deleteCustomer.toString() + "} successfully!");
        }else {
            System.out.println("Customer information does not exist!");
        }
    }

    public Customer searchByPhoneNumber(String phoneNumber){
        return customerService.searchByPhoneNumber(phoneNumber);
    }

    public void saveAll(){
        customerService.saveAll();
    }

    public boolean checkDuplicatePhoneNumber(String phoneNumber){
        return customerService.checkDuplicatePhoneNumber(phoneNumber);
    }
}

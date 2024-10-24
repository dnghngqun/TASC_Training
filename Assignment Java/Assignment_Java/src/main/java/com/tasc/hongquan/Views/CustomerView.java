package com.tasc.hongquan.Views;

import com.tasc.hongquan.Controllers.CustomerController;
import com.tasc.hongquan.Models.Customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CustomerView {

    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final CustomerController customerController;

    public CustomerView(CustomerController customerController) {
        this.customerController = customerController;
    }

    public void run(){
        while(true) {
            try {
                int choice = menu();
                switch (choice) {
                    case 1:
                        viewAllCustomer();
                        break;
                    case 2:
                        addCustomer();
                        break;
                    case 3:
                        findCustomer();
                        break;
                    case 4:
                        handleUpdateCustomer();
                        break;
                    case 5:
                        deleteCustomer();
                        break;
                    case 6:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void findCustomer() throws IOException {
        System.out.print("Enter the phone number of the customer you want to find: ");
        String phoneNumber = reader.readLine();
        Customer customer = customerController.searchByPhoneNumber(phoneNumber);
        if(customer != null){
            System.out.println("Customer found: " + customer.toString());
        }else {
            System.out.println("Customer information does not exist!");
        }
    }

    private void updateCustomer(int index) throws IOException {
        System.out.print("Enter the phone number of the customer" + (index > 0 ? index : "") +" you want to update: ");
        String phoneNumber = reader.readLine();
        Customer customer = customerController.searchByPhoneNumber(phoneNumber);
        System.out.println("Customer" + (index > 0 ? index : "") + " found: " + customer.toString());

        System.out.println("Update customer" + (index > 0 ? index : "") + " information: ");
        System.out.print("Enter customer" + (index > 0 ? index : "") + " name: ");
        customer.setName(checkName());
        System.out.print("Enter customer" + (index > 0 ? index : "") + " email: ");
        customer.setEmail(checkEmail());
        System.out.println("Do you want to update phone number(y/n)?");
        System.out.print("Choice: ");
        String choice = String.valueOf(reader.readLine().charAt(0)).toLowerCase();
        if(choice.equals("y")) {
            String lastPhoneNumber = customer.getPhoneNumber();
            System.out.print("Enter customer" + (index > 0 ? index : "") + " phone number: ");
            customer.setPhoneNumber(checkUpdatePhoneNumber(lastPhoneNumber));
            customerController.updateChangePhoneCustomer(lastPhoneNumber,customer);
        }else {
            System.out.println("No update phone number!");
            customerController.updateCustomer(customer);
        }
    }
    private void handleUpdateCustomer() throws IOException {
        System.out.print("Enter the number of customers to update: ");
        try {
            int n = Integer.parseInt(reader.readLine());

            if(n == 1) {
                updateCustomer(0);
                System.out.println("Update customer successfully!");
            }else if(n > 1) {
                for(int i = 0; i < n; i++) {
                    updateCustomer(i+1);
                    System.out.println("Update customer" + (i+1) + " successfully!");
                }
                System.out.println("Update customers successfully!");
            }else {
                System.out.println("Invalid input!");
            }
            System.out.println("Please wait for write to file...");
            customerController.saveAll();

        }catch (NumberFormatException e) {
            System.out.println("Invalid input! Please try again!");
            handleUpdateCustomer();
        }
    }

    private void deleteCustomer() throws IOException {
        System.out.print("Enter the phone number of the customer you want to delete: ");
        String phoneNumber = reader.readLine();
        customerController.deleteCustomer(phoneNumber);
        System.out.println("Please wait for write to file...");
        customerController.saveAll();
    }

    private int menu() throws IOException {
        System.out.println("=====Customer Management=====");
        System.out.println("1. View All Customer");
        System.out.println("2. Add Customer");
        System.out.println("3. Find Customer");
        System.out.println("4. Update Customer");
        System.out.println("5. Delete Customer");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
        int choice = Integer.parseInt(reader.readLine());
        return choice;
    }

    private boolean patternCheck(String regexPattern, String input) {
        return Pattern.compile(regexPattern)
                .matcher(input)
                .matches();
    }

    private String checkEmail() throws IOException {
        String emailPattern = "^(.+)@(\\S+)$";
        String email = reader.readLine();

        if(email.equals("") || email == null) {
            System.out.print("Email is not null! Enter email again: ");
            email = reader.readLine();
        }

        while (!patternCheck(emailPattern, email)) {
            System.out.print("Email is not valid! Enter email again: ");
            email = reader.readLine();
        }
        return email.trim();
    }
    private String checkPhoneNumber() throws IOException {
        String phonePattern = "^(0|84)([35789])([0-9]{8})$";
        String phone = reader.readLine();

        if (phone.equals("") || phone == null) {
            System.out.print("Phone number is not null! Enter phone number again: ");
            phone = reader.readLine();
        }

        while (!patternCheck(phonePattern, phone)) {
            System.out.print("Phone number is not valid! Enter phone number again: ");
            phone = reader.readLine();
        }

        if(customerController.checkDuplicatePhoneNumber(phone)){
            System.out.print("Phone number is duplicated! Enter phone number again: ");
            phone = reader.readLine();
        }
        return phone.trim();
    }

    private String checkUpdatePhoneNumber(String lastPhone) throws IOException {
        String phonePattern = "^(0|84)([35789])([0-9]{8})$";
        String phone = reader.readLine();

        if (phone.equals("") || phone == null) {
            System.out.print("Phone number is not null! Enter phone number again: ");
            phone = reader.readLine();
        }

        if(phone == lastPhone) return phone;

        while (!patternCheck(phonePattern, phone)) {
            System.out.print("Phone number is not valid! Enter phone number again: ");
            phone = reader.readLine();
        }

        if(customerController.checkDuplicatePhoneNumber(phone)){
            System.out.print("Phone number is duplicated! Enter phone number again: ");
            phone = reader.readLine();
        }
        return phone.trim();
    }

    private String checkName() throws IOException {
        String name = reader.readLine();

        if (name.equals("") || name == null) {
            System.out.print("Name is not null! Enter name again: ");
            name = reader.readLine();
        }
        return name.trim();
    }

    private void addCustomer() throws IOException {
        System.out.print("Enter the number of customers to add: ");
        try {
            int n = Integer.parseInt(reader.readLine());

            if(n == 1) {
                Customer customer = new Customer();
                System.out.print("Enter customer name: ");
                customer.setName(checkName());
                System.out.print("Enter customer email: ");
                customer.setEmail(checkEmail());
                System.out.print("Enter customer phone number: ");
                customer.setPhoneNumber(checkPhoneNumber());
                customerController.addCustomer(customer);
            }else if(n > 1) {
                HashMap<String, Customer> customers = new HashMap<>();
                for(int i = 0; i < n; i++) {
                    Customer customer = new Customer();
                    System.out.print("Enter customer name " + (i+1) + ": ");
                    customer.setName(checkName());
                    System.out.print("Enter customer email " + (i+1) + ": ");
                    customer.setEmail(checkEmail());
                    System.out.print("Enter customer phone number " + (i+1) + ": ");
                    customer.setPhoneNumber(checkPhoneNumber());
                    customers.put(customer.getPhoneNumber(), customer);
                }
                customerController.addMoreCustomer(customers);
            }else {
                System.out.println("Invalid input!");
            }

            System.out.println("Please wait for write to file...");
            customerController.saveAll();
        }catch (NumberFormatException e) {
            System.out.println("Invalid input! Please try again!");
            addCustomer();
        }
    }

    private void viewAllCustomer() {
        HashMap<String, Customer> customers = customerController.getAllCustomer();
        if(customers == null || customers.isEmpty()) {
            System.out.println("No customer information available!");
        }else {
            int i = 0;
            System.out.println("=====View All Customer=====");
            System.out.println("|     No     |         Name         |             Email              | Phone Number |");
            for(Customer customer : customers.values()) {
                System.out.printf("|%s|  %-20s|  %-30s|%s|\n", center(String.valueOf(i+1), 12), customer.getName(), customer.getEmail(), center(customer.getPhoneNumber(), 14));
                i++;
            }
        }
    }

    private String center(String text, int length) {
        int padding = length - text.length();
        int paddingLeft = padding /2;
        int paddingRight = padding - paddingLeft;
        return " ".repeat(paddingLeft) + text + " ".repeat(paddingRight);
    }


}

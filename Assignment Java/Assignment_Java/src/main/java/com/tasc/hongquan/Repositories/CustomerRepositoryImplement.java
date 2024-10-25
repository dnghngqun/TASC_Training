package com.tasc.hongquan.Repositories;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tasc.hongquan.Main;
import com.tasc.hongquan.Models.Customer;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.*;

public class CustomerRepositoryImplement implements Repository<Customer,String,Customer> {
    private final String filePath = "src/main/resources/customer.json";
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private Future<Boolean> currentSaveTask;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    /*
    @Override
    public void saveAll(HashMap<String, Customer> data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // Tạo Gson với định dạng đẹp
        Collection<Customer> values = data.values();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(values, writer); // Chuyển đổi HashMap thành JSON và ghi vào file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */



    @Override
    public synchronized void saveAll(HashMap<String, Customer> t) {
        //cancel last task
        if(currentSaveTask != null && !currentSaveTask.isDone()){
            currentSaveTask.cancel(true);
        }

        //start new task
        currentSaveTask = executorService.submit(() -> {
            System.out.println("Start saving customers...");
            Collection<Customer> values = t.values();
            try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
                gson.toJson(values, writer);
                System.out.println("Save customers successfully!");
                return true;
            }catch (IOException e){
                e.printStackTrace();
                return false;
            }

        });

        try{
            //wait all thread to finish with time = infinity
            currentSaveTask.get(Long.MAX_VALUE, TimeUnit.SECONDS);
        }catch (TimeoutException e) {
            System.out.println("Task timed out and was cancelled.");
            currentSaveTask.cancel(true);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    //method using stop executor when don't use
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }

        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    /*


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
            Gson gson = new GsonBuilder().create();
            Type customerListType = new TypeToken<List<Customer>>() {}.getType(); // Định nghĩa kiểu danh sách Customer

            // Đọc toàn bộ nội dung file
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonContent.append(line);
            }

            // Chuyển đổi nội dung JSON thành danh sách Customer
            List<Customer> customerList = gson.fromJson(jsonContent.toString(), customerListType);

            // Thêm vào HashMap
            for (Customer customer : customerList) {
                customers.put(customer.getPhoneNumber(), customer); // Sử dụng số điện thoại làm key
            }

            System.out.println("Load customers successfully!");
        }catch (IOException e) {
            e.printStackTrace();
        }
        return customers;

    }


     */
/*
 */

    @Override
    public synchronized HashMap<String, Customer> LoadAll() {
        System.out.println("Start loading customers...");
        File fileJSON = new File(filePath);
        if(!fileJSON.exists()){
            System.out.println("File does not exist!");
            return new HashMap<>();
        }
        int numThreads = 4;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        List<ByteArrayOutputStream> byteOutputStreams = new ArrayList<>(Collections.nCopies(numThreads, null));
        //load data
        try(RandomAccessFile file = new RandomAccessFile(filePath,"r")){
            long fileSize = file.length();
            for(int i = 0; i < numThreads; i++){
                long startByte = i * (fileSize/numThreads);
                long endByte = (i == numThreads -1) ? fileSize: (i+1) * (fileSize/numThreads);
                final int threadIndex = i;
                executorService.execute(() -> {
                    try(RandomAccessFile dataFile = new RandomAccessFile(filePath,"r")){
                        dataFile.seek(startByte);
                        byte[] buffer = new byte[(int) (endByte - startByte)];
                        int bytesRead;
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        while(dataFile.getFilePointer() < endByte && (bytesRead = dataFile.read(buffer)) != -1){
                            outputStream.write(buffer, 0, bytesRead);
                        }

                        synchronized (dataFile){
                            byteOutputStreams.set(threadIndex, outputStream);
                            System.out.println("Thread " + (threadIndex + 1) + " done!");
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                });

            }
        }catch(IOException e){
            e.printStackTrace();
        }

        executorService.shutdown();//shutdown thead after success
        try{
            //wait all thread to finish with time = infinity
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }catch (InterruptedException e){
            executorService.shutdownNow();
        }
        //ghep cac du lieu vao nhau
        ByteArrayOutputStream fullFileByte = new ByteArrayOutputStream();
        for(ByteArrayOutputStream outputStream : byteOutputStreams) {
            if(outputStream != null){
                try {
                    //write to full file
                    outputStream.writeTo(fullFileByte);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Convert to hashmap");
        //convert byte to String
        String fullFileString = fullFileByte.toString();
        //convert String to HashMap
        HashMap<String, Customer> customers = new HashMap<>();
        Gson gson = new GsonBuilder().create();

        // Tạo danh sách Customer từ JSON
        Customer[] customerArray = gson.fromJson(fullFileString, Customer[].class);

        // Thêm vào HashMap
        for (Customer customer : customerArray) {
            customers.put(customer.getPhoneNumber(), customer);
        }

        System.out.println("Load customers successfully!");
        return customers;
    }

}

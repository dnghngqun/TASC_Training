package com.tasc.hongquan.Repositories;

import com.tasc.hongquan.Models.Customer;

import java.util.HashMap;
import java.util.List;

public interface Repository<T,K,V> {
    void saveAll(HashMap<K,V> t);
    HashMap<K,V> LoadAll();
    void shutdown();//shutdown  all thead after success, using for stop program
}

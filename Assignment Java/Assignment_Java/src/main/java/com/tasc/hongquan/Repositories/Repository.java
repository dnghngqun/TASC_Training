package com.tasc.hongquan.Repositories;

import com.tasc.hongquan.Models.Customer;

import java.util.HashMap;
import java.util.List;

public interface Repository<T,K,V> {
    void saveAll(List<T> t);
    HashMap<K,V> LoadAll();

}

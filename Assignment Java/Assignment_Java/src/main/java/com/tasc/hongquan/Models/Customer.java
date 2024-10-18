package com.tasc.hongquan.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Comparable<Customer> {
    private String name;
    private String email;
    private String phoneNumber;

    @Override
    public int compareTo(Customer other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public String toString() {
        return this.name + ", " + this.email + ", " + this.phoneNumber ;
    }
}

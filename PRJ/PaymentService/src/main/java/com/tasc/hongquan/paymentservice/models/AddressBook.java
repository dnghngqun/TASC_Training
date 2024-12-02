package com.tasc.hongquan.paymentservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address_book")
public class AddressBook {
    @Id
    @Column(name = "address_book_id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Size(max = 255)
    @NotNull
    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Lob
    @Column(name = "address")
    private String address;

}
package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be atleast 5 charactors")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be atleast 5 charactors")
    private String buildingName;

    @NotBlank
    @Size(min = 4, message = "City name must be atleast 4 charactors")
    private String city;

    @NotBlank
    @Size(min = 2, message = "State name must be atleast 2 charactors")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be atleast 2 charactors")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Pincode must be atleast 6 charactors")
    private String pinCode;

    @ManyToMany(mappedBy = "addresses")
    private List<User> users = new ArrayList<>();

    public Address(String buildingName, String city, String country, String pinCode, String state, String street) {
        this.buildingName = buildingName;
        this.city = city;
        this.country = country;
        this.pinCode = pinCode;
        this.state = state;
        this.street = street;
    }
}

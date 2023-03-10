package com.dailycodebuffer.springbootmongodb.dto;

import com.dailycodebuffer.springbootmongodb.model.Address;
import lombok.Data;

import java.util.List;

@Data
public class PersonDto {
    private String personId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String hobbies;
    private List<Address> addressList;
}
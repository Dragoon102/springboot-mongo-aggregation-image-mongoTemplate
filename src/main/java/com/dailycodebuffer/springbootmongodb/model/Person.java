package com.dailycodebuffer.springbootmongodb.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document (collection = "person")
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL )
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    private String personId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String hobbies;
    private List<Address> addressList;
}

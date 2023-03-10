package com.dailycodebuffer.springbootmongodb.repository;

import com.dailycodebuffer.springbootmongodb.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends MongoRepository<Person,String> {
    List<Person> findByFirstNameStartsWith(String name);

    @Query(value = "{ 'age' : { $gt : ?0 , $lt : ?1 }}",
    fields = "{addressList : 0}")
    List<Person> getPersonBetweenAge(int minAge, int maxAge);
//    List<Person> findByAgeBetween(int minAge,int maxAge);
}

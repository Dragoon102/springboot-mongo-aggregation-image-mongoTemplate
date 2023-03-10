package com.dailycodebuffer.springbootmongodb.service;

import com.dailycodebuffer.springbootmongodb.dto.PersonDto;
import com.dailycodebuffer.springbootmongodb.model.Person;
import com.dailycodebuffer.springbootmongodb.repository.PersonRepository;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    public String save(PersonDto personDto) {
        Person person=new Person();
        person.setPersonId(UUID.randomUUID().toString().split("-")[0]);
        person.setAge(personDto.getAge());
        person.setHobbies(personDto.getHobbies());
        person.setLastName(personDto.getLastName());
        person.setFirstName(personDto.getFirstName());
        person.setAddressList(personDto.getAddressList());

        return personRepository.save(person).getPersonId();
    }

    public List<Person> getPerson(String name) {
        return personRepository.findByFirstNameStartsWith(name);
    }

    public String DeleteUserById(String id) {
        String user=personRepository.findById(id).get().getFirstName();
        personRepository.deleteById(id);
        return "deleted user" + user;
    }

    public List<Person> getPersonBetweenAge(int minAge, int maxAge) {
        return personRepository.getPersonBetweenAge(minAge,maxAge);
    }

    public Page<Person> searchPerson(String name, Integer minAge, Integer maxAge, String city, Pageable pageable) {
        Query query=new Query().with(pageable);
        List<Criteria> criteriaList=new ArrayList<>();

        if (name != null && !name.isEmpty()){
            criteriaList.add(Criteria.where("firstName").regex(name,"i"));
        }

        if (minAge!=null && maxAge !=null){
            criteriaList.add(Criteria.where("age").gte(minAge).lte(maxAge));
        }
        if (city != null && !city.isEmpty()){
            criteriaList.add(Criteria.where("addressList.city").is(city));
        }
        if (!criteriaList.isEmpty()){
            query.addCriteria(new Criteria()
                    .andOperator(criteriaList.toArray(new Criteria[0])));
        }

        Page<Person> People= PageableExecutionUtils.getPage(
                mongoTemplate.find(query,Person.class),pageable,
                () -> mongoTemplate.count(query.skip(0).limit(0),Person.class)
        );
        return People;
    }

    public List<Document> getOldestPersonByCity() {
        UnwindOperation unwindOperation= Aggregation.unwind("addressList");
        SortOperation sortOperation=Aggregation.sort(Sort.Direction.DESC,"age");
        GroupOperation groupOperation=Aggregation.group("addressList.city").first(Aggregation.ROOT)
                .as("oldest Person");

        Aggregation aggregation=Aggregation.newAggregation(unwindOperation,sortOperation,groupOperation);
        List<Document> person=mongoTemplate.aggregate(aggregation,Person.class,Document.class).getMappedResults();

        return person;
    }

    public List<Document> getPopulationByCity() {
        UnwindOperation unwindOperation= Aggregation.unwind("addressList");
        GroupOperation groupOperation=Aggregation.group("addressList.city")
                .count().as("popCount");
        SortOperation sortOperation=Aggregation.sort(Sort.Direction.DESC,"popCount");

        ProjectionOperation projectionOperation=Aggregation.project()
                .andExpression("_id").as("cityName")
                .andExpression("popCount").as("count")
                .andExclude("_id");

        Aggregation aggregation=Aggregation.newAggregation(unwindOperation,groupOperation,
                sortOperation,projectionOperation);

        List <Document> documents=mongoTemplate.aggregate(aggregation,Person.class,Document.class).getMappedResults();
        return documents;
    }
}

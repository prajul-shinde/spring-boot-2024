package com.omsai.spring6reactiveexamples.repositories;

import com.omsai.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testMonoByIdBlock() {

        Mono<Person> personMono = personRepository.findById(1);
        Person person = personMono.block();
        System.out.println(person.toString());
    }

    @Test
    void testMonoByIdSubscriber() {

        Mono<Person> personMono = personRepository.findById(1);
        personMono.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void testMapOperation() {

        Mono<Person> personMono = personRepository.findById(1);
        personMono.map(person -> {
            return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println(firstName);
        });
    }

}
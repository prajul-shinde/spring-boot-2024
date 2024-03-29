package com.omsai.spring6reactiveexamples.repositories;

import com.omsai.spring6reactiveexamples.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void testFindByIdFoundStepVerifier() {

        Mono<Person> personMono = personRepository.findById(3);
        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();
    }

    @Test
    void testFindByIdNotFoundStepVerifier() {

        Mono<Person> personMono = personRepository.findById(11);
        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();
    }

    @Test
    void testFindByIdFound() {

        Mono<Person> personMono = personRepository.findById(3);
        assertTrue(personMono.hasElement().block());
    }

    @Test
    void testFindByIdNotFound() {

        Mono<Person> personMono = personRepository.findById(11);
        assertFalse(personMono.hasElement().block());
    }

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
        personMono.map(Person::getFirstName).subscribe(firstName -> {
            System.out.println(firstName);
        });
    }

    @Test
    void testFluxBlockFirst() {

        Flux<Person> personFlux = personRepository.findAll();
        Person person = personFlux.blockFirst();
        System.out.println(person.toString());
    }

    @Test
    void testFluxSubscriber() {

        Flux<Person> personFlux = personRepository.findAll();
        personFlux.subscribe(person -> System.out.println(person.toString()));
    }

    @Test
    void testFLuxMap() {

        Flux<Person> personFlux = personRepository.findAll();
        personFlux.map(Person::getFirstName).subscribe(firstName -> System.out.println(firstName));
    }

    @Test
    void testFluxToList() {

        Flux<Person> personFlux = personRepository.findAll();
        Mono<List<Person>> listMono = personFlux.collectList();
        listMono.subscribe(list -> {
            list.forEach(person -> System.out.println(person.getFirstName()));
        });
    }

    @Test
    void testFilterOnName() {

        personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona"))
                .subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void testGetById() {

        Mono<Person> personMono = personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Fiona"))
                .next();
        personMono.subscribe(person -> System.out.println(person.getFirstName()));

    }

    @Test
    void testFindPersonByIdNotFound() {

        final Integer id = 8;
        Mono<Person> personMono = personRepository.findAll()
                .filter(person -> person.getId() == id)
                .single()
                .doOnError(throwable -> {
                    System.out.println(throwable.toString());
                });

        personMono.subscribe(person -> person.toString(), throwable -> {
            System.out.println(throwable.toString());
        });
    }
}
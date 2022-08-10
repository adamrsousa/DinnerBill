package com.payment.bill.v1.api.controller;

import com.payment.bill.v1.api.http.resources.request.PersonRequest;
import com.payment.bill.v1.api.http.resources.response.PersonResponse;
import com.payment.bill.v1.domain.model.Person;
import com.payment.bill.v1.domain.service.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/people")
public class PersonController {

    private final PersonService service;

    private final ModelMapper modelMapper;

    public PersonController(PersonService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<PersonResponse> findById(@PathVariable(name = "id") Long id) {
        final Person person = service.findById(id);
        PersonResponse response = modelMapper.map(person, PersonResponse.class);
        return ResponseEntity.ok(response);

    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<PersonResponse> create(@RequestBody PersonRequest personRequest) {
        Person request = modelMapper.map(personRequest, Person.class);
        Person created = service.create(request);
        PersonResponse response = modelMapper.map(created, PersonResponse.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<PersonResponse> update(@PathVariable(name = "id") Long id,
                                                 @RequestBody PersonRequest request) {
        Person data = modelMapper.map(request, Person.class);
        Person person = service.update(id, data);
        PersonResponse response = modelMapper.map(person, PersonResponse.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<PersonResponse> delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
       return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Page<PersonResponse>> findAll(Pageable pageable) {
        Page<Person> people = service.findAll(pageable);
        List<PersonResponse> content = people.stream()
                .map(item -> modelMapper.map(item, PersonResponse.class))
                .collect(Collectors.toList());
        Page<PersonResponse> personResponse = new PageImpl<>(content, pageable, people.getTotalElements());
        return ResponseEntity.ok(personResponse);
    }
}

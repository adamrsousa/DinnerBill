package com.payment.bill.v1.api.controller;

import com.payment.bill.v1.api.http.resources.request.GroupSpendingRequest;
import com.payment.bill.v1.api.http.resources.response.GroupSpendingResponse;
import com.payment.bill.v1.domain.model.GroupSpending;
import com.payment.bill.v1.domain.service.GroupSpendingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value="/billdivision")
public class BillDivisionController {

    private final GroupSpendingService service;
    private final ModelMapper modelMapper;

    public BillDivisionController(GroupSpendingService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<GroupSpendingResponse> findById(@PathVariable(name = "id") Long id) {
        final GroupSpending group = service.findById(id);
        GroupSpendingResponse response = modelMapper.map(group, GroupSpendingResponse.class);
        return ResponseEntity.ok(response);

    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<GroupSpendingResponse> create(@RequestBody GroupSpendingRequest groupRequest) {
        GroupSpending request = modelMapper.map(groupRequest, GroupSpending.class);
        GroupSpending created = service.create(request);
        GroupSpendingResponse response = modelMapper.map(created, GroupSpendingResponse.class);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<GroupSpendingResponse> update(@PathVariable(name = "id") Long id,
                                                 @RequestBody GroupSpendingRequest request) {
        GroupSpending data = modelMapper.map(request, GroupSpending.class);
        GroupSpending group = service.update(id, data);
        GroupSpendingResponse response = modelMapper.map(group, GroupSpendingResponse.class);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity<GroupSpendingResponse> delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Page<GroupSpendingResponse>> findAll(Pageable pageable) {
        Page<GroupSpending> group = service.findAll(pageable);
        List<GroupSpendingResponse> content = group.stream()
                .map(item -> modelMapper.map(item, GroupSpendingResponse.class))
                .collect(Collectors.toList());
        Page<GroupSpendingResponse> groupResponse = new PageImpl<>(content, pageable, group.getTotalElements());
        return ResponseEntity.ok(groupResponse);
    }

}

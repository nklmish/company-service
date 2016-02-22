package com.nklmish.boundary.api;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(value = "/", description = "Starting point to explore API")
public class ApiController {
    private final ApiResourceAssembler assembler;

    @Autowired
    public ApiController(ApiResourceAssembler assembler) {
        this.assembler = assembler;
    }

    @RequestMapping()
    public HttpEntity<ApiResource> getApi() {
        ApiResource resource = assembler.toResource();
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
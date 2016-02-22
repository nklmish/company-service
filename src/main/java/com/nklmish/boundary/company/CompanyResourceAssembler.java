package com.nklmish.boundary.company;

import com.nklmish.entity.company.Company;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class CompanyResourceAssembler implements ResourceAssembler<Company, CompanyResource>{

    @Override
    public CompanyResource toResource(Company entity) {
        CompanyResource resource = new CompanyResource(entity);
        resource.add(linkTo(methodOn(CompanyController.class).getCompanyById(entity.getId()))
                .withSelfRel());
        return resource;
    }
}

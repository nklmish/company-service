package com.nklmish.boundary.company;

import com.nklmish.entity.company.Company;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class CompanyResource extends Resource<Company> {

    public CompanyResource(Company content, Link... links) {
        super(content, links);
    }
}

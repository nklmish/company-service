package com.nklmish.repository.company;


import com.nklmish.entity.company.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {

}

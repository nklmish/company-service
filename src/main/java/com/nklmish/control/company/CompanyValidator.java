package com.nklmish.control.company;

import com.nklmish.control.Validator;
import com.nklmish.entity.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Component
public class CompanyValidator implements Validator<Set<ConstraintViolation<Company>>, Company> {

    private final javax.validation.Validator validator;

    @Autowired
    public CompanyValidator(javax.validation.Validator validator) {
        this.validator = validator;
    }

    @Override
    public Set<ConstraintViolation<Company>> validate(Company param) {
        return validator.validate(param);
    }
}

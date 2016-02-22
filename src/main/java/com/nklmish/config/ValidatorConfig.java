package com.nklmish.config;

import com.nklmish.control.Validator;
import com.nklmish.control.beneficial.BeneficialOwnerValidator;
import com.nklmish.control.company.CompanyValidator;
import com.nklmish.entity.beneficial.BeneficialOwner;
import com.nklmish.entity.company.Company;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

@Configuration
public class ValidatorConfig {

    @Bean
    public Validator<Set<ConstraintViolation<Company>>, Company> companyValidationService() {
        return new CompanyValidator(localValidatorFactoryBean());
    }

    @Bean
    public Validator<Set<ConstraintViolation<BeneficialOwner>>, BeneficialOwner> beneficialOwnerValidationService() {
        return new BeneficialOwnerValidator(localValidatorFactoryBean());
    }

    @Bean
    public javax.validation.Validator localValidatorFactoryBean() {
        return Validation.buildDefaultValidatorFactory().getValidator();
    }

}

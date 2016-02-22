package com.nklmish.control.company;

import com.nklmish.control.Validator;
import com.nklmish.entity.beneficial.BeneficialOwner;
import com.nklmish.entity.company.Company;
import com.nklmish.boundary.Converter;
import com.nklmish.boundary.company.form.CompanyForm;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.nklmish.boundary.api.ApiConstant.RequestParamsNames.Company.BENEFICIAL_OWNERS;

@Component
public class CompanyRequestValidator implements org.springframework.validation.Validator {

    private Validator<Set<ConstraintViolation<Company>>, Company> companyValidator;
    private Validator<Set<ConstraintViolation<BeneficialOwner>>, BeneficialOwner> beneficialOwnerValidator;
    private Converter<Company, CompanyForm> companyFormConverter;
    private Converter<List<BeneficialOwner>, Map<?, ?>> beneficialOwnerMapConverter;

    @Autowired
    public CompanyRequestValidator(Validator<Set<ConstraintViolation<Company>>, Company> companyValidator,
                                   Validator<Set<ConstraintViolation<BeneficialOwner>>, BeneficialOwner> beneficialOwnerValidator,
                                   Converter<Company, CompanyForm> companyFormConverter,
                                   Converter<List<BeneficialOwner>, Map<?, ?>> beneficialOwnerMapConverter) {
        this.companyValidator = companyValidator;
        this.beneficialOwnerValidator = beneficialOwnerValidator;
        this.companyFormConverter = companyFormConverter;
        this.beneficialOwnerMapConverter = beneficialOwnerMapConverter;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CompanyForm.class.equals(clazz) || LinkedHashMap.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target.getClass().equals(CompanyForm.class)) {
            validateCompany(errors, (CompanyForm) target);
        } else {
            validateMap(errors, (Map<?, ?>) target);
        }

    }

    private void validateCompany(Errors errors, CompanyForm companyForm) {
        Company company = companyFormConverter.toModel(companyForm);
        Set<ConstraintViolation<Company>> violations = companyValidator.validate(company);
        reportErrors(violations, violation -> {
            errors.rejectValue(violation.getPropertyPath().toString(), violation.getMessage());
        });
    }

    private static <T> void reportErrors(Collection<ConstraintViolation<T>> violations, Consumer<ConstraintViolation> consumer) {
        violations.forEach(consumer::accept);
    }

    @SuppressWarnings("unchecked")
    private void validateMap(Errors errors, Map<?, ?> params) {
        if (MapUtils.isEmpty(params) || !params.containsKey(BENEFICIAL_OWNERS)) {
            errors.reject(HttpStatus.BAD_REQUEST.toString(), "require list of beneficial owners");
            return;
        }

        List<BeneficialOwner> owners = beneficialOwnerMapConverter.toModel(params);

        if (CollectionUtils.isEmpty(owners)) {
            errors.reject(StringUtils.EMPTY, "value.required");
        }

        Set<ConstraintViolation<BeneficialOwner>> violations = owners.stream()
                .map(beneficialOwnerValidator::validate)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        reportErrors(violations, violation -> {
            errors.reject(violation.getPropertyPath().toString(), violation.getMessage());
        });
    }
}

package com.nklmish.control.company;

import com.nklmish.entity.beneficial.BeneficialOwner;
import com.nklmish.entity.company.Company;
import com.nklmish.boundary.Converter;
import com.nklmish.boundary.company.form.CompanyForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CompanyManager {

    private final Converter<Company, CompanyForm> companyFormConverter;
    private final Converter<List<BeneficialOwner>, Map<?, ?>> beneficialOwnerMapConverter;
    private final org.springframework.validation.Validator companyRequestValidator;

    @Autowired
    public CompanyManager(Converter<Company, CompanyForm> companyFormConverter,
                          Converter<List<BeneficialOwner>, Map<?, ?>> beneficialOwnerMapConverter,
                          org.springframework.validation.Validator companyRequestValidator) {
        this.companyFormConverter = companyFormConverter;
        this.beneficialOwnerMapConverter = beneficialOwnerMapConverter;
        this.companyRequestValidator = companyRequestValidator;
    }

    public Company processForm(CompanyForm form) {
        return companyFormConverter.toModel(form);
    }

    public List<BeneficialOwner> processBeneficialOwners(Map<?, ?> params) {
        return beneficialOwnerMapConverter.toModel(params);
    }

    public org.springframework.validation.Validator getCompanyRequestValidator() {
        return companyRequestValidator;
    }
}

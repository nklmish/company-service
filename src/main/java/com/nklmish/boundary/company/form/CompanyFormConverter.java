package com.nklmish.boundary.company.form;

import com.nklmish.entity.company.Company;
import com.nklmish.boundary.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyFormConverter implements Converter<Company, CompanyForm> {

    @Override
    public Company toModel(CompanyForm companyForm) {
        if (companyForm == null) {
            return new Company();
        }
        Company company = new Company();
        company.setName(companyForm.getName());
        company.setAddress(companyForm.getAddress());
        company.setBeneficialOwners(companyForm.getBeneficialOwners());
        company.setContact(companyForm.getContact());
        return company;
    }
}

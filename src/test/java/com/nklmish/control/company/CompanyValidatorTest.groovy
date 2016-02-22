package com.nklmish.control.company

import com.nklmish.entity.address.Address
import com.nklmish.entity.beneficial.BeneficialOwner
import com.nklmish.entity.company.Company
import com.nklmish.entity.contact.Contact
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

class CompanyValidatorTest extends Specification {

    static Validator validator = Validation.buildDefaultValidatorFactory().getValidator()

    com.nklmish.control.Validator<Set<ConstraintViolation<Company>>, Company> companyValidationService

    void setup() {
        companyValidationService = new CompanyValidator(validator)
    }

    @Unroll("Company's Name: #company.name expectedErrors = #totalErrors")
    def "should be able to distinguish between valid and invalid company names"() {
        expect:
        companyValidationService.validate(company).size() == totalErrors

        where:
        company                                                                                                            || totalErrors
        generateCompanyUsingSensibleDefaults()                                                                             || 0
        generateCompanyUsingSensibleDefaults(name: " ")                                                                    || 1
        generateCompanyUsingSensibleDefaults(name: "a ",address: new Address())                                            || 3
        generateCompanyUsingSensibleDefaults(name: " ", address: new Address())                                            || 4
    }


    @Unroll("Company's Address: #company.address expectedErrors = #totalErrors")
    def "should be able to distinguish between valid and invalid company's address"() {
        expect:
        companyValidationService.validate(company).size() == totalErrors

        where:
        company                                                                                                            || totalErrors
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: _, city: _, country: _))            || 0
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: " x", city: " y", country: " z"))   || 0
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: "x ", city: " y", country: _))      || 0
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: _, city: " y ", country: _))        || 0
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: _, city: "  y", country: _))        || 0
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: _, city: _, country: " z "))        || 0
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: _, city: _, country: "  z"))        || 0
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: "", city: _, country: _))           || 1
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: " ", city: _, country: _))          || 1
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: _, city: "", country: _))           || 1
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: _, city: " ", country: _))          || 1
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: _, city: _, country: ""))           || 1
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: _, city: _, country: " "))          || 1
        generateCompanyUsingSensibleDefaults(address: new Address(locationDescription: "", city: "", country: ""))         || 3
    }


    @Unroll("Company's Contact: #company.contact expectedErrors = #totalErrors")
    def "should be ablet to distinguish between valid and invalid company's contact" () {
        expect:
        companyValidationService.validate(company).size() == totalErrors

        where:
        company                                                                                                            || totalErrors
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "", phoneNumber: 1))                              || 0
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: " ", phoneNumber: 1))                             || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "", phoneNumber: ""))                             || 0
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "", phoneNumber: "A"))                            || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "", phoneNumber: "12331231231231"))               || 0
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "", phoneNumber: "123-123-123"))                  || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "aaa", phoneNumber: ""))                          || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "@aa@bb", phoneNumber: ""))                       || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "john@.com.xy", phoneNumber: ""))                 || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "johnDoe@.com.com", phoneNumber: ""))             || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "john", phoneNumber: ""))                         || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "john@doe.com", phoneNumber: ""))                 || 0
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "john@%*.com", phoneNumber: ""))                  || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "johnDoe@.com", phoneNumber: ""))                 || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "johnDoe@yahoo.a", phoneNumber: ""))              || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "john..2016@gmail.com", phoneNumber: ""))         || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "john@gmail.com.1a", phoneNumber: ""))            || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "john()*@gmail.com", phoneNumber: ""))            || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "john.@gmail.com", phoneNumber: ""))              || 1
        generateCompanyUsingSensibleDefaults(contact: new Contact(email: "john@john@gmail.com", phoneNumber: ""))          || 1
    }

    @Unroll("Company #company expectedErrors = #totalErrors")
    def "should be able to validate or invalidate all company's information at once"() {
        expect:
        companyValidationService.validate(company).size() == totalErrors

        where:
        company                                                                                                            || totalErrors
        new Company()                                                                                                      || 3
        new Company(name: "a", address: null, beneficialOwners: null)                                                      || 2
        new Company(name: "a", address: new Address(), beneficialOwners: [])                                               || 4
        new Company(name: "", address: new Address(), beneficialOwners: [_])                                               || 4
        new Company(name: "a", address: new Address())                                                                     || 4
        generateCompanyUsingSensibleDefaults(name: "a ",address: new Address())                                            || 3
        generateCompanyUsingSensibleDefaults(name: "a", address: new Address(), beneficialOwners: [new BeneficialOwner()]) || 4
    }

    private static Company generateCompanyUsingSensibleDefaults(Map map = [:]) {
        return new Company(
                name: map.name ?: _,
                address: map.address ?:new Address(country: _, city: _, locationDescription: _),
                beneficialOwners:  map.beneficialOwners ?: [_],
                contact: map.contact ?: new Contact(email: "", phoneNumber: "")
        )
    }

}

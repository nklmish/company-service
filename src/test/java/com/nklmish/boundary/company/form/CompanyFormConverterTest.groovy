package com.nklmish.boundary.company.form


import com.nklmish.entity.address.Address
import com.nklmish.entity.beneficial.BeneficialOwner
import com.nklmish.entity.company.Company
import com.nklmish.boundary.Converter
import com.nklmish.entity.contact.Contact
import spock.lang.Specification
import spock.lang.Unroll


class CompanyFormConverterTest extends Specification {
    Converter<Company, CompanyForm> formConverter

    void setup() {
        formConverter = new CompanyFormConverter()
    }

    def "we should be able to convert company form to a company object"() {
        given:'a valid form'
        CompanyForm form = new CompanyForm(name: _, address: new Address(), beneficialOwners: [])

        when:'we perform conversion'
        Company convertedCompany = formConverter.toModel(form)

        then:
        convertedCompany == new Company(name: _, address: new Address(), beneficialOwners: [])
    }

    @Unroll("Application form: #applicationForm , expected Company: #expectedCompany")
    def "we shouldn't loose any information during conversion process"() {

        expect:
        formConverter.toModel(applicationForm) == expectedCompany

        where:
        applicationForm                                                                            || expectedCompany
        generateApplicationForm()                                                                  || new Company(name: "a", address: new Address( locationDescription: "x ", city: "y ", country: "z "), beneficialOwners: [new BeneficialOwner(name: "x")], contact:  new Contact())
        generateApplicationForm(contact: new Contact(email: 'foo@bar.foobar', phoneNumber: "123")) || new Company(name: "a", address: new Address( locationDescription: "x ", city: "y ", country: "z "), beneficialOwners: [new BeneficialOwner(name: "x")], contact: new Contact(email: 'foo@bar.foobar', phoneNumber: "123"))
        generateApplicationForm(contact: new Contact(email: 'foo@bar.foobar', phoneNumber: ""))    || new Company(name: "a", address: new Address( locationDescription: "x ", city: "y ", country: "z "), beneficialOwners: [new BeneficialOwner(name: "x")], contact: new Contact(email: 'foo@bar.foobar', phoneNumber: ""))
    }


    private static CompanyForm generateApplicationForm(Map map = [:]) {
        return new CompanyForm(name: "a",
                address: new Address(locationDescription: "x ", city: "y ", country: "z "),
                beneficialOwners: [new BeneficialOwner(name: "x")],
                contact: map.contact ?: new Contact(email: null, phoneNumber: null)
        )
    }

    def "we should be able to handle invalid form's conversion"() {
        given:'an invalid form'
        CompanyForm form = null

        when:'we perform conversion'
        Company convertedCompany = formConverter.toModel(form)

        then:
        convertedCompany == new Company()
    }
}

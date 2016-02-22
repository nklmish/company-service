package com.nklmish.control.beneficial

import com.nklmish.entity.beneficial.BeneficialOwner
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

class BeneficialOwnerValidatorTest extends Specification {

    static Validator validator = Validation.buildDefaultValidatorFactory().getValidator()

    @Shared
    com.nklmish.control.Validator<Set<ConstraintViolation<BeneficialOwner>>, BeneficialOwner> beneficialOwnerValidationService

    void setup() {
        beneficialOwnerValidationService = new BeneficialOwnerValidator(validator)
    }

    @Unroll("Owner #owner expectedErrors = #totalErrors")
    def "should be able to validate benefical owner"() {
        expect:
        beneficialOwnerValidationService.validate(owner).size() == totalErrors

        where:
        owner                             || totalErrors
        new BeneficialOwner(name: " a")   || 0
        new BeneficialOwner(name: "a ")   || 0
        new BeneficialOwner(name: "a")    || 0
        new BeneficialOwner(name: "")     || 1
        new BeneficialOwner(name: "  ")   || 1

    }
}

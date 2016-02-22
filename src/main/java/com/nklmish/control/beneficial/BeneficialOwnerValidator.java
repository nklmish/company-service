package com.nklmish.control.beneficial;

import com.nklmish.control.Validator;
import com.nklmish.entity.beneficial.BeneficialOwner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Service
public class BeneficialOwnerValidator implements Validator<Set<ConstraintViolation<BeneficialOwner>>, BeneficialOwner> {

    private final javax.validation.Validator validator;

    @Autowired
    public BeneficialOwnerValidator(javax.validation.Validator validator) {
        this.validator = validator;
    }

    @Override
    public Set<ConstraintViolation<BeneficialOwner>> validate(BeneficialOwner param) {
        return validator.validate(param);
    }
}

package com.nklmish.boundary.company.form;

import com.nklmish.entity.address.Address;
import com.nklmish.entity.beneficial.BeneficialOwner;
import com.nklmish.entity.contact.Contact;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompanyForm {
    private String name;
    private Address address;
    private List<BeneficialOwner> beneficialOwners = new ArrayList<>();
    private Contact contact;
}

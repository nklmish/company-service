package com.nklmish.entity.company;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nklmish.entity.address.Address;
import com.nklmish.entity.beneficial.BeneficialOwner;
import com.nklmish.entity.contact.Contact;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Document(collection = "company")
@Data
public class Company {

    @Id
    private String id;
    @NotBlank
    private String name;
    @Valid
    @NotNull
    private Address address;
    @Valid
    @NotEmpty
    private List<BeneficialOwner> beneficialOwners;
    @Valid
    private Contact contact;
}

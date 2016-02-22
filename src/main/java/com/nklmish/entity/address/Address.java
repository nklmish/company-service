package com.nklmish.entity.address;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class Address {
    @NotBlank
    private String locationDescription;
    @NotBlank
    private String city;
    @NotBlank
    private String country;
}

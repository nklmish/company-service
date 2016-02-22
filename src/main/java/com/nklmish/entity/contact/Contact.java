package com.nklmish.entity.contact;

import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class Contact {
    @Pattern(regexp = "^$|^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @Pattern(regexp = "^$|\\d{0,20}")
    private String phoneNumber;
}

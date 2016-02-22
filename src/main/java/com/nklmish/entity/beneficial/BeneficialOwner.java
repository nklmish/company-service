package com.nklmish.entity.beneficial;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class BeneficialOwner {
    @NotBlank
    private String name;
}

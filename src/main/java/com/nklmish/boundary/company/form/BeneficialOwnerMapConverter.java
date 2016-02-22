package com.nklmish.boundary.company.form;

import com.nklmish.entity.beneficial.BeneficialOwner;
import com.nklmish.boundary.Converter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.nklmish.boundary.api.ApiConstant.RequestParamsNames.Company.*;
@Component
public class BeneficialOwnerMapConverter implements Converter<List<BeneficialOwner>, Map<?, ?>> {

    @SuppressWarnings("unchecked")
    @Override
    public List<BeneficialOwner> toModel(Map<?, ?> params) {
        if (MapUtils.isEmpty(params)) {
            return Collections.emptyList();
        }

        List<LinkedHashMap<String, String>> newOwners = (List<LinkedHashMap<String,String>>) params.get(BENEFICIAL_OWNERS);

        if (CollectionUtils.isEmpty(newOwners)) {
            return Collections.emptyList();
        }

        return getBeneficialOwners(newOwners);
    }

    private List<BeneficialOwner> getBeneficialOwners(List<LinkedHashMap<String, String>> newOwners) {
        List<BeneficialOwner> owners  = new ArrayList<>();
        for (LinkedHashMap<String, String> map : newOwners) {

            map.entrySet().stream()
                    .filter(entry -> entry.getKey().equals(BENEFICIAL_OWNERS_FIELD_NAME))
                    .forEach(entry -> {
                BeneficialOwner owner = new BeneficialOwner();
                owner.setName(map.get(BENEFICIAL_OWNERS_FIELD_NAME));
                owners.add(owner);
            });
        }
        return owners;
    }
}

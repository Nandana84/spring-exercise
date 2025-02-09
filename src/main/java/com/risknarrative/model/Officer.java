package com.risknarrative.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Officer {
    private Address address;
    private String name;
    private String appointed_on;
    private String officer_role;
    private String resigned_on;
    private String occupation;
    private String country_of_residence;
    private String nationality;
}

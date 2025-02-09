package com.risknarrative.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Address {
    private String premises;
    private String postal_code;
    private String country;
    private String locality;
    private String address_line_1;
}

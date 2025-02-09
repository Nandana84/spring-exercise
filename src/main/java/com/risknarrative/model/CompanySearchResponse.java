package com.risknarrative.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CompanySearchResponse {
    private int total_results;
    private List<Company> items;
}

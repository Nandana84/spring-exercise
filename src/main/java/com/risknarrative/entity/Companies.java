package com.risknarrative.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Companies {
    @Id
    @JsonProperty("company_number")
    private Long companyNumber;

    @JsonProperty("company_type")
    private String companyType;

    private String title;

    @JsonProperty("company_status")
    private String companyStatus;

    @JsonProperty("date_of_creation")
    private String dateOfCreation;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Addresses address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Officers> officers;
}


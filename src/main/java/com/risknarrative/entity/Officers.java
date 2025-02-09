package com.risknarrative.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"id", "occupation","nationality"})
public class Officers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JsonProperty("appointed_on")
    private String appointedOn;

    @JsonProperty("officer_role")
    private String officerRole;

    private String occupation;
    private String nationality;
    private String resignedOn;
    private String countryOfResidence;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Addresses address;

    @ManyToOne
    @JoinColumn(name = "company_number", referencedColumnName = "companyNumber")
    private Companies company;

}

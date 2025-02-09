package com.risknarrative.mapper;

import com.risknarrative.entity.Addresses;
import com.risknarrative.entity.Companies;
import com.risknarrative.entity.Officers;
import com.risknarrative.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonMapper {

    public CompanySearchResponse buildCompanyOfficersResponse(List<Companies> companiesList){
        Company company = null;
        for(Companies companyDetails:companiesList){
            Officer officer = Officer.builder()
                    .name(companyDetails.getOfficers().stream().map(Officers::getName).findFirst().orElse(StringUtils.EMPTY))
                    .officer_role(companyDetails.getOfficers().stream().map(Officers::getOfficerRole).findFirst().orElse(StringUtils.EMPTY))
                    .appointed_on(companyDetails.getOfficers().stream().map(Officers::getAppointedOn).findFirst().orElse(StringUtils.EMPTY))
                    .address(buildAddresses(companyDetails.getOfficers().stream().map(Officers::getAddress).findFirst().orElse(null)))
                    .build();

            company = Company.builder()
                    .company_type(companyDetails.getCompanyType())
                    .company_number(companyDetails.getCompanyNumber())
                    .title(companyDetails.getTitle())
                    .company_status(companyDetails.getCompanyStatus())
                    .address(buildAddresses(companyDetails.getAddress()))
                    .date_of_creation(companyDetails.getDateOfCreation())
                    .officers(List.of(officer)).build();
        }

        return CompanySearchResponse.builder().total_results(companiesList.size()).items(List.of(company)).build();
    }
    public Addresses buildAddress(Address company){
        return Addresses.builder()
                .premises(company.getPremises())
                .postalCode(company.getPostal_code())
                .country(company.getCountry())
                .locality(company.getLocality())
                .addressLine1(company.getAddress_line_1()).build();
    }

    public Address buildAddresses(Addresses company){
        return Address.builder()
                .premises(company.getPremises())
                .postal_code(company.getPostalCode())
                .country(company.getCountry())
                .locality(company.getLocality())
                .address_line_1(company.getAddressLine1()).build();
    }

}

package com.risknarrative.service;

import com.risknarrative.entity.Addresses;
import com.risknarrative.entity.Companies;
import com.risknarrative.entity.Officers;
import com.risknarrative.mapper.CommonMapper;
import com.risknarrative.model.*;
import com.risknarrative.repository.AddressRepository;
import com.risknarrative.repository.CompanyRepository;
import com.risknarrative.repository.OfficerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {

    Logger logger = LoggerFactory.getLogger(CompanyService.class);

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private OfficerRepository officerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CommonMapper mapper;

    //Saving Data to DB
    public Companies saveCompany(Companies company) {
        return companyRepository.save(company);
    }

    public List<Companies> getCompanyOfficerDetails() {
        return companyRepository.findAll();
    }

    public Officers saveCompanyOfficers(Officers officers) {
        return officerRepository.save(officers);
    }

    public Addresses saveAddress(Addresses address){
        return addressRepository.save(address);
    }

    //insert data to db companies, address and officer tables
    @Transactional
    public void saveCompanyOfficerDetails(CompanySearchResponse searchResponse, OfficerResponse companyOfficers){
        for(Company company: searchResponse.getItems()) {
            saveCompany(Companies.builder()
                    .companyStatus(company.getCompany_status())
                    .companyNumber(company.getCompany_number())
                    .title(company.getTitle())
                    .companyType(company.getCompany_type())
                    .dateOfCreation(company.getDate_of_creation())
                    .companyType(company.getCompany_type())
                    .address(saveAddress(mapper.buildAddress(company.getAddress())))
                    .officers(List.of(saveCompanyOfficers(getCompanyOfficers(companyOfficers, searchResponse))))
                    .build());
        }

    }

    @Transactional
    public Officers getCompanyOfficers(OfficerResponse officerResponse, CompanySearchResponse searchResponse){
        Officer officer = officerResponse.getItems().stream().findFirst().orElse(null);
        Company company = searchResponse.getItems().stream().findFirst().orElse(null);
        return Officers.builder()
                .nationality(officer.getNationality())
                .countryOfResidence(officer.getCountry_of_residence())
                .officerRole(officer.getOfficer_role())
                .name(officer.getName())
                .appointedOn(officer.getAppointed_on())
                .resignedOn(officer.getResigned_on())
                .occupation(officer.getOccupation())
                .address(saveAddress(mapper.buildAddress(officer.getAddress())))
                .company(saveCompany(Companies.builder()
                        .companyStatus(company.getCompany_status())
                        .companyNumber(company.getCompany_number())
                        .title(company.getTitle())
                        .companyType(company.getCompany_type())
                        .dateOfCreation(company.getDate_of_creation())
                        .companyType(company.getCompany_type())
                        .build()))
                .build();
    }

}

package com.risknarrative.service;

import com.risknarrative.entity.Addresses;
import com.risknarrative.entity.Companies;
import com.risknarrative.entity.Officers;
import com.risknarrative.mapper.CommonMapper;
import com.risknarrative.model.*;
import com.risknarrative.repository.AddressRepository;
import com.risknarrative.repository.CompanyRepository;
import com.risknarrative.repository.OfficerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private OfficerRepository officerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private Company mockCompany;

    @Mock
    private CommonMapper mapper;

    @InjectMocks
    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCompany() {
        Companies company = new Companies();
        company.setCompanyNumber(650024412L);
        company.setTitle("Test Company");

        when(companyRepository.save(any(Companies.class))).thenReturn(company);

        Companies savedCompany = companyService.saveCompany(company);

        assertEquals("Test Company", savedCompany.getTitle());
        assertEquals(650024412L, savedCompany.getCompanyNumber());
    }

   @Test
    void testGetCompanyOfficerDetails() {
        Companies company1 = new Companies();
        company1.setCompanyNumber(112121L);
        company1.setTitle("Company 1");

        Companies company2 = new Companies();
        company2.setCompanyNumber(2222222L);
        company2.setTitle("Company 2");

        List<Companies> companiesList = Arrays.asList(company1, company2);

        when(companyRepository.findAll()).thenReturn(companiesList);

        List<Companies> result = companyService.getCompanyOfficerDetails();

        assertEquals(2, result.size());
        assertEquals("Company 1", result.get(0).getTitle());
        assertEquals("Company 2", result.get(1).getTitle());
    }

    @Test
    void testSaveCompanyOfficers() {
        Officers mockOfficer = new Officers();
        mockOfficer.setId(1L);
        mockOfficer.setName("John Doe");

        when(officerRepository.save(any(Officers.class))).thenReturn(mockOfficer);

        Officers savedOfficer = companyService.saveCompanyOfficers(mockOfficer);

        assertEquals(1L, savedOfficer.getId());
        assertEquals("John Doe", savedOfficer.getName());
        verify(officerRepository).save(mockOfficer);
    }

    @Test
    void testSaveAddress() {
        Addresses mockAddress = new Addresses();
        mockAddress.setId(1L);
        mockAddress.setAddressLine1("123 Main St");

        when(addressRepository.save(any(Addresses.class))).thenReturn(mockAddress);

        Addresses savedAddress = companyService.saveAddress(mockAddress);

        assertEquals(1L, savedAddress.getId());
        assertEquals("123 Main St", savedAddress.getAddressLine1());
        verify(addressRepository).save(mockAddress);
    }

    @Test
    public void testgetCompanyOfficers() {

        OfficerResponse officerResponse = buildOfficerResponse();
        CompanySearchResponse searchResponseResponse = buildSearchResponse();
        Officers officer = companyService.getCompanyOfficers(officerResponse, searchResponseResponse);

        assertNotNull(officer);
        assertEquals("British", officer.getNationality());
        assertEquals("UK", officer.getCountryOfResidence());
        assertEquals("Director", officer.getOfficerRole());
        assertEquals("John Doe", officer.getName());
        assertEquals("2020-01-01", officer.getAppointedOn());
        assertEquals("2021-01-01", officer.getResignedOn());
        assertEquals("Engineer", officer.getOccupation());
    }

    private OfficerResponse buildOfficerResponse() {

        Address address = Address.builder().address_line_1("Cranford Close").postal_code("SW20 0DP").country("England").locality("London").premises("5").build();
        OfficerResponse officerResponse = new OfficerResponse();
        officerResponse.setItems(List.of(Officer.builder()
                .nationality("British")
                .country_of_residence("UK")
                .officer_role("Director")
                .name("John Doe")
                .appointed_on("2020-01-01")
                .resigned_on("2021-01-01")
                .occupation("Engineer")
                .address(address).build()));
        return officerResponse;
    }

    private CompanySearchResponse buildSearchResponse() {
        Address address = Address.builder().address_line_1("North Leverton").postal_code("DN22 0A").country("England").locality("Retford").premises("Boswell Cottage Main Street").build();

        return CompanySearchResponse.builder().items(List.of(Company.builder()
                .company_status("active")
                .company_number(12345L)
                .title("Test Company")
                .company_type("Private")
                .date_of_creation("2020-01-01")
                .address(address).build()))
                .build();
    }

}


package com.risknarrative.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.risknarrative.entity.Officers;
import com.risknarrative.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompanyServiceIntegrationTest {

    @LocalServerPort
    private int port;

    private WireMockServer wireMockServer;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private CompanyService companyService;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(); // Default WireMock server port
        wireMockServer.start();
        configureFor("localhost", 8081);
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testGetCompanyOfficers() throws URISyntaxException, Exception {

        OfficerResponse officerResponse = buildOfficerResponse();
        ObjectMapper objectMapper = new ObjectMapper();



        CompanySearchResponse searchResponse = buildSearchResponse();
        String searchResponseJson = objectMapper.writeValueAsString(searchResponse);

        wireMockServer.stubFor(get(urlPathEqualTo("/api/company"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(searchResponseJson)));

        // Act
        URI uri = new URI("http://localhost:" + port + "/api/company?company=TestCompany&domain=testdomain.com");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer test-token");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Officers> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Officers.class);

        // Assert
        Officers officers = response.getBody();
        assertNotNull(officers);
    }

    private OfficerResponse buildOfficerResponse() {

        Address address = Address.builder().address_line_1("Cranford Close").postal_code("SW20 0DP").country("England").locality("London").premises("5").build();
        OfficerResponse officerResponse = new OfficerResponse();
        officerResponse.setItems(List.of(Officer.builder()
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



package com.risknarrative.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.risknarrative.service.TruProxyService;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(WireMockExtension.class)
public class CompanyControllerIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;
    private WireMockServer wireMockServer;

    @Autowired
    private TruProxyService companyService;

    @BeforeEach
    void setUp() {
        this.wireMockServer = new WireMockServer(Options.DYNAMIC_PORT);
        this.wireMockServer.start();
        configureFor("https://exercise.trunarrative.cloud/TruProxyAPI/rest/Companies/v1", this.wireMockServer.port());
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

  @SneakyThrows
  @Test
  @DisplayName("should return not found fail")
  void testFailForCompanyUri() {
      String companyJson = getFileContentAsString("src/test/resources/companyresponse.json");
      wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/Search/*"))
                      .willReturn(WireMock.aResponse().withHeader("Content-Type", "application/json")));
      this.mockMvc.perform(MockMvcRequestBuilders.post("/Search")
              .contentType(MediaType.APPLICATION_JSON)
              .content(getFileContentAsString("src/test/resources/requestbody.json")))
              .andExpect(MockMvcResultMatchers.status().is4xxClientError());
  }

@SneakyThrows
@Test
@DisplayName("should return success")
void testSuccessForCompanyUri() {
    String companyJson = getFileContentAsString("src/test/resources/companyresponse.json");
    wireMockServer.stubFor(WireMock.post(WireMock.urlMatching("/Search/*"))
            .willReturn(WireMock.aResponse().withHeader("Content-Type", "application/json")));
    this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/search?isActive=true")
            .contentType(MediaType.APPLICATION_JSON)
            .header("x-api-key","")
            .content(getFileContentAsString("src/test/resources/requestbody.json")))
            .andExpect(MockMvcResultMatchers.status().isOk());
}

    public static String getFileContentAsString(String path) throws IOException{
        String inputJson = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
        return inputJson;
    }

}
package com.example.inventoryservice.service;

import com.example.inventoryservice.client.MemberServiceClient;
import com.example.inventoryservice.dto.InventoryRequest;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.mock.WireMockConfig;
import com.example.inventoryservice.repository.InventoryRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
public class InventoryServiceTest {

    @Autowired
    WireMockServer mockMemberService;

    @Autowired
    MemberServiceClient memberServiceClient;

    @Autowired
    InventoryServiceImpl inventoryService;

    @Autowired
    InventoryRepository inventoryRepository;

    @BeforeEach
    public void setUp() {
        String email = "yejin@naver.com";
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(email);
    }

    @Order(1)
    @Test
    public void createTest() {
        // given
        String email = "yejin@naver.com";
        Long sellerId = 1L;

        mockMemberService.stubFor(WireMock.get(urlEqualTo("/members/email/" + email))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(String.valueOf(sellerId))
                )
        );
        InventoryRequest request = new InventoryRequest("ADIDAS", 1000, 100);

        // when
        InventoryResponse response = inventoryService.create(request);

        // then
        assertThat(response.getSellerId()).isEqualTo(sellerId);
        assertThat(response.getProductName()).isEqualTo(request.getProductName());
        assertThat(response.getUnitPrice()).isEqualTo(request.getUnitPrice());
    }

    @Order(2)
    @Test
    public void updateTest() {
        // given
        String email = "yejin@naver.com";
        Long sellerId = 1L;
        Long productId = 1L;

        mockMemberService.stubFor(WireMock.get(urlEqualTo("/members/email/" + email))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(String.valueOf(sellerId))
                )
        );
        InventoryRequest request = new InventoryRequest("NEW BALANCE", 2000, 200);

        // when
        InventoryResponse response = inventoryService.update(productId, request);

        // then
        assertThat(response.getSellerId()).isEqualTo(sellerId);
        assertThat(response.getProductName()).isEqualTo(request.getProductName());
        assertThat(response.getStock()).isEqualTo(request.getStock());
    }

    @Order(3)
    @Test
    public void deleteTest() {
        // given
        String email = "yejin@naver.com";
        Long sellerId = 1L;
        Long productId = 1L;

        mockMemberService.stubFor(WireMock.get(urlEqualTo("/members/email/" + email))
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withBody(String.valueOf(sellerId))
                )
        );

        // when
        inventoryService.delete(productId);

        // then
        assertThat(inventoryRepository.findById(productId)).isEmpty();
    }
}

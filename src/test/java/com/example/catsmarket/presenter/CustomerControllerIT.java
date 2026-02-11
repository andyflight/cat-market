package com.example.catsmarket.presenter;

import com.example.catsmarket.AbstractIT;
import com.example.catsmarket.application.CustomerService;
import com.example.catsmarket.data.CustomerRepository;
import com.example.catsmarket.domain.Customer;
import com.example.catsmarket.presenter.dto.customer.CustomerRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("Customer Controller IT")
public class CustomerControllerIT extends AbstractIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @SpyBean
    private CustomerService customerService;

    private final CustomerRequestDto validCustomerRequest = CustomerRequestDto.builder()
            .fullName("John Doe")
            .username("johndoe2")
            .password("password123")
            .email("john.doe@example.com")
            .build();

    private final CustomerRequestDto invalidCustomerRequest = CustomerRequestDto.builder()
            .fullName("")
            .username("")
            .password("")
            .email("invalid-email")
            .build();

    private final CustomerRequestDto invalidPersistenceRequest = CustomerRequestDto.builder()
            .fullName("John Doe")
            .username("johndoe")
            .password("password123")
            .email("john.doe@example.com")
            .build();

    @BeforeEach
    void setupBefore() {
        reset(customerService);
        customerRepository.deleteAll();
        customerRepository.save(Customer.builder()
                .id(1L)
                .customerReference(UUID.fromString("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640"))
                .fullName("John Doe")
                .username("johndoe")
                .password("password123")
                .email("john.doe@example.com")
                .build());
    }

    @Test
    @SneakyThrows
    void getCustomerByReference_ShouldReturnCustomer_WhenCustomerExists() {

        mockMvc.perform(get("/api/v1/customers/3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerReference", is("3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")))
                .andExpect(jsonPath("$.fullName", is("John Doe")))
                .andExpect(jsonPath("$.username", is("johndoe")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")));
    }

    @Test
    @SneakyThrows
    void getCustomerByReference_ShouldThrowException_WhenCustomerDoesNotExist() {

        mockMvc.perform(get("/api/v1/customers/00000000-0000-0000-0000-000000000000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("customer-not-found")))
                .andExpect(jsonPath("$.title", is("Customer Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("Customer with reference 00000000-0000-0000-0000-000000000000 not found")))
                .andExpect(jsonPath("$.instance", is("/api/v1/customers/00000000-0000-0000-0000-000000000000")));
    }

    @Test
    @SneakyThrows
    void createCustomer_ShouldCreateCustomer_WhenRequestValid() {
        mockMvc.perform(post("/api/v1/customers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCustomerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerReference").exists())
                .andExpect(jsonPath("$.fullName", is(validCustomerRequest.fullName())))
                .andExpect(jsonPath("$.username", is(validCustomerRequest.username())))
                .andExpect(jsonPath("$.email", is(validCustomerRequest.email())));

    }

    @Test
    @SneakyThrows
    void createCustomer_ShouldThrowException_WhenRequestInvalid() {
        mockMvc.perform(post("/api/v1/customers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCustomerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type", is("urn:problem-type:validation-error")))
                .andExpect(jsonPath("$.title", is("Field Validation Exception")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.detail", is("Request validation failed")))
                .andExpect(jsonPath("$.instance", is("/api/v1/customers")))
                .andExpect(jsonPath("$.invalidParams[*].fieldName").exists())
                .andExpect(jsonPath("$.invalidParams[*].reason").exists());
    }

    @Test
    @SneakyThrows
    void updateCustomer_ShouldUpdateCustomer_WhenRequestValid() {

        CustomerRequestDto updateRequest = CustomerRequestDto.builder()
                .fullName("John Updated")
                .username("johnupdated")
                .password("newpassword123")
                .email("john.updated@example.com")
                .build();

        mockMvc.perform(put("/api/v1/customers/3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName", is(updateRequest.fullName())))
                .andExpect(jsonPath("$.username", is(updateRequest.username())))
                .andExpect(jsonPath("$.email", is(updateRequest.email())));
    }

    @Test
    @SneakyThrows
    void updateCustomer_ShouldThrowException_WhenCustomerNotFound() {
        CustomerRequestDto updateRequest = CustomerRequestDto.builder()
                .fullName("John Updated")
                .username("johnupdated")
                .password("newpassword123")
                .email("john.updated@example.com")
                .build();

        mockMvc.perform(put("/api/v1/customers/00000000-0000-0000-0000-000000000000")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type", is("customer-not-found")))
                .andExpect(jsonPath("$.title", is("Customer Not Found")))
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.detail", is("Customer with reference 00000000-0000-0000-0000-000000000000 not found")))
                .andExpect(jsonPath("$.instance", is("/api/v1/customers/00000000-0000-0000-0000-000000000000")));
    }

    @Test
    @SneakyThrows
    void deleteCustomer_ShouldDeleteCustomer_WhenCustomerExists() {

        mockMvc.perform(delete("/api/v1/customers/3bb7dce2-b2eb-4d7c-9b8f-edb2397c4640")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successful")));

    }

    @Test
    @SneakyThrows
    void deleteCustomer_ShouldDoNothing_WhenCustomerDoesNotExist() {
        mockMvc.perform(delete("/api/v1/customers/00000000-0000-0000-0000-000000000000")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successful")));
    }

    @Test
    @SneakyThrows
    void createCustomer_ShouldThrowException_WhenUsernameIsNotUnique() {
        mockMvc.perform(post("/api/v1/customers")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPersistenceRequest)))
                .andExpect(status().isInternalServerError());

    }
}

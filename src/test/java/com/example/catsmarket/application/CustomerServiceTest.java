package com.example.catsmarket.application;

import com.example.catsmarket.application.context.customer.CustomerContext;
import com.example.catsmarket.application.exceptions.CustomerNotFoundException;
import com.example.catsmarket.data.CustomerRepository;
import com.example.catsmarket.domain.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("Customer Service Test")
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void createCustomer_shouldCreateAndReturnCustomer() {
        CustomerContext context = CustomerContext.builder()
                .fullName("John Doe")
                .username("johndoe")
                .password("password123")
                .email("john.doe@example.com")
                .build();

        Customer expectedCustomer = Customer.builder()
                .id(1L)
                .customerReference(UUID.randomUUID())
                .fullName(context.getFullName())
                .username(context.getUsername())
                .password(context.getPassword())
                .email(context.getEmail())
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);

        Customer actualCustomer = customerService.createCustomer(context);

        assertNotNull(actualCustomer);
        assertEquals(expectedCustomer.getFullName(), actualCustomer.getFullName());
        assertEquals(expectedCustomer.getUsername(), actualCustomer.getUsername());
        assertEquals(expectedCustomer.getEmail(), actualCustomer.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void updateCustomer_shouldUpdateAndReturnCustomer_WhenCustomerExists() {
        UUID reference = UUID.randomUUID();
        Customer existingCustomer = Customer.builder()
                .id(1L)
                .customerReference(reference)
                .fullName("John Doe")
                .username("johndoe")
                .password("password123")
                .email("john.doe@example.com")
                .build();

        CustomerContext context = CustomerContext.builder()
                .fullName("John Doe Updated")
                .username("johnupdated")
                .password("newpassword123")
                .email("john.updated@example.com")
                .build();

        Customer updatedCustomer = existingCustomer.toBuilder()
                .fullName(context.getFullName())
                .username(context.getUsername())
                .password(context.getPassword())
                .email(context.getEmail())
                .build();

        when(customerRepository.findByReference(reference)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer actualCustomer = customerService.updateCustomer(reference, context);

        assertNotNull(actualCustomer);
        assertEquals(context.getFullName(), actualCustomer.getFullName());
        assertEquals(context.getUsername(), actualCustomer.getUsername());
        assertEquals(context.getEmail(), actualCustomer.getEmail());
        verify(customerRepository, times(1)).findByReference(reference);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void updateCustomer_shouldThrowException_WhenCustomerNotFound() {
        UUID reference = UUID.randomUUID();
        CustomerContext context = CustomerContext.builder()
                .fullName("John Doe Updated")
                .username("johnupdated")
                .password("newpassword123")
                .email("john.updated@example.com")
                .build();

        when(customerRepository.findByReference(reference)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(reference, context));
        verify(customerRepository, times(1)).findByReference(reference);
    }

    @Test
    void getByReference_shouldReturnCustomer_WhenCustomerExists() {
        UUID reference = UUID.randomUUID();
        Customer expectedCustomer = Customer.builder()
                .id(1L)
                .customerReference(reference)
                .fullName("John Doe")
                .username("johndoe")
                .password("password123")
                .email("john.doe@example.com")
                .build();

        when(customerRepository.findByReference(reference)).thenReturn(Optional.of(expectedCustomer));

        Customer actualCustomer = customerService.getByReference(reference);

        assertNotNull(actualCustomer);
        assertEquals(expectedCustomer.getFullName(), actualCustomer.getFullName());
        assertEquals(expectedCustomer.getUsername(), actualCustomer.getUsername());
        assertEquals(expectedCustomer.getEmail(), actualCustomer.getEmail());
        verify(customerRepository, times(1)).findByReference(reference);
    }

    @Test
    void getByReference_shouldThrowException_WhenCustomerNotFound() {
        UUID reference = UUID.randomUUID();

        when(customerRepository.findByReference(reference)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getByReference(reference));
        verify(customerRepository, times(1)).findByReference(reference);
    }

    @Test
    void deleteCustomer_shouldDeleteCustomer_Anyway() {
        UUID reference = UUID.randomUUID();

        doNothing().when(customerRepository).deleteByReference(reference);

        customerService.deleteCustomer(reference);

        verify(customerRepository, times(1)).deleteByReference(reference);
    }

}


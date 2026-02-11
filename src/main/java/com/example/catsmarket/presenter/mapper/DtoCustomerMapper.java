package com.example.catsmarket.presenter.mapper;

import com.example.catsmarket.application.context.customer.CustomerContext;
import com.example.catsmarket.domain.Customer;
import com.example.catsmarket.presenter.dto.customer.CustomerRequestDto;
import com.example.catsmarket.presenter.dto.customer.CustomerResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoCustomerMapper {

    CustomerContext toContext(CustomerRequestDto dto);

    CustomerResponseDto toDto(Customer customer);
}
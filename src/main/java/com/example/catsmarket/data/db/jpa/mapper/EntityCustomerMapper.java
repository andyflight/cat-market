package com.example.catsmarket.data.db.jpa.mapper;

import com.example.catsmarket.data.db.jpa.entity.CustomerEntity;
import com.example.catsmarket.domain.Customer;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface EntityCustomerMapper {

    Customer toDomain(CustomerEntity entity);

    CustomerEntity toEntity(Customer customer);

}

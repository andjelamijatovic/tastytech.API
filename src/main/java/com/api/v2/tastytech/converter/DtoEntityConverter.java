package com.api.v2.tastytech.converter;

public interface DtoEntityConverter <I, O, E>{

    O toDto(E entity); // convert entity to output DTO
    E toEntity(I input) throws Exception; // convert input DTO to entity

}

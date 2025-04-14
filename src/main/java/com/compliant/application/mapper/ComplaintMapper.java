package com.compliant.application.mapper;


import com.compliant.application.dto.ComplaintDto;
import com.compliant.domain.model.Complaint;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ComplaintMapper {

    ComplaintDto toEntity(Complaint complaint);

    Complaint toDto(ComplaintDto ComplaintDto);

}

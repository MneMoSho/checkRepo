package com.example.checkrepo.mapper;

import com.example.checkrepo.dto.CompanyDto;
import com.example.checkrepo.entities.Company;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CompanyMapper {

    public CompanyDto toCompanyDto(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setCompanyId(company.getId());
        dto.setCompanyName(company.getCompanyName());
        if (company.getFlights() != null) {
            dto.setFlights(company.getFlights().stream()
                            .map(FlightMapper::toFlightDto).collect(Collectors.toSet()));
        }
        return dto;
    }

    public CompanyDto toDtoShallow(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setCompanyId(company.getId());
        dto.setCompanyName(company.getCompanyName());
        return dto;
    }

    public Company toEntity(CompanyDto companyDto) {
        Company company = new Company();
        company.setCompanyName(companyDto.getCompanyName());
        company.setId(companyDto.getCompanyId());
        return company;
    }

    public List<CompanyDto> toDtoList(List<Company> companysList) {
        List<CompanyDto> dtoList = new ArrayList<>();
        for (Company source : companysList) {
            dtoList.add(CompanyMapper.toCompanyDto(source));
        }
        return dtoList;
    }

    public List<Company> toEntityList(List<CompanyDto> companyDtoList) {
        List<Company> list = new ArrayList<>();
        for (CompanyDto source : companyDtoList) {
            list.add(CompanyMapper.toEntity(source));
        }
        return list;
    }
}
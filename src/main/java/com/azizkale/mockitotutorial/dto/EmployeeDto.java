package com.azizkale.mockitotutorial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmployeeDto {
    private Integer id;
    private String name;
    private String department;
    private String gender;
    private Date dob;

}

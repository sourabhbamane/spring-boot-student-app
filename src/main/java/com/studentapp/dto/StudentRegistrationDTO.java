package com.studentapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentRegistrationDTO {
    private String name;
    private String email;
    private String dob;
    private String gender;
    private String address;
    private String userUsername;
    private String userPassword;
}
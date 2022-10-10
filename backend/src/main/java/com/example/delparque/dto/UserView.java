package com.example.delparque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserView {
    private String id;
    private String email;
    private String name;
    private String telephoneNumber;
    private String emergencyNumber;
    private List<String> roles;
}

package com.backendproject.hotel_system.Models;


import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter
@NoArgsConstructor
@Data
@Entity
public class User extends BaseModel{


  private String firstName;
 private String lastName;
 private String email;
  private  String password;
  private UserType userType;
 private String phone;
  private String address;
  private String city;
 private String state;
 private int zip;

}

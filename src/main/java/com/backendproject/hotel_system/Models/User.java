package com.backendproject.hotel_system.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.usertype.UserType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel {
 private String firstName;
 private String lastName;
 private String email;
 private String password;
 private String userRole;
 private String phone;
 private String address;
 private String city;
 private String state;
 private int zip;
}

package com.backendproject.hotel_system.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Token extends BaseModel{
    private String token;
    private Date expires;
    private long userid;
    private boolean hasExpired;
}

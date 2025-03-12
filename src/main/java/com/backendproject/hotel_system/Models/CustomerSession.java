package com.backendproject.hotel_system.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter @Setter
@Data
@NoArgsConstructor
@Entity
public class CustomerSession extends BaseModel {

    @OneToOne
    private User user;

    private CustomerSessionStatus customerSessionStatus;

    public boolean isActive() {
        return this.customerSessionStatus == CustomerSessionStatus.ACTIVE;
    }
}


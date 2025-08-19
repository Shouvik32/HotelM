package com.backendproject.hotel_system.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter
@Data
@NoArgsConstructor
@Entity
public class CustomerSession extends BaseModel {

    @OneToOne
    private User user;
    private CustomerSessionStatus customerSessionStatus;
    @OneToMany
    private List<Booking> bookings;
    private Date sessionStart;
    private Date sessionEnd;
    public boolean isActive() {
        return this.customerSessionStatus == CustomerSessionStatus.ACTIVE;
    }
}


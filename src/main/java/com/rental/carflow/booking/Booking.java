package com.rental.carflow.booking;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Table(name = "bookings")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
}

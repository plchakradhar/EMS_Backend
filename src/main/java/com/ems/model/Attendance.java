// Backend: Attendance Model
package com.ems.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.Serializable;

@Entity
@Table(name = "attendance")
public class Attendance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String empId;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private LocalTime checkIn;

    @Column
    private LocalTime checkOut;

    @Column(nullable = false)
    private String status;  // e.g., "Present", "Absent", "Late"

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public LocalTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalTime checkIn) { this.checkIn = checkIn; }
    public LocalTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalTime checkOut) { this.checkOut = checkOut; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
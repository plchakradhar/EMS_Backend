// Backend: Payroll Model
package com.ems.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "payroll")
public class Payroll implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String empId;

    @Column(nullable = false)
    private String month;  // e.g., "2023-11"

    @Column(nullable = false)
    private double salary;

    @Column(nullable = false)
    private String status;  // e.g., "Paid", "Pending"

    @Column
    private String payslipUrl;  // URL or path to payslip PDF

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmpId() { return empId; }
    public void setEmpId(String empId) { this.empId = empId; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPayslipUrl() { return payslipUrl; }
    public void setPayslipUrl(String payslipUrl) { this.payslipUrl = payslipUrl; }
}
package com.ems.repository;

import com.ems.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmpId(String empId);
    List<LeaveRequest> findAll();
}
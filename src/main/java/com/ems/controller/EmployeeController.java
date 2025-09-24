package com.ems.controller;

import com.ems.model.Employee;
import com.ems.model.LeaveRequest;
import com.ems.model.Payroll;
import com.ems.model.Attendance;
import com.ems.repository.EmployeeRepository;
import com.ems.repository.LeaveRequestRepository;
import com.ems.repository.PayrollRepository;
import com.ems.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private PayrollRepository payrollRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;

    // Get all employees (for Admin Dashboard)
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get pending employees (for Signup Requests)
    @GetMapping("/pending")
    public List<Employee> getPendingEmployees() {
        return employeeRepository.findByStatus("pending");
    }

    // Login for employee
    @PostMapping("/login")
    public ResponseEntity<Employee> login(@RequestBody Map<String, String> credentials) {
        String empId = credentials.get("loginId");
        String password = credentials.get("password");
        Optional<Employee> optionalEmployee = employeeRepository.findByEmpId(empId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            if (employee.getPassword().equals(password) && "approved".equals(employee.getStatus())) {
                return ResponseEntity.ok(employee);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // or throw exception
    }

    // Create new employee (from Signup or Add in Admin)
    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Employee employee) {
        // Generate empId (e.g., random 4-digit)
        Random random = new Random();
        employee.setEmpId(String.format("%04d", random.nextInt(10000)));
        employee.setStatus("pending"); // Default to pending for signup
        return employeeRepository.save(employee);
    }

    // Approve employee
    @PostMapping("/approve/{id}")
    public ResponseEntity<Void> approveEmployee(@PathVariable Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setStatus("approved");
            employeeRepository.save(employee);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Reject employee (delete pending)
    @DeleteMapping("/reject/{id}")
    public ResponseEntity<Void> rejectEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Update full employee (for Admin Edit)
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setName(updatedEmployee.getName());
            employee.setUsername(updatedEmployee.getUsername());
            employee.setPassword(updatedEmployee.getPassword());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setMobile(updatedEmployee.getMobile());
            employee.setGender(updatedEmployee.getGender());
            employee.setDepartment(updatedEmployee.getDepartment());
            employee.setPosition(updatedEmployee.getPosition());
            employee.setJoinDate(updatedEmployee.getJoinDate());
            employee.setStatus(updatedEmployee.getStatus());
            return ResponseEntity.ok(employeeRepository.save(employee));
        }
        return ResponseEntity.notFound().build();
    }

    // Delete employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // Get leave history for the logged-in employee
    @GetMapping("/leaves")
    public List<LeaveRequest> getLeaveHistory(@RequestParam String empId) {
        return leaveRequestRepository.findByEmpId(empId);
    }

    // Submit a new leave request
    @PostMapping("/leave-request")
    public LeaveRequest submitLeaveRequest(@RequestBody Map<String, String> leaveData, @RequestParam String empId) {
        LeaveRequest leave = new LeaveRequest();
        leave.setEmpId(empId);
        leave.setType(leaveData.get("type"));
        leave.setStartDate(LocalDate.parse(leaveData.get("startDate")));
        leave.setEndDate(LocalDate.parse(leaveData.get("endDate")));
        leave.setReason(leaveData.get("reason"));
        leave.setStatus("Pending");
        return leaveRequestRepository.save(leave);
    }

    // Get payroll history for the logged-in employee
    @GetMapping("/payroll")
    public List<Payroll> getPayrollHistory(@RequestParam String empId) {
        return payrollRepository.findByEmpId(empId);
    }

    // Get personal profile for the logged-in employee
    @GetMapping("/profile")
    public Employee getProfile(@RequestParam String empId) {
        return employeeRepository.findByEmpId(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // Update personal profile (for Employee)
    @PutMapping("/profile")
    public Employee updateProfile(@RequestParam String empId, @RequestBody Employee updatedProfile) {
        Employee emp = employeeRepository.findByEmpId(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        emp.setName(updatedProfile.getName());
        emp.setEmail(updatedProfile.getEmail());
        emp.setMobile(updatedProfile.getMobile());
        return employeeRepository.save(emp);
    }

    // Get all leave requests for HR
    @GetMapping("/leave-requests")
    public List<LeaveRequest> getLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    // Update leave request status
    @PutMapping("/leave-request/{id}/decision")
    public LeaveRequest updateLeaveDecision(@PathVariable Long id, @RequestBody Map<String, String> decision) {
        LeaveRequest leave = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        leave.setStatus(decision.get("status"));
        return leaveRequestRepository.save(leave);
    }

    // Get attendance records
    @GetMapping("/attendance")
    public List<Attendance> getAttendanceRecords() {
        return attendanceRepository.findAll();
    }
}
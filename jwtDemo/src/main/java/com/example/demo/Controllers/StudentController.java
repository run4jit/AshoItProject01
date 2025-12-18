package com.example.demo.Controllers;

import com.example.demo.Entities.Student;
import com.example.demo.Services.JWTService;
import com.example.demo.Services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class StudentController {

    @Autowired
    private StudentService service;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;


    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = service.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        Student saved =  service.saveStudent(student);
        return  new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginStudent(@RequestBody Student std) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(std.getEmail(), std.getPwd());
        try {
            Authentication authentication = authManager.authenticate(authToken);
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(std.getEmail());
                String str = "Welcome back " +
                        std.getEmail() +
                        " Token: " +
                        token;
                return new ResponseEntity<>(str, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid login credential", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid login credential", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/adminUser")
    public ResponseEntity<String> getAdminUser() {
        return new ResponseEntity<>("Shiva", HttpStatus.OK);
    }
}

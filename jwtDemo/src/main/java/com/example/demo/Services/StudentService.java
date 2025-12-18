package com.example.demo.Services;

import com.example.demo.Configurations.AppSecurityConfig;
import com.example.demo.Entities.Student;
import com.example.demo.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class StudentService implements UserDetailsService {
    @Autowired
    private StudentRepository repository;
    @Autowired
    private PasswordEncoder pwdEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = repository.findByEmail(email);

        return new User(student.getEmail(), student.getPwd(), Collections.emptyList());
    }

    public Student saveStudent(Student std) {
        String pwd = pwdEncoder.encode(std.getPwd());
        std.setPwd(pwd);
        repository.save(std);
        return std;
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }
}

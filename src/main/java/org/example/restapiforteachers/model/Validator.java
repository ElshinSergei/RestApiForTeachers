package org.example.restapiforteachers.model;

import java.util.ArrayList;
import java.util.List;

public class Validator {

    public static List<String> validatorTeacher(TeacherDTO teacherDTO) {
        List<String> errors = new ArrayList<>();

        String firstName = teacherDTO.getFirstName();
        if(firstName == null || firstName.length()<2 || firstName.length()>50 || !firstName.matches("[a-zA-zа-я]+")) {
            errors.add("First name must be 2-50 characters and contain only letters");
        }

        String lastName = teacherDTO.getLastName();
        if(lastName == null || lastName.length()<2 || lastName.length()>50 || !lastName.matches("[a-zA-zа-я]+")) {
            errors.add("Last name must be 2-50 characters and contain only letters");
        }

        String subject = teacherDTO.getSubject();
        if(subject.isEmpty()) {
            errors.add("Subject is required");
        }

        Integer experience = teacherDTO.getExperience();
        if (experience < 0 || experience > 50 ) {
            errors.add("Experience must be between 0 and 50 years");
        }

        Double salary = teacherDTO.getSalary();
        if (salary <= 0 || salary > 100000 ) {
            errors.add("Salary must be between 0 and 100000");
        }

        String email = teacherDTO.getEmail();
        if (!email.matches(".+[@].+\\..+")) {
            errors.add("Invalid email format");
        }
        return  errors;
    }

    public static List<String> validatorUpdatePartial(TeacherDTO teacherDTO) {
        List<String> errors = new ArrayList<>();

        String firstName = teacherDTO.getFirstName();
        if(firstName != null) {
            if(firstName.length()<2 || firstName.length()>50 || !firstName.matches("[a-zA-zа-я]+")) {
                errors.add("First name must be 2-50 characters and contain only letters");
            }
        }

        String lastName = teacherDTO.getLastName();
        if(lastName != null) {
            if(lastName.length()<2 || lastName.length()>50 || !lastName.matches("[a-zA-zа-я]+")) {
                errors.add("Last name must be 2-50 characters and contain only letters");
            }
        }

        String subject = teacherDTO.getSubject();
        if(subject != null) {
            if(subject.isEmpty()) {
                errors.add("Subject is required");
            }
        }

        Integer experience = teacherDTO.getExperience();
        if(experience != null) {
            if (experience < 0 || experience > 50 ) {
                errors.add("Experience must be between 0 and 50 years");
            }
        }

        Double salary = teacherDTO.getSalary();
        if(salary != null) {
            if (salary <= 0 || salary > 100000 ) {
                errors.add("Salary must be between 0 and 100000");
            }
        }

        String email = teacherDTO.getEmail();
        if(email != null) {
            if (!email.matches(".+[@].+\\..+")) {
                errors.add("Invalid email format");
            }
        }
        return  errors;
    }

}

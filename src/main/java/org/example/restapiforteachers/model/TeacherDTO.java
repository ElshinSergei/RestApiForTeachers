package org.example.restapiforteachers.model;

public class TeacherDTO {
    private String firstName;
    private String lastName;
    private String subject;
    private Integer experience;
    private Double salary;
    private String email;
    private Boolean isActive;

    public TeacherDTO() {
    }

    public TeacherDTO(String firstName, String lastName, String subject, Integer experience, Double salary, String email, Boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.subject = subject;
        this.experience = experience;
        this.salary = salary;
        this.email = email;
        this.isActive = isActive;
    }

    // конструктор для конвертации в DTO
    public TeacherDTO(Teacher teacher) {
        this.firstName = teacher.getFirstName();
        this.lastName = teacher.getLastName();
        this.subject = teacher.getSubject();
        this.experience = teacher.getExperience();
        this.salary = teacher.getSalary();
        this.email = teacher.getEmail();
        this.isActive = teacher.getActive();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

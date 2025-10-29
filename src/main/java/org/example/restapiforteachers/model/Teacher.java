package org.example.restapiforteachers.model;

public class Teacher {
    private static int nextId = 1;
    private Integer id;
    private String firstName;
    private String lastName;
    private String subject;
    private Integer experience;
    private Double salary;
    private String email;
    private Boolean isActive;

    public Teacher(String firstName, String lastName, String subject, Integer experience, Double salary, String email, Boolean isActive) {
        this.id = nextId++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.subject = subject;
        this.experience = experience;
        this.salary = salary;
        this.email = email;
        this.isActive = isActive;
    }

    public Teacher(TeacherDTO teacherDTO) {
        this.id = nextId++;
        this.firstName = teacherDTO.getFirstName();
        this.lastName = teacherDTO.getLastName();
        this.subject = teacherDTO.getSubject();
        this.experience = teacherDTO.getExperience();
        this.salary = teacherDTO.getSalary();
        this.email = teacherDTO.getEmail();
        this.isActive = teacherDTO.getIsActive();
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

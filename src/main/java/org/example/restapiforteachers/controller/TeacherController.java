package org.example.restapiforteachers.controller;

import org.example.restapiforteachers.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/teacher")
public class TeacherController {

    List<Teacher> teachers = new ArrayList<>();

    @GetMapping("/all")
    public ResponseEntity<List<Teacher>> getAll() {
        if(teachers.isEmpty()) {
           return  ResponseEntity.status(204).body(teachers);
        }
        return ResponseEntity.status(200).body(teachers);
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody TeacherDTO teacherDTO) {

        List<String> errors = new ArrayList<>();     //сообщения с ошибками

        if(teachers.stream().anyMatch(t -> teacherDTO.getFirstName().equalsIgnoreCase(t.getFirstName())
                && teacherDTO.getLastName().equalsIgnoreCase(t.getLastName()))) {
            errors.add("Teacher with this name already exists");

            ErrorResponse errorResponse = new ErrorResponse(409, "Conflict", "Teacher with this name already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }

        errors = Validator.validatorTeacher(teacherDTO);

        if(!errors.isEmpty()) {
            ErrorResponse error = new ErrorResponse(400, "Bad Request", "Validation failed", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Teacher teacher = new Teacher(teacherDTO);
        teachers.add(teacher);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/teacher/add" )
                .body(teacher);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TeacherDTO> getById(@PathVariable Integer id) {
        if (id < 0) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
          Optional<Teacher> teacher = teachers.stream().filter(t -> id.equals(t.getId())).findFirst();
          if (teacher.isEmpty()) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
          }

          TeacherDTO teacherDTO = new TeacherDTO(teacher.get());
          return ResponseEntity.status(200).body(teacherDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Teacher>> getByName(
            @RequestParam(name="firstName", required = false) String firstName,
            @RequestParam(name="lastName", required = false) String lastName
    ) {
        if(firstName.isEmpty() && lastName.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(teachers);
        }
        List<Teacher> teacher = teachers.stream()
                .filter(t -> (firstName.isEmpty() || firstName.equalsIgnoreCase(t.getFirstName())))
                .filter(t -> (lastName.isEmpty() || lastName.equalsIgnoreCase(t.getLastName())))
                .collect(Collectors.toList());
        if (teacher.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
            return ResponseEntity.status(200).body(teacher);
    }

    @GetMapping("/subject/{subject}")
    public ResponseEntity<?> searchBySubject(@PathVariable String subject) {

            if (!subject.matches("[a-zA-Zа-яА-Я]+")) {
                ErrorResponse errorResponse = new ErrorResponse(400, "BAD REQUEST", "incorrect parameters");
                  return ResponseEntity.status(400).body(errorResponse);
            }
            List<Teacher> teacherSub = teachers.stream()
                .filter(t -> subject.equalsIgnoreCase(t.getSubject()))
                .toList();

            if (teacherSub.isEmpty()) {
                 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
                 return ResponseEntity.status(200).body(teacherSub);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterByExpSalary(
            @RequestParam(name = "minExp") Integer minExp,
            @RequestParam(name = "maxExp") Integer maxExp,
            @RequestParam(name = "minSalary") Double minSalary,
            @RequestParam(name = "maxSalary") Double maxSalary
    ) {
        if(minExp > maxExp || minSalary > maxSalary) {
            ErrorResponse errorResponse = new ErrorResponse(400, "BAD REQUEST", "the minimum is greater than the maximum");
            return ResponseEntity.status(400).body(errorResponse);
        }

        List<Teacher> teacherFilter = teachers.stream().filter(t -> (t.getExperience() >= minExp && t.getExperience() <= maxExp)
                && (t.getSalary() >= minSalary) && t.getSalary() <=maxSalary).toList();

        if(teacherFilter.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
            return  ResponseEntity.status(200).body(teacherFilter);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Teacher>> isActive() {
         List<Teacher> active = teachers.stream().filter(t -> t.getActive() == true).toList();

         if (active.isEmpty()) {
            return ResponseEntity.status(204).build();
         }
         return ResponseEntity.status(200).body(active);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getQuantityTeacher() {
        Integer quantity = teachers.size();
        return ResponseEntity.status(200).body(quantity);
    }

    @GetMapping("/count-by-subject")
    public ResponseEntity<Map<String, Long>> getCountBySubject() {
        Map<String, Long> count = teachers.stream().collect(Collectors.groupingBy(Teacher::getSubject, Collectors.counting()));
        if(count.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
            return ResponseEntity.status(200).body(count);
    }

    @PostMapping("/add-bulk")
    public ResponseEntity<?> add_bulk(@RequestBody List<TeacherDTO> teachersDTO) {
        List<List<String>> errors = new ArrayList<>();
        int added = 0; // количество добавленных
        int failed = 0; // колиество невалидных
        for (TeacherDTO teacherDTO : teachersDTO){
            List<String> error = (Validator.validatorTeacher(teacherDTO));
            if(!error.isEmpty()) {
                error.add(teacherDTO.getFirstName() + " " + teacherDTO.getLastName());
                errors.add(error);
                failed++;
            }
            if(error.isEmpty()) {
                if (teachers.stream().anyMatch(t -> teacherDTO.getFirstName().equalsIgnoreCase(t.getFirstName())
                        && teacherDTO.getLastName().equalsIgnoreCase(t.getLastName()))) {
                        error.add("Teacher with this name already exists");
                        error.add(teacherDTO.getFirstName() + " " + teacherDTO.getLastName());
                        errors.add(error);
                        failed++;
                } else {
                    Teacher teacher = new Teacher(teacherDTO);
                    teachers.add(teacher);
                    added++;
                }
            }
        }
        BulkAddResponse bulkAddResponse = new BulkAddResponse(added, failed, errors);
        if(failed == 0 && added > 0) {
            return ResponseEntity.status(201).body(bulkAddResponse);
        } else if (failed > 0 && added > 0) {
            return ResponseEntity.status(207).body(bulkAddResponse);
        } else {
            return ResponseEntity.status(400).body(bulkAddResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody TeacherDTO teacherDTO) {

        if(!teachers.stream().anyMatch(t -> id.equals(t.getId()))) {
            ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", "Teacher with id " + id + " not found");
            return ResponseEntity.status(404).body(errorResponse);
        }

        List<String> errors = Validator.validatorTeacher(teacherDTO);

        if(!errors.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(400, "BAD REQUEST", "Validation failed", errors);
            return ResponseEntity.status(400).body(errorResponse);
        }

        if (teachers.stream().anyMatch(t -> teacherDTO.getFirstName().equalsIgnoreCase(t.getFirstName())
                && teacherDTO.getLastName().equalsIgnoreCase(t.getLastName()))) {
            errors.add("Teacher with this name already exists");
            ErrorResponse errorResponse = new ErrorResponse(409, "CONFLICT", "Teacher with this name already exists", errors);
            return ResponseEntity.status(409).body(errorResponse);
        }

        List<Teacher> teacher1 = new ArrayList<>();
        for (Teacher teacher : teachers) {
            if(teacher.getId().equals(id)) {
                teacher.setFirstName(teacherDTO.getFirstName());
                teacher.setLastName(teacherDTO.getLastName());
                teacher.setSubject(teacherDTO.getSubject());
                teacher.setExperience(teacherDTO.getExperience());
                teacher.setSalary(teacherDTO.getSalary());
                teacher.setEmail(teacherDTO.getEmail());
                teacher.setActive(teacherDTO.getIsActive());
                teacher1.add(teacher);
                break;
            }
        }
        return ResponseEntity.status(200).body(teacher1);
    }

    @PatchMapping("/update-partial/{id}")
    public ResponseEntity<?> updatePartial(@PathVariable Integer id, @RequestBody TeacherDTO teacherDTO) {

        if(!teachers.stream().anyMatch(t -> id.equals(t.getId()))) {
            ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", "Teacher with id " + id + " not found");
            return ResponseEntity.status(404).body(errorResponse);
        }

        List<String> errors = Validator.validatorUpdatePartial(teacherDTO);

        if(!errors.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(400, "BAD REQUEST", "Validation failed", errors);
            return ResponseEntity.status(400).body(errorResponse);
        }

        if(teacherDTO.getFirstName() != null && teacherDTO.getLastName() != null) {
            List<Teacher> teacherList = teachers.stream().filter(t -> teacherDTO.getFirstName().equalsIgnoreCase(t.getFirstName())
                    && teacherDTO.getLastName().equalsIgnoreCase(t.getLastName())).toList();

            if (teacherList.size() > 1) {
                errors.add("Teacher with this name already exists");
                ErrorResponse errorResponse = new ErrorResponse(409, "CONFLICT", "Teacher with this name already exists", errors);
                return ResponseEntity.status(409).body(errorResponse);
            }
        }

        List<Teacher> teacher1 = new ArrayList<>();
        for (Teacher teacher : teachers) {
            if(teacher.getId().equals(id)) {
                if(teacherDTO.getFirstName() != null) {
                    teacher.setFirstName(teacherDTO.getFirstName());
                }
                if (teacherDTO.getLastName() != null) {
                    teacher.setLastName(teacherDTO.getLastName());
                }
                if (teacherDTO.getSubject() != null) {
                    teacher.setSubject(teacherDTO.getSubject());
                }
                if (teacherDTO.getExperience() != null) {
                    teacher.setExperience(teacherDTO.getExperience());
                }
                if (teacherDTO.getSalary() != null) {
                    teacher.setSalary(teacherDTO.getSalary());
                }
                if (teacherDTO.getEmail() != null) {
                    teacher.setEmail(teacherDTO.getEmail());
                }
                if (teacherDTO.getIsActive() != null) {
                    teacher.setActive(teacherDTO.getIsActive());
                }
                teacher1.add(teacher);
            }
        }
        return ResponseEntity.status(200).body(teacher1);
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivate(@PathVariable Integer id) {
        if(!teachers.stream().anyMatch(t -> id.equals(t.getId()))) {
            ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", "Teacher with id " + id + " not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
        List<Teacher> teacher1 = new ArrayList<>();
        for(Teacher teacher : teachers) {
            if(teacher.getId().equals(id)) {
                if(teacher.getActive() == false) {
                    ErrorResponse errorResponse = new ErrorResponse(400, "BAD REQUEST", "The teacher has already been deactivated");
                    return ResponseEntity.status(400).body(errorResponse);
                }
                teacher.setActive(false);
                teacher1.add(teacher);
                break;
            }
        }
        return ResponseEntity.status(200).body(teacher1);
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<?> activate(@PathVariable Integer id) {
        if(!teachers.stream().anyMatch(t -> id.equals(t.getId()))) {
            ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", "Teacher with id " + id + " not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
        List<Teacher> teacher1 = new ArrayList<>();
        for(Teacher teacher : teachers) {
            if(teacher.getId().equals(id)) {
                if(teacher.getActive() == true) {
                    ErrorResponse errorResponse = new ErrorResponse(400, "BAD REQUEST", "The teacher has already been activated");
                    return ResponseEntity.status(400).body(errorResponse);
                }
                teacher.setActive(true);
                teacher1.add(teacher);
                break;
            }
        }
        return ResponseEntity.status(200).body(teacher1);
    }

    @PatchMapping("/increase-salary/{id}")
    public ResponseEntity<?> increasSalary(@PathVariable Integer id, @RequestParam(name="percent") Double percent) {

        if(!teachers.stream().anyMatch(t -> id.equals(t.getId()))) {
            ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", "Teacher with id " + id + " not found");
            return ResponseEntity.status(404).body(errorResponse);
        }
        if(percent < 0 || percent > 100) {
            ErrorResponse errorResponse = new ErrorResponse(400, "BAD REQUEST", "incorrect percentage");
            return ResponseEntity.status(400).body(errorResponse);
        }
        List<Teacher> teacher1 = new ArrayList<>();
        for(Teacher teacher : teachers) {
            if(teacher.getId().equals(id)) {
                Double newSalary = (teacher.getSalary() / 100 * percent) + teacher.getSalary();
                if( newSalary > 100000) {
                    ErrorResponse errorResponse = new ErrorResponse(400, "BAD REQUEST", "exceeding the salary limit");
                    return ResponseEntity.status(400).body(errorResponse);
                }
                teacher.setSalary(newSalary);
                teacher1.add(teacher);
                break;
            }
        }
        return ResponseEntity.status(200).body(teacher1);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {

        if(!teachers.stream().anyMatch(t -> id.equals(t.getId()))) {
            ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", "Teacher with id " + id + " not found");
            return ResponseEntity.status(404).body(errorResponse);
        }

        for(Teacher teacher : teachers) {
            if(teacher.getId().equals(id)) {
                teachers.remove(teacher);
                break;
            }
        }
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/delete-by-subject/{subject}")
    public ResponseEntity<?> deleteBySubject(@PathVariable String subject) {

        if(!teachers.stream().anyMatch(t -> subject.equals(t.getSubject()))) {
            ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", "there are no teachers with such a subject");
            return ResponseEntity.status(404).body(errorResponse);
        }

        int count = 0;
        List<Teacher> remove = new ArrayList<>();
           for(Teacher teacher : teachers) {
            if(teacher.getSubject().equalsIgnoreCase(subject)) {
                remove.add(teacher);
                count++;
            }
        }
        teachers.removeAll(remove);
        return ResponseEntity.status(200).body("delete: " + count);
    }

    @DeleteMapping("/delete-inactive")
    public ResponseEntity<?> deleteInactive() {
        int count = 0;
        List<Teacher> inactive = new ArrayList<>();
        for(Teacher teacher : teachers) {
            if(teacher.getActive() == false) {
                inactive.add(teacher);
                count++;
            }
        }
        if(inactive.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        teachers.removeAll(inactive);
        return ResponseEntity.status(200).body("delete: " + count);
    }

}

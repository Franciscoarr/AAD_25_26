package com.farrnav3006.aad.application;

import com.farrnav3006.aad.model.Module;
import com.farrnav3006.aad.model.Student;
import com.farrnav3006.aad.repository.ModuleRepository;
import com.farrnav3006.aad.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor //Genera un constructor con argumentos requeridos (final fields)
public class StudentService implements CustomService<Student> {

    private final StudentRepository studentRepository;
    private final ModuleRepository moduleRepository;

//    public StudentService(Student student) {
//        this.student = student;
//    }

    /**
     * @param entity
     * @return
     */
    @Override
    public boolean validate(Student entity) {
        return !entity.getDni().isBlank() && !entity.getName().isBlank();
    }

    public Student createStudent(final Student student, List<Module> modules) {
        if (validate(student)) {
            student.setModules(modules);
            return studentRepository.create(student);
        }
        return null;
    }


}

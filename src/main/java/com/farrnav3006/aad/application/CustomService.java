package com.farrnav3006.aad.application;

import com.farrnav3006.aad.model.Enrollment;
import com.farrnav3006.aad.model.Student;
import com.farrnav3006.aad.model.Module;

public interface CustomService {
    Module createModule(Module module);
    Student createStudent(Student student);
    Enrollment enrollStudentInModule(Integer studentId, Integer moduleId);
}

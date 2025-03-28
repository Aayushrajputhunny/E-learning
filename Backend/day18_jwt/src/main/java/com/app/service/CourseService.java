package com.app.service;

import java.util.List;

import com.app.dto.CountPerCourseNameDto;
import com.app.entities.Courses;

public interface CourseService {

	List<Courses> getAllCourses();

	List<CountPerCourseNameDto> countPerCname();

}

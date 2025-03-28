package com.app.controller;

import static org.springframework.http.MediaType.IMAGE_GIF_VALUE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.ApiResponse;
import com.app.dto.ContentDescDto;
import com.app.dto.ContentGetDto;
import com.app.dto.CoursesDto;
import com.app.dto.EditCourseDto;
import com.app.dto.StudentDetailDTO;
import com.app.dto.TeacherDetailResponseDto;
import com.app.entities.Content;
import com.app.entities.Courses;
import com.app.entities.Teachers;
import com.app.service.ContentService;
import com.app.service.TeacherService;

@RestController
@RequestMapping("/teacher")
@CrossOrigin("*")
@Validated
public class TeacherController {

	@Autowired
	private TeacherService teacher;
	
	@Autowired
	private ContentService content;
	
	
	
	
	@GetMapping (value = "/{teacherID}")
	public ResponseEntity<?> studentDetails(@PathVariable Long teacherID){
		System.out.println("in get Teacher"+teacherID);
		return ResponseEntity.ok(teacher.getDetailsByID(teacherID));
	}
	
	@PutMapping
	public ResponseEntity<?> updateDetails(@RequestBody TeacherDetailResponseDto teach){
		System.out.println("in update Teacher details"+ teach);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(teacher.editDetails(teach));
	}
	
	@DeleteMapping("/{teacherID}")
	public ResponseEntity<?> deleteTeacher(@PathVariable Long teacherID){
		System.out.println("to delete Teacher "+teacherID);
		try {
			return ResponseEntity.ok(teacher.deleteByID(teacherID));
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse("Invalid action, might be enrolled"));
		}
	}
	
	@PostMapping("/course/{teacherId}")
	public ResponseEntity<?> addCourse(@PathVariable Long teacherId,@RequestBody CoursesDto course){
		
		System.out.println("in add course"+course);
		return ResponseEntity.status(HttpStatus.CREATED).body(teacher.newCourseByTID(teacherId,course));
	}
	
	
	@GetMapping("/course/{teacherId}")
	public ResponseEntity<?> getCourses(@PathVariable @NotNull Long teacherId)
	{
		System.out.println("in get courser" + teacherId);
		return ResponseEntity.ok(teacher.getCoursesById(teacherId));
	}
	
	@DeleteMapping("/course/{CourseId}")
	public ResponseEntity<?> deleteCourse(@PathVariable @NotNull Long CourseId)
	{
		System.out.println("in delete Courese " + CourseId);
		try {
			return ResponseEntity.ok(teacher.deleteCourseById(CourseId));

		} catch (Exception e) {
			return ResponseEntity.internalServerError().body(new ApiResponse("Invalid action, might be enrolled"));
		}
	}
	
	@PostMapping("/course/content/{CourseId}")
	public ResponseEntity<?> addContents(@PathVariable @NotNull Long CourseId,@RequestBody ContentDescDto content)
	{
		System.out.println("in add course"+content + CourseId);
		return ResponseEntity.status(HttpStatus.CREATED).body(this.content.newContentByCId(CourseId,content));
	}
	
	@PostMapping(value = "/course/content/video/{contentId}",consumes = "multipart/form-data")
	public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("image")MultipartFile file,@PathVariable Long contentId) throws IOException, java.io.IOException {
		ApiResponse uploadImage = content.uploadImageToFileSystem(file,contentId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(uploadImage);
	}
	
	@GetMapping("/course/content/{contentId}")  
	public ResponseEntity<ContentGetDto> coureseContentById(@PathVariable @NotNull Long contentId)
	{
		System.out.println("in Get Content"+ contentId);
		return ResponseEntity.ok(content.contentBYId(contentId));
	}
	
	@GetMapping("/course_content/{CourseId}")  // getting all contents assosiated with a course 
	public ResponseEntity<?> getContentBYContentId(@PathVariable @NotNull Long CourseId)
	{
		System.out.println("in get content by course id" + CourseId);
		return ResponseEntity.ok(content.getCoursesById(CourseId));
	}

	@GetMapping("/enroll/{teacherId}")
	public ResponseEntity<?> getStudentsForTeachers(@PathVariable @NotNull Long teacherId)
	{
		System.out.println("in get students by TeacherID ="+teacherId);
		return ResponseEntity.ok(teacher.studentsForTeachers(teacherId));
	}
	
	@DeleteMapping("/course/content/{contentId}")  // deleting the contentId
	public ResponseEntity<?> deleteContentById(@PathVariable @NotNull Long contentId)
	{
		System.out.println("in get delete by course id" + contentId);
		return ResponseEntity.ok(content.deleteByCourseId(contentId));
	}
	
	@PostMapping(value="/images/{teacherId}",consumes = "multipart/form-data")
	public ResponseEntity<?> uploadImage(@PathVariable Long teacherId, @RequestParam MultipartFile imageFile)
			throws IOException, java.io.IOException {
		System.out.println("in upload img " + teacherId);
		return ResponseEntity.status(HttpStatus.CREATED).body(teacher.uploadImage(teacherId, imageFile));
	}
	
	@GetMapping(value = "/images/{teacherId}", produces = { IMAGE_GIF_VALUE, IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE })
	public ResponseEntity<?> serveTeacherImage(@PathVariable Long teacherId) throws IOException, java.io.IOException {
		System.out.println("in download img " + teacherId);
		return ResponseEntity.ok(teacher.downloadImage(teacherId));
	}
	
	@PutMapping("/course/{teacherId}") // to update course title and desc for course associated by teacher
	public ResponseEntity<?> editCourse(@PathVariable @NotNull Long teacherId, @RequestBody @Valid EditCourseDto course)
	{
		System.out.println("to edit detail of courseId =" + course.getId()+" of teacherId ="+ teacherId);
		return ResponseEntity.ok(teacher.editContentByTID(teacherId,course));
	}
	
//	@PutMapping("/course/content/{contentId}")
//	public ResponseEntity<?> editCourseContent(@PathVariable @NotNull Long teacherId, @RequestBody @Valid EditCourseDto course)
//	{
//		System.out.println("to edit detail of courseId =" + course.getId()+" of teacherId ="+ teacherId);
//		return ResponseEntity.ok(teacher.editContentByTID(teacherId,course));
//	}
//	
	
	@GetMapping("/Course/name/{CourseId}")
	public ResponseEntity<?> courseName(@PathVariable @NotNull Long CourseId)
	{
		System.out.println("in get course name by course id" + CourseId);
		return ResponseEntity.ok(content.courseNameById(CourseId));	
	}
	
	@GetMapping("/data/{teacherId}")
	public ResponseEntity<?> getCountPerCourse(@PathVariable @NotNull Long teacherId)
	{
		System.out.println(" to get analysied data of tid = "+teacherId);
		return ResponseEntity.ok(teacher.getCountForCId(teacherId));
	}
	
}

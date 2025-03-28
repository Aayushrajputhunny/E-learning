package com.app.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.JoinColumn;
//import javax.persistence.Lob;
//import javax.persistence.MapsId;
//import javax.persistence.OneToOne;
//import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "students")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Students extends BaseEntity{
	
	@Column(length = 50)
	private String name;
	
	private Integer age;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private Gender gender;
	
	@Column(length = 15)
	private String phoneNo;
	
	@Column(length = 200)
	private String address;
	
	@Lob
	private byte[] profilePic;
	
	private LocalDate joiningDate = LocalDate.now();
	
	@OneToOne
	@JoinColumn(name = "student_id",nullable = false)
	@MapsId
	private UserEntity sId;
	
	@OneToMany(mappedBy = "sid",cascade = CascadeType.ALL)
	private List<Enrollment> enrollmentNo;
	
	 // helper method : to add Enrollment
 	public void addCourse(Enrollment e) {		
 		this.enrollmentNo.add(e);// can navigate from parent --> child
 		e.setSid(this);// can navigate from child --> parent
 	}

 	// helper method : to remove Enrollment
 	public void removeCourse(Enrollment e) {
 		this.enrollmentNo.remove(e);
 		e.setSid(null);
 	}
	
}

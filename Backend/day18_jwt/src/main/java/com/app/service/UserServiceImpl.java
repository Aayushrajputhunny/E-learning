package com.app.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.custom_exceptions.ResourceNotFoundException;
import com.app.dao.StudentDao;
import com.app.dao.TeacherDao;
import com.app.dao.UserEntityDao;
import com.app.dto.Signup;
import com.app.dto.StudentSignUp;
import com.app.dto.TeacherSignUp;
import com.app.dto.UserDetailDto;
import com.app.entities.Students;
import com.app.entities.Teachers;
import com.app.entities.UserEntity;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	//dep : dao layer i/f
	@Autowired
	private UserEntityDao userDao;
	//dep
	@Autowired
	private ModelMapper mapper;
	//dep 
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private TeacherDao teacherDao;

	@Override
	public StudentSignUp studentRegistration(StudentSignUp reqDTO) {
		//dto --> entity
		Students student=mapper.map(reqDTO, Students.class);
//		Students student = new Students(reqDTO.getName(), reqDTO.getAge(), reqDTO.getGender(), reqDTO.getPhoneNo(), reqDTO.getAddress(), null, null, null);
		UserEntity user = mapper.map(reqDTO, UserEntity.class);
		
		user.setPassword(encoder.encode(user.getPassword()));//pwd : encrypted using SHA
		StudentSignUp reg = mapper.map(userDao.save(user), StudentSignUp.class) ;
		student.setSId(user);
		reg = mapper.map(studentDao.save(student), StudentSignUp.class) ;
		return reg;
	}

	@Override
	public TeacherSignUp teacherRegistration(TeacherSignUp reqDTO) {
		Teachers teacher = mapper.map(reqDTO, Teachers.class);
		UserEntity user = mapper.map(reqDTO, UserEntity.class);
		
		user.setPassword(encoder.encode(user.getPassword()));  //pwd : encrypted using SHA
		TeacherSignUp reg = mapper.map(userDao.save(user), TeacherSignUp.class) ;
		teacher.setTId(user);
		reg = mapper.map(teacherDao.save(teacher), TeacherSignUp.class) ;
		
		return reg;
	}

	@Override
	public UserDetailDto getDetialByEmail(String email) {
		// TODO Auto-generated method stub
		UserEntity u =  userDao.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("Invalid user"));
	
		Long id = u.getId();
		
		if (u.getRole().name()=="STUDENT") {
			Students s= studentDao.getById(id);
			UserDetailDto res = mapper.map(s, UserDetailDto.class);
			res.setEmail(u.getEmail());
			res.setRole(u.getRole());
			return res;
		}
		else if (u.getRole().name()=="TEACHER") {
			Teachers t = teacherDao.getById(id);
			UserDetailDto res = mapper.map(t, UserDetailDto.class);
			res.setEmail(u.getEmail());
			res.setRole(u.getRole());
			return res;
		}
		return null;
	}

}

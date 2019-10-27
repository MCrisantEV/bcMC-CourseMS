package mc.bc.ms.courses.app.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import mc.bc.ms.courses.app.repositories.CourseRepository;
import mc.bc.ms.courses.app.services.CourseService;

@Service
public class CourseImpl implements CourseService{
	
	@Autowired
	private CourseRepository intRep;

	@Autowired
	private Validator validator;

}

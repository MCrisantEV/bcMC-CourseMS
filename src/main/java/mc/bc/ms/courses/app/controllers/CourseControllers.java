package mc.bc.ms.courses.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mc.bc.ms.courses.app.services.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseControllers {
	@Autowired
	private CourseService couServ;
}

package mc.bc.ms.courses.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mc.bc.ms.courses.app.models.Course;
import mc.bc.ms.courses.app.services.CourseService;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/courses")
public class CourseControllers {
	@Autowired
	private CourseService couServ;
	
	@PostMapping
	public Mono<Map<String, Object>> createInstitute(@RequestBody Course course) {

		return couServ.saveCourse(course);
	}
}

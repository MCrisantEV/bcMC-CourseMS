package mc.bc.ms.courses.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mc.bc.ms.courses.app.models.Course;
import mc.bc.ms.courses.app.services.CourseService;
import reactor.core.publisher.Flux;
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
	
	@GetMapping
	public Flux<Course> listCourses(){
		return couServ.findAll();
	}
	
	@GetMapping("/{id}")
	public Mono<Course> findIdCourse(@PathVariable String id){
		return couServ.findById(id);
	}
	
	@PutMapping("/{id}")
	public Mono<Map<String, Object>> editCourse(@PathVariable String id, @RequestBody Course course){
		return couServ.updateCourse(id, course);
	}
	
	@DeleteMapping("/{id}")
	public Mono<Map<String, Object>> removeInstitute(@PathVariable String id) {
		return couServ.deleteCourses(id);
	}
}

package mc.bc.ms.courses.app.services;

import java.util.Map;

import mc.bc.ms.courses.app.models.Course;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseService {
	
	public Mono<Map<String, Object>> saveCourse(Course course); 
	
	public Flux<Course> findAll();
	
	public Mono<Course> findById(String id);

}

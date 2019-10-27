package mc.bc.ms.courses.app.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import mc.bc.ms.courses.app.models.Course;
import reactor.core.publisher.Flux;

public interface CourseRepository extends ReactiveMongoRepository<Course, String> {

	public Flux<Course> findByTeacherAndState(String teacher, String state);
	
}

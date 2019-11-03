package mc.bc.ms.courses.app.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import mc.bc.ms.courses.app.models.Course;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseRepository extends ReactiveMongoRepository<Course, String> {

	public Flux<Course> findByTeacherAndInstituteAndState(String teacher, String intitute, String state);
	
	public Flux<Course> findByInstitute(String institute);
	
	public Flux<Course> findByTeacherAndInstitute(String teacher, String institute);
	
	public Mono<Course> findByNameAndInstitute(String name, String institute);
	
}

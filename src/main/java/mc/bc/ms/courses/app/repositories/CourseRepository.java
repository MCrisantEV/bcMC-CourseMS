package mc.bc.ms.courses.app.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import mc.bc.ms.courses.app.models.Course;

public interface CourseRepository extends ReactiveMongoRepository<Course, String>{

}

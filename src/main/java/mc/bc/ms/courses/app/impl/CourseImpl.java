package mc.bc.ms.courses.app.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import mc.bc.ms.courses.app.models.Course;
import mc.bc.ms.courses.app.repositories.CourseRepository;
import mc.bc.ms.courses.app.services.CourseService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseImpl implements CourseService {

	@Autowired
	private CourseRepository couRep;

	@Autowired
	private Validator validator;

	@Override
	public Mono<Map<String, Object>> saveCourse(Course course) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		if (errors(course) == null) {
			return couRep.findByTeacherAndInstituteAndState(course.getTeacher(), course.getInstitute(), "Active")
					.collectList().flatMap(listCour -> {
						if (listCour.size() >= 2) {
							respuesta.put("Error: ", "El profesor, tiene 2 o más cursos activos");
							return Mono.just(respuesta);
						} else {
							return couRep.findByNameAndInstitute(course.getName(), course.getInstitute()).map(c -> {
								respuesta.put("Error", "El Curso " + c.getName() + ", ya existe");
								return respuesta;
							}).switchIfEmpty(Mono.just(course).map(m -> {
								couRep.save(course).subscribe();
								respuesta.put("Mensaje", "Curso " + course.getName() + ", creado con éxito");
								return respuesta;
							}));
						}
					});
		} else {
			return errors(course);
		}
	}

	@Override
	public Flux<Course> findAllCourse() {
		return couRep.findAll();
	}

	@Override
	public Mono<Course> findIdCourse(String id) {
		return couRep.findById(id);
	}

	@Override
	public Mono<Map<String, Object>> updateCourse(String id, Course course) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		course.setName(id);
		course.setInstitute(id);
		if (errors(course) == null) {
			return couRep.findById(id).flatMap(dbc -> {
				course.setInstitute(dbc.getInstitute());
				course.setName(dbc.getName());
				course.setId(id);
				if (dbc.getTeacher().equals(course.getTeacher())) {
					couRep.save(course).subscribe();
					respuesta.put("Mensaje", "El curso se actualizo con éxito");
					return Mono.just(respuesta);
				} else {
					return couRep
							.findByTeacherAndInstituteAndState(course.getTeacher(), course.getInstitute(), "Active")
							.collectList().map(listCour -> {
								if (listCour.size() >= 2) {
									respuesta.put("Error: ", "El profesor, tiene 2 o más cursos activos");
									return respuesta;
								} else {
									couRep.save(course).subscribe();
									respuesta.put("Mensaje", "Curso " + course.getName() + ", se actualizo con éxito");
									return respuesta;
								}
							});
				}

			}).switchIfEmpty(Mono.just("").map(er -> {
				respuesta.put("Error", "El curso no esta registrado");
				return respuesta;
			}));
		} else {
			return errors(course);
		}
	}

	@Override
	public Mono<Map<String, Object>> deleteCourses(String id) {
		Map<String, Object> respuesta = new HashMap<String, Object>();

		return couRep.findById(id).map(courDb -> {
			couRep.delete(courDb).subscribe();
			respuesta.put("Mensaje: ", courDb.getName() + " se eliminó con éxito");
			return respuesta;
		}).switchIfEmpty(Mono.just("").map(er -> {
			respuesta.put("Mensaje: ", "El Curso no se pudo elimininar");
			respuesta.put("Status", HttpStatus.BAD_REQUEST.value());
			respuesta.put("Error", "Problemas con ID");
			return respuesta;
		}));

	}

	@Override
	public Flux<Course> findAllInstituteCourse(String institute) {
		return couRep.findByInstitute(institute);
	}

	@Override
	public Flux<Course> findTeacher(String teacher, String institute) {
		return couRep.findByTeacherAndInstitute(teacher, institute);
	}

	private Mono<Map<String, Object>> errors(Course course) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		Errors errors = new BeanPropertyBindingResult(course, Course.class.getName());
		validator.validate(course, errors);

		respuesta.put("status", HttpStatus.BAD_REQUEST.value());
		respuesta.put("Mensaje", "Error, revise los datos");

		if (errors.hasErrors()) {
			return Flux.fromIterable(errors.getFieldErrors()).map(err -> {
				String[] matriz = { err.getField(), err.getDefaultMessage() };
				return matriz;
			}).collectList().flatMap(l -> {
				l.forEach(m -> {
					for (int i = 0; i < m.length; i++) {
						respuesta.put(m[0], m[i]);
					}
				});
				return Mono.just(respuesta);
			});
		} else if (course.getMin() == 0 || course.getMin() == 0) {
			if (course.getMin() == 0) {
				respuesta.put("min: ", "no puede estar vacío");
			}
			if (course.getMax() == 0) {
				respuesta.put("max: ", "no puede estar vacío");
			}
			return Mono.just(respuesta);
		} else if (course.getMax() < course.getMin()) {
			respuesta.put("Error: ", "La cupo máximo no puede ser menor que cupo mínimo");
			return Mono.just(respuesta);
		}

		return null;
	}
}

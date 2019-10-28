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

		return Mono.just(course).flatMap(c -> {

			Errors errors = new BeanPropertyBindingResult(c, Course.class.getName());
			validator.validate(c, errors);

			if (errors.hasErrors()) {
				return Flux.fromIterable(errors.getFieldErrors()).map(err -> {
					String[] matriz = { err.getField(), err.getDefaultMessage() };
					return matriz;
				}).collectList().flatMap(l -> {
					respuesta.put("status", HttpStatus.BAD_REQUEST.value());
					respuesta.put("Mensaje", "Error, revise los datos");
					l.forEach(m -> {
						for (int i = 0; i < m.length; i++) {respuesta.put(m[0], m[i]);}
					});
					return Mono.just(respuesta);
				});
			} else if (c.getMin() == 0 || c.getMin() == 0) {
				respuesta.put("status", HttpStatus.BAD_REQUEST.value());
				respuesta.put("Mensaje", "Error, revise los datos");
				if (c.getMin() == 0) { respuesta.put("min: ", "no puede estar vacío"); }
				if (c.getMax() == 0) { respuesta.put("max: ", "no puede estar vacío"); }
				return Mono.just(respuesta);
			} else if (c.getMax() < c.getMin()) {
				respuesta.put("status", HttpStatus.BAD_REQUEST.value());
				respuesta.put("Mensaje", "Error, revise los datos");
				respuesta.put("Error: ", "La cupo máximo no puede ser menor que cupo mínimo");
				return Mono.just(respuesta);
			} else {
				return couRep.findByTeacherAndState(c.getTeacher(), "Active").collectList().map(listCour -> {
//					if(listCour.size() >= 2 && c.getState().equals("Active")) {
					if(listCour.size() >= 2) {
						respuesta.put("Error: ", c.getTeacher() + " tiene mas de 2 cursos activos");
					}else {
						couRep.save(course).subscribe();
						respuesta.put("Curso", c.getName());
						respuesta.put("Mensaje", "Curso creado con éxito");
					}
					return respuesta;
				});
			}
		});
	}

	@Override
	public Flux<Course> findAll() {
		return couRep.findAll();
	}

	@Override
	public Mono<Course> findById(String id) {
		return couRep.findById(id);
	}

	@Override
	public Mono<Map<String, Object>> updateCourse(String id, Course course) {
		Map<String, Object> respuesta = new HashMap<String, Object>();

		return Mono.just(course).flatMap(c -> {

			Errors errors = new BeanPropertyBindingResult(c, Course.class.getName());
			validator.validate(c, errors);

			if (errors.hasErrors()) {
				return Flux.fromIterable(errors.getFieldErrors()).map(err -> {
					String[] matriz = { err.getField(), err.getDefaultMessage() };
					return matriz;
				}).collectList().flatMap(l -> {
					respuesta.put("status", HttpStatus.BAD_REQUEST.value());
					respuesta.put("Mensaje", "Error, revise los datos");
					l.forEach(m -> {
						for (int i = 0; i < m.length; i++) {respuesta.put(m[0], m[i]);}
					});
					return Mono.just(respuesta);
				});
			} else if (c.getMin() == 0 || c.getMin() == 0) {
				respuesta.put("status", HttpStatus.BAD_REQUEST.value());
				respuesta.put("Mensaje", "Error, revise los datos");
				if (c.getMin() == 0) { respuesta.put("min: ", "no puede estar vacío"); }
				if (c.getMax() == 0) { respuesta.put("max: ", "no puede estar vacío"); }
				return Mono.just(respuesta);
			} else if (c.getMax() < c.getMin()) {
				respuesta.put("status", HttpStatus.BAD_REQUEST.value());
				respuesta.put("Mensaje", "Error, revise los datos");
				respuesta.put("Error: ", "La cupo máximo no puede ser menor que cupo mínimo");
				return Mono.just(respuesta);
			} else {
				return couRep.findByTeacherAndState(c.getTeacher(), "Active").collectList().flatMap(listCour -> {
//					if(listCour.size() >= 2 && c.getState().equals("Active")) {
					if(listCour.size() >= 2) {
						respuesta.put("Error: ", c.getTeacher() + " tiene mas de 2 cursos activos");
						return Mono.just(respuesta);
					}else {
						return couRep.findById(id).map(coutDb -> {
							c.setId(id);
							respuesta.put("Curso", c.getName());
							respuesta.put("Mensaje", "Curso se actualizo con éxito");
							couRep.save(c).subscribe();
							return respuesta; 
						}).switchIfEmpty(Mono.just(course).map(er -> {
							respuesta.put("Error: ", er.getName() + " No se puede actualizar");
							return respuesta;
						}));
					}
				});
			}
		});
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

}

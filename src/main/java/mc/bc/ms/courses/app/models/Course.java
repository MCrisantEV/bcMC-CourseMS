package mc.bc.ms.courses.app.models;

import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "courses")
public class Course {
	@Id
	private String id;

	@NotBlank
	private String intitute;
	@NotBlank
	private String name;
	@NotBlank
	private String teacher;

	private int min;
	private int max;

	@NotBlank
	private String state;

}


package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;


	@Autowired
	public CourseService(final CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@Transactional
	public Iterable<Course> findAll() {
		return this.courseRepository.findAll();
	}

	public Optional<Course> findCourseById(final int courseId) {
		return this.courseRepository.findById(courseId);
	}

}

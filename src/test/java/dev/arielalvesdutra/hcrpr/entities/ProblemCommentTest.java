package dev.arielalvesdutra.hcrpr.entities;


import static org.assertj.core.api.Assertions.assertThat;

import java.security.InvalidParameterException;
import java.time.OffsetDateTime;

import javax.persistence.ManyToOne;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ProblemCommentTest {	
	
	@Test
	public void setAndGetId_shouldWork() {
		ProblemComment comment = new ProblemComment();
		comment.setId(1L);
		
		assertThat(comment.getId()).isEqualTo(1L);
	}
	
	@Test
	public void setAndGetContent_shouldWork() {
		String content = "Iniciando verificação";
		ProblemComment comment = new ProblemComment();
		comment.setContent(content);
		
		assertThat(comment.getContent()).isEqualTo(content);
	}
	
	@Test
	public void setAndGetTimestamp_shouldWork() {
		OffsetDateTime timestamp = OffsetDateTime.now();
		ProblemComment comment = new ProblemComment();
		comment.setTimestamp(timestamp);
		
		assertThat(comment.getTimestamp()).isEqualTo(timestamp);
	}
	
	@Test
	public void setAndGetProblem_shouldWork() {
		Problem problem = new Problem("Distração no estudo");
		ProblemComment comment = new ProblemComment();
		
		comment.setProblem(problem);
		
		assertThat(comment.getProblem()).isEqualTo(problem);
		
	}
	
	@Test(expected = InvalidParameterException.class)
	public void setEmptyContent_shouldThrownInvalidParameterException() {
		ProblemComment comment = new ProblemComment();
		comment.setContent("");
	}
	
	@Test
	public void equals_shouldBeByIdAndTimestamp() {
		Long id = 1L;
		OffsetDateTime timestamp = OffsetDateTime.now();
		
		ProblemComment comment1 = new ProblemComment();
		ProblemComment comment2 = new ProblemComment();

		comment1.setId(id);
		comment1.setTimestamp(timestamp);
		comment2.setId(id);
		comment2.setTimestamp(timestamp);
		
		
		assertThat(comment1).isEqualTo(comment2);
	}
	
	@Test
	public void commentMustHaveAnEmptyConstructor() {
		new ProblemComment();
	}
	
	@Test
	public void commentMustHaveAnConstructorWithContent() {
		String content = "Novo processo iniciado";
		ProblemComment comment = new ProblemComment(content);
		
		assertThat(comment.getContent()).isEqualTo(content);
	}
	
	@Test
	public void problem_mustHaveManyToOneAnnotation() 
			throws NoSuchFieldException, SecurityException {
		
		boolean isIdAnnotationPresent = ProblemComment.class
				.getDeclaredField("problem")
				.isAnnotationPresent(ManyToOne.class);
		
		
		assertThat(isIdAnnotationPresent).isTrue();
	}
}
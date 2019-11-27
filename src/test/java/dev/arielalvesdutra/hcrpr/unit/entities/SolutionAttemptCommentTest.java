package dev.arielalvesdutra.hcrpr.unit.entities;


import static org.assertj.core.api.Assertions.assertThat;

import java.security.InvalidParameterException;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.annotation.JsonIgnore;

import dev.arielalvesdutra.hcrpr.entities.ProblemComment;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttempt;
import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;

@RunWith(SpringRunner.class)
public class SolutionAttemptCommentTest {	
	
	@Test
	public void setAndGetId_shouldWork() {
		SolutionAttemptComment comment = new SolutionAttemptComment();
		comment.setId(1L);
		
		assertThat(comment.getId()).isEqualTo(1L);
	}
	
	@Test
	public void setAndGetContent_shouldWork() {
		String content = "Iniciando verificação";
		SolutionAttemptComment comment = new SolutionAttemptComment();
		comment.setContent(content);
		
		assertThat(comment.getContent()).isEqualTo(content);
	}
	
	@Test
	public void setAndGetSolutionAttempt_shouldWork() {
		SolutionAttempt solutionAttempt = new SolutionAttempt("Utilizar técnica X");
		SolutionAttemptComment comment = new SolutionAttemptComment();
		
		
		comment.setSolutionAttempt(solutionAttempt);
		
		assertThat(comment.getSolutionAttempt()).isEqualTo(solutionAttempt);		
	}
	
	@Test
	public void setAndGetCreatedAt_shouldWork() {
		OffsetDateTime timestamp = OffsetDateTime.now();
		SolutionAttemptComment comment = new SolutionAttemptComment();
		comment.setCreatedAt(timestamp);
		
		assertThat(comment.getCreatedAt()).isEqualTo(timestamp);
	}
	
	@Test(expected = InvalidParameterException.class)
	public void setEmptyContent_shouldThrownInvalidParameterException() {
		SolutionAttemptComment comment = new SolutionAttemptComment();
		comment.setContent("");
	}
	
	@Test
	public void equals_shouldBeByIdAndTimestamp() {
		Long id = 1L;
		OffsetDateTime timestamp = OffsetDateTime.now();
		
		SolutionAttemptComment comment1 = new SolutionAttemptComment();
		SolutionAttemptComment comment2 = new SolutionAttemptComment();

		comment1.setId(id);
		comment1.setCreatedAt(timestamp);
		comment2.setId(id);
		comment2.setCreatedAt(timestamp);
		
		
		assertThat(comment1).isEqualTo(comment2);
	}
	
	@Test
	public void mustHaveAnEmptyConstructor() {
		new SolutionAttemptComment();
	}
	
	@Test
	public void mustHaveAnConstructorWithContent() {
		String content = "Novo processo iniciado";
		SolutionAttemptComment comment = new SolutionAttemptComment(content);
		
		assertThat(comment.getContent()).isEqualTo(content);
	}
	
	@Test
	public void solutionAttempt_mustHaveManyToOneAnnotation() 
			throws NoSuchFieldException, SecurityException {
		
		boolean isManyToOneAnnotationPresent = SolutionAttemptComment.class
				.getDeclaredField("solutionAttempt")
				.isAnnotationPresent(ManyToOne.class);
		
		
		assertThat(isManyToOneAnnotationPresent).isTrue();
	}
	
	@Test
	public void solutionAttempt_mustHaveJsonIgnoreAnnotation() 
			throws NoSuchFieldException, SecurityException {
		boolean isJsonIgnoreAnnotationPresent = SolutionAttemptComment.class
				.getDeclaredField("solutionAttempt")
				.isAnnotationPresent(JsonIgnore.class);
		
		
		assertThat(isJsonIgnoreAnnotationPresent).isTrue();
	}
	
	@Test
	public void content_mustHaveColumnAnnotationWithTextDefinition() 
			throws NoSuchFieldException, SecurityException {
		Column column = SolutionAttemptComment.class
				.getSuperclass()
				.getDeclaredField("content")
				.getAnnotation(Column.class);
		
		
		assertThat(column).isNotNull();
		assertThat(column.columnDefinition()).isEqualTo("TEXT");
	}
}

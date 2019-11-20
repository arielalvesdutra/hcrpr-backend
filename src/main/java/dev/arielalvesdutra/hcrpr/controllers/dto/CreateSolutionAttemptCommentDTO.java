package dev.arielalvesdutra.hcrpr.controllers.dto;

import java.time.OffsetDateTime;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;

public class CreateSolutionAttemptCommentDTO {
	
	@NotEmpty
	@Size(min = 5)
	private String content;
	
	private OffsetDateTime createdAt = OffsetDateTime.now();

	public CreateSolutionAttemptCommentDTO() { }
	
	public CreateSolutionAttemptCommentDTO(String content) {	
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public SolutionAttemptComment toSolutionAttemptComment() {
		SolutionAttemptComment comment = new SolutionAttemptComment();
		comment.setContent(this.getContent());
		comment.setCreatedAt(this.createdAt);
		return comment;
	}
}

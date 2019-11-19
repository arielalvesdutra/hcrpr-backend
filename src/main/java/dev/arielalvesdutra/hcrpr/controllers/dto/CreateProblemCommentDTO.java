package dev.arielalvesdutra.hcrpr.controllers.dto;

import java.time.OffsetDateTime;

import dev.arielalvesdutra.hcrpr.entities.ProblemComment;

public class CreateProblemCommentDTO {
	
	private String content;
	
	private OffsetDateTime createdAt = OffsetDateTime.now();

	public CreateProblemCommentDTO() { }
	
	public CreateProblemCommentDTO(String content) {	
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ProblemComment toProblemComment() {
		ProblemComment comment = new ProblemComment();
		comment.setContent(this.getContent());
		comment.setCreatedAt(this.createdAt);
		return comment;
	}
}

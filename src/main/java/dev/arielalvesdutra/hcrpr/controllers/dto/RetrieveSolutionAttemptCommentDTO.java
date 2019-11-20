package dev.arielalvesdutra.hcrpr.controllers.dto;

import java.time.OffsetDateTime;

import org.springframework.data.domain.Page;

import dev.arielalvesdutra.hcrpr.entities.SolutionAttemptComment;

public class RetrieveSolutionAttemptCommentDTO {
	private Long id;
	
	private String content;
	
	private OffsetDateTime createdAt;
	
	public RetrieveSolutionAttemptCommentDTO() { }
	
	public RetrieveSolutionAttemptCommentDTO(SolutionAttemptComment comment) { 
		this.setId(comment.getId());
		this.setContent(comment.getContent());
		this.setCreatedAt(comment.getCreatedAt());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public static Page<RetrieveSolutionAttemptCommentDTO> fromCommentPageToRetrieveSolutionAttemptCommentDTOPage(
			Page<SolutionAttemptComment> commentsPage) {
		return commentsPage.map(RetrieveSolutionAttemptCommentDTO::new);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RetrieveSolutionAttemptCommentDTO other = (RetrieveSolutionAttemptCommentDTO) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.isEqual(other.createdAt))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RetrieveSolutionAttemptCommentDTO [id=" + id + ", content=" + content + ", createdAt=" + createdAt + "]";
	}
}

package dev.arielalvesdutra.hcrpr.entities;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@SuppressWarnings("serial")
@MappedSuperclass
abstract class AbstractComment implements Serializable {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(columnDefinition = "TEXT")
	protected String content;

	protected OffsetDateTime createdAt = OffsetDateTime.now();

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setContent(String content) {
		
		if (content.isEmpty()) {
			throw new InvalidParameterException("O conteúdo não pode ser nulo");
		}
		
		this.content = content;		
	}

	public String getContent() {
		return this.content;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public OffsetDateTime getCreatedAt() {
		return this.createdAt;
	}
	
	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", timestamp=" + createdAt + "]";
	}
}

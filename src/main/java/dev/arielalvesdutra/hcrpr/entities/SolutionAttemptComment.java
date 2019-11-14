package dev.arielalvesdutra.hcrpr.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class SolutionAttemptComment extends AbstractComment {
	
	private static final long serialVersionUID = -817273275685331354L;
	
	@ManyToOne
	private SolutionAttempt solutionAttempt;
	
	public SolutionAttemptComment() { }		
	
	public SolutionAttemptComment(String content) {
		this.content = content;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
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
		AbstractComment other = (AbstractComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "SolutionAttemptComment [id=" + id + ", content=" + content + ", timestamp=" + createdAt + "]";
	}

	public void setSolutionAttempt(SolutionAttempt solutionAttempt) {
		this.solutionAttempt = solutionAttempt;		
	}
	
	public SolutionAttempt getSolutionAttempt() {
		return solutionAttempt;
	}
}

package dev.arielalvesdutra.hcrpr.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class ProblemComment extends AbstractComment {

	private static final long serialVersionUID = 443036192319928592L;
	
	@ManyToOne
	private Problem problem;
	
	public ProblemComment() { }		
	
	public ProblemComment(String content) {
		this.content = content;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
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
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ProblemComment [id=" + id + ", content=" + content + ", timestamp=" + timestamp + "]";
	}
	
	public Problem getProblem() {
		return problem;
	}
	
	public void setProblem(Problem problem) {
		this.problem = problem;
	}
}

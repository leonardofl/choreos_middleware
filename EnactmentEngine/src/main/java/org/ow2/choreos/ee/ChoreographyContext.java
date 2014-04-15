package org.ow2.choreos.ee;

import org.ow2.choreos.chors.datamodel.Choreography;
import org.ow2.choreos.chors.datamodel.ChoreographySpec;

public class ChoreographyContext {

	private Choreography chor;
	private ChoreographySpec requestedChoreographySpec;

	public ChoreographyContext(Choreography chor) {
		this.chor = chor;
		this.requestedChoreographySpec = this.chor.getChoreographySpec();
	}

	public Choreography getChoreography() {
		return chor;
	}

	public ChoreographySpec getRequestedChoreographySpec() {
		return requestedChoreographySpec;
	}

	public void setRequestedChoreographySpec(
			ChoreographySpec requestedChoreographySpec) {
		this.requestedChoreographySpec = requestedChoreographySpec;
	}

	public void enactmentFinished() {
		chor.setChoreographySpec(requestedChoreographySpec);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chor.getId() == null) ? 0 : chor.getId().hashCode());
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
		ChoreographyContext other = (ChoreographyContext) obj;
		if (chor.getId() == null) {
			if (other.chor.getId() != null)
				return false;
		} else if (!chor.getId().equals(other.chor.getId()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChoreographyContext [" + chor.getId() + "]";
	}

}

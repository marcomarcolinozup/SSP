package org.studentsuccessplan.ssp.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.studentsuccessplan.ssp.model.reference.Challenge;
import org.studentsuccessplan.ssp.model.reference.ChallengeReferral;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Task extends AbstractTask implements Serializable {

	private static final long serialVersionUID = 1477217415946557983L;

	@ManyToOne
	@JoinColumn(name = "challenge_id", updatable = false)
	private Challenge challenge;

	@ManyToOne
	@JoinColumn(name = "challenge_referral_id", updatable = false)
	private ChallengeReferral challengeReferral;

	/**
	 * Constructor that only calls {@link AbstractTask#AbstractTask()}.
	 */
	public Task() {
		super();
	}

	public Task(String description, Date dueDate, Person person,
			Challenge challenge, ChallengeReferral challengeReferral) {
		super(description, dueDate, person);
		this.challenge = challenge;
		this.challengeReferral = challengeReferral;
	}

	public Challenge getChallenge() {
		return challenge;
	}

	public void setChallenge(Challenge challenge) {
		this.challenge = challenge;
	}

	public ChallengeReferral getChallengeReferral() {
		return challengeReferral;
	}

	public void setChallengeReferral(ChallengeReferral challengeReferral) {
		this.challengeReferral = challengeReferral;
	}
}

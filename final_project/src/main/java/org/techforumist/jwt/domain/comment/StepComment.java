package org.techforumist.jwt.domain.comment;

import javax.persistence.Entity;

@Entity
public class StepComment extends Comment{

    private Long stepId;

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }
}
package org.techforumist.jwt.domain.comment;

import javax.persistence.Entity;

@Entity
public class InstructionComment extends Comment{

    private Long instructionId;

    public Long getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(Long instructionId) {
        this.instructionId = instructionId;
    }
}
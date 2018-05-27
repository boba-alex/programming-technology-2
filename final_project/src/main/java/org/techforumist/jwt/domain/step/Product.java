package org.techforumist.jwt.domain.step;

import org.techforumist.jwt.domain.comment.StepComment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long instructionId;
    private String name;
    private String creatorName;

    private Date creationDate;
    private Date lastEditDate;

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection
    private List<ProductBlock> productBlocks;

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection
    private List<StepComment> stepComments;


    public Product() {
        this.creationDate = new Date();
        this.lastEditDate = this.creationDate;
        this.stepComments = new ArrayList<>();
        this.productBlocks = new ArrayList<>();
    }

    public Date getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Date lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getName() {
        return name;
    }

    public List<StepComment> getStepComments() {
        return stepComments;
    }

    public void setStepComments(List<StepComment> stepComments) {
        this.stepComments = stepComments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getInstructionId() {
        return instructionId;
    }

    public void setInstructionId(Long instructionId) {
        this.instructionId = instructionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductBlock> getProductBlocks() {
        return productBlocks;
    }

    public void setProductBlocks(List<ProductBlock> productBlocks) {
        this.productBlocks = productBlocks;
    }
}

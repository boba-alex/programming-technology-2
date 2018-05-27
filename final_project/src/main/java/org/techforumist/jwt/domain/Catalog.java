package org.techforumist.jwt.domain;

import org.techforumist.jwt.domain.comment.InstructionComment;
import org.techforumist.jwt.domain.step.Product;

import javax.persistence.*;
import java.util.*;

@Entity
public class Catalog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Date creationDate;
    private Date lastEditDate;
    private String creatorName;

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection
    private List<Product> products;

    @OneToMany(cascade = CascadeType.ALL)
    @ElementCollection
    private List<InstructionComment> instructionComments;

    private String tags;
    private String category;

    private int rating;
    private HashMap<Long, Integer> ratingMap;// = new HashMap<Long, Integer>();

    public Catalog() {
        this.creationDate = new Date();
        this.lastEditDate = this.creationDate;
        this.ratingMap = new HashMap<>();
        this.products = new ArrayList<>();
        this.instructionComments = new ArrayList<>();
//        this.tags = new ArrayList<>();
//        this.tags = new String[10];
    }

    public Catalog(String name) {
        this.creationDate = new Date();
        this.lastEditDate = this.creationDate;
        this.name = name;
        this.ratingMap = new HashMap<>();
        this.products = new ArrayList<>();
        this.instructionComments = new ArrayList<>();
    }

    public void calclucateRating(Long userId, Integer value) {
        ratingMap.put(userId, value);
        int result = 0;
        for (Integer integer : ratingMap.values()) {
            result += integer ;
        }
        rating = result / ratingMap.size();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public HashMap<Long, Integer> getRatingMap() {
        return ratingMap;
    }

    public void setRatingMap(HashMap<Long, Integer> ratingMap) {
        this.ratingMap = ratingMap;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(Date lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public List<InstructionComment> getInstructionComments() {
        return instructionComments;
    }

    public void setInstructionComments(List<InstructionComment> instructionComments) {
        this.instructionComments = instructionComments;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

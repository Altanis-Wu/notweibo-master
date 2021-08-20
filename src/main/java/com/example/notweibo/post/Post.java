package com.example.notweibo.post;

import com.example.notweibo.user.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 140)
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name="user_id")
    @JsonIdentityReference(alwaysAsId = true)
    private User owner;

    @ManyToMany
    @JoinTable(
            name="post_likes",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    @JsonIdentityReference(alwaysAsId = true)
    private List<User> likes;

    public Post() {
    }

    public Post(String content, User owner, List<User> likes) {
        this.content = content;
        this.owner = owner;
        this.likes = likes;
    }

    public Post(Long id, String content, User owner, List<User> likes) {
        this.id = id;
        this.content = content;
        this.owner = owner;
        this.likes = likes;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}

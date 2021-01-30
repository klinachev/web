package ru.itmo.wp.domain;

import org.hibernate.annotations.CreationTimestamp;
import ru.itmo.wp.form.PostFields;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * @noinspection unused
 */
@Entity
@Table
public class Post {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotEmpty
    @Size(min = 1, max = 60)
    private String title;

    @NotEmpty
    @Size(min = 1, max = 65000)
    @Lob
    private String text;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @OrderBy("name")
    private Set<Tag> tags;

    @CreationTimestamp
    private Date creationTime;

    public Post() {}

    public Post(PostFields postFields) {
        title = postFields.getTitle();
        text = postFields.getText();
        tags = new HashSet<>();
        String[] tagsArray = postFields.getTags().replaceAll(" +", " ").split(" ");
        for (String st: tagsArray) {
            tags.add(new Tag(st));
        }
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public int getCommentsSize() {
        return comments.size();
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public void addComment(Comment comment) {
        comment.setPost(this);
        comments.add(comment);
    }
}

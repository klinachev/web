package ru.itmo.wp.form;


import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PostFields {
    @NotEmpty
    @Size(min = 1, max = 60)
    private String title;

    @NotEmpty
    @Size(min = 1, max = 65000)
    @Lob
    private String text;

    @Size(max = 200)
    @Pattern(regexp = "[a-z ]*", message = "Expected lowercase Latin letters and whitespaces")
    private String tags;

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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}

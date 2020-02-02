package es.codeurjc.daw.dtos;

import java.util.ArrayList;
import java.util.List;

public class PostFullDto {
    private long id = -1;

    private String title;

    private String content;

    private List<CommentFullDto> comments = new ArrayList<>();

    public PostFullDto() {
    }

    public PostFullDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<CommentFullDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentFullDto> comments) {
        this.comments = comments;
    }
}
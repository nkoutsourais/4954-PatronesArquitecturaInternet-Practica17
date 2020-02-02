package es.codeurjc.daw.dtos;

public class CommentFullDto {

    private long id = -1;

    private String author;

    private String message;

    public CommentFullDto() {
    }

    public CommentFullDto(String author, String message) {
        this.author = author;
        this.message = message;
    }

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
	public String toString() {
		return this.author + ": " + this.message;
	}
}
package es.codeurjc.daw;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import es.codeurjc.daw.dtos.CommentFullDto;
import es.codeurjc.daw.dtos.PostFullDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("REST tests")
public class RestTest {

    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
	@DisplayName("Crear un post y verificar que se crea correctamente")
	public void createPostTest() throws Exception{

        // CREAMOS UN NUEVO POST

		PostFullDto post = new PostFullDto("Mi titulo", "Mi contenido");
    	
        PostFullDto createdPost = 
            given().
                request()
                    .body(objectMapper.writeValueAsString(post))
                    .contentType(ContentType.JSON).
            when().
                post("/api/post/").
            then().
                assertThat().
                statusCode(201).
                body("title", equalTo(post.getTitle()))
                .extract().as(PostFullDto.class);

        // COMPROBAMOS QUE EL POST SE HA CREADO CORRECTAMENTE

        when().
            get("/api/post/{id}", createdPost.getId()).
        then().
             assertThat().
             statusCode(200).
             body("title", equalTo(post.getTitle()));
        
    }

    @Test
	@DisplayName("Añadir un comentario a un post y verificar que se añade el comentario")
	public void createCommentTest() throws Exception {

		// CREAMOS UN NUEVO POST

		PostFullDto post = new PostFullDto("Mi titulo", "Mi contenido");
    	
        PostFullDto createdPost = 
            given().
                request()
                    .body(objectMapper.writeValueAsString(post))
                    .contentType(ContentType.JSON).
            when().
                post("/api/post/").
            then()
                .extract().as(PostFullDto.class);
        
        // CREAMOS UN NUEVO COMENTARIO

        CommentFullDto comment = new CommentFullDto("Juan", "Buen post");

        given().
            request()
                    .body(objectMapper.writeValueAsString(comment))
                    .contentType(ContentType.JSON).
        when().
            post("/api/post/{postId}/comment", createdPost.getId()).
        then().
            assertThat().
            statusCode(201).
            body("author", equalTo(comment.getAuthor())).
            body("message", equalTo(comment.getMessage()));

        
        // COMPROBAMOS QUE EL COMENTARIO EXISTE

        when().
            get("/api/post/{id}", createdPost.getId()).
        then().
             assertThat().
             statusCode(200).
             body("comments[0].author", equalTo(comment.getAuthor())).
             body("comments[0].message", equalTo(comment.getMessage()));
    
    }

    @Test
	@DisplayName("Borrar un comentario de un post y verificar que no aparece el comentario")
	public void deleteCommentTest() throws Exception {

        // CREAMOS UN NUEVO POST

		PostFullDto post = new PostFullDto("Mi titulo", "Mi contenido");
    	
        PostFullDto createdPost = 
            given().
                request()
                    .body(objectMapper.writeValueAsString(post))
                    .contentType(ContentType.JSON).
            when().
                post("/api/post/").
            then()
                .extract().as(PostFullDto.class);
        
        // CREAMOS UN NUEVO COMENTARIO

        CommentFullDto comment = new CommentFullDto("Juan", "Buen post");

        CommentFullDto createdComment = given().
            request()
                    .body(objectMapper.writeValueAsString(comment))
                    .contentType(ContentType.JSON).
        when().
            post("/api/post/{postId}/comment", createdPost.getId()).
        then().
            assertThat().
            statusCode(201).
            body("author", equalTo(comment.getAuthor())).
            body("message", equalTo(comment.getMessage())).
            extract().as(CommentFullDto.class);

        // BORRAMOS EL COMENTARIO
        
        when().
             delete(
                 "/api/post/{postId}/comment/{commentId}", 
                 createdPost.getId(), createdComment.getId()
             ).
        then().
             assertThat().
             statusCode(204);


        
        // COMPROBAMOS QUE EL COMENTARIO NO EXISTE

        when().
             get("/api/post/{id}", createdPost.getId()).
        then().
             assertThat().
             statusCode(200).
             body("comments", IsEmptyCollection.empty());
    
    }
    
}
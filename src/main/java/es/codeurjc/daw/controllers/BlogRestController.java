package es.codeurjc.daw.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.daw.dtos.CommentFullDto;
import es.codeurjc.daw.dtos.PostBasicDto;
import es.codeurjc.daw.dtos.PostFullDto;
import es.codeurjc.daw.services.PostService;

@RestController
@RequestMapping("/api")
public class BlogRestController {

	@Autowired
	private PostService postService;

	@GetMapping("/post")
	public ResponseEntity<List<PostBasicDto>> listPosts() {
		return new ResponseEntity<>(new ArrayList<>(this.postService.getPosts().values()), HttpStatus.OK);
	}

	@GetMapping("/post/{id}")
	public ResponseEntity<PostFullDto> getPost(@PathVariable long id) {
		PostFullDto post = this.postService.getPost(id);
		if (post == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(this.postService.getPost(id), HttpStatus.OK);
	}

	@PostMapping("/post")
	public ResponseEntity<PostFullDto> newPost(@RequestBody PostFullDto post) {
		this.postService.addPost(post);
		return new ResponseEntity<>(post, HttpStatus.CREATED);
	}

	@PostMapping("/post/{postId}/comment")
	public ResponseEntity<CommentFullDto> newComment(@PathVariable long postId, @RequestBody CommentFullDto comment) {
		PostFullDto post = this.postService.getPost(postId);
		if (post == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		this.postService.setCommentId(comment);
		this.postService.addComment(post, comment);
		return new ResponseEntity<>(comment, HttpStatus.CREATED);
	}

	@DeleteMapping("/post/{postId}/comment/{commentId}")
	public ResponseEntity<CommentFullDto> deleteComment(@PathVariable long postId, @PathVariable long commentId) {
		PostFullDto post = this.postService.getPost(postId);
		if (post == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		CommentFullDto comment = this.postService.getComment(post, commentId);
		if (comment == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		this.postService.deleteComment(post, comment);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
package es.codeurjc.daw.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.codeurjc.daw.dtos.CommentFullDto;
import es.codeurjc.daw.dtos.PostBasicDto;
import es.codeurjc.daw.dtos.PostFullDto;
import es.codeurjc.daw.entities.Post;

@Service
public class PostService {

	@Autowired
	private PostMapper postMapper;

	private Map<Long, Post> posts = new ConcurrentHashMap<>();
	private AtomicLong lastPostId = new AtomicLong();
	private AtomicLong lastCommentId = new AtomicLong();

	public Map<Long, PostBasicDto> getPosts() {
		return this.posts.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getKey(), e -> postMapper.convertPostEntityToPostBasicDto(e.getValue())));
	}

	public PostFullDto getPost(long id) {
		return postMapper.convertPostEntityToPostFullDto(this.posts.get(id));
	}

	public void addPost(PostFullDto post) {
		post.setId(lastPostId.incrementAndGet());
		this.posts.put(post.getId(), postMapper.convertPostFullDtoToPostEntity(post));
	}

	public void setCommentId(CommentFullDto comment) {
		comment.setId(this.lastCommentId.incrementAndGet());
	}

	public CommentFullDto getComment(PostFullDto post, Long commentId) {
		return postMapper.convertCommentEntityToCommentFullDto(this.posts.get(post.getId()).getComment(commentId));
	}

	public void addComment(PostFullDto post, CommentFullDto comment) {
		this.posts.get(post.getId()).addComment(postMapper.convertCommentFullDtoToCommentEntity(comment));
	}

	public void deleteComment(PostFullDto post, CommentFullDto comment) {
		this.posts.get(post.getId()).deleteComment(comment.getId());
	}
}
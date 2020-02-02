package es.codeurjc.daw.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.codeurjc.daw.dtos.*;
import es.codeurjc.daw.entities.*;

@Component
class PostMapper {

    @Autowired
	private ModelMapper modelMapper;
    
    public PostFullDto convertPostEntityToPostFullDto(Post post) {
		return modelMapper.map(post, PostFullDto.class);
	}

	public Post convertPostFullDtoToPostEntity(PostFullDto post) {
		return modelMapper.map(post, Post.class);
	}

	public PostBasicDto convertPostEntityToPostBasicDto(Post post) {
		return modelMapper.map(post, PostBasicDto.class);
	}

	public Comment convertCommentFullDtoToCommentEntity(CommentFullDto comment) {
		return modelMapper.map(comment, Comment.class);
	}

	public CommentFullDto convertCommentEntityToCommentFullDto(Comment comment) {
		return modelMapper.map(comment, CommentFullDto.class);
	}
}
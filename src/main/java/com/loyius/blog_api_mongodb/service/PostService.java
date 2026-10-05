package com.loyius.blog_api_mongodb.service;

import com.loyius.blog_api_mongodb.domain.Post;
import com.loyius.blog_api_mongodb.repository.PostRepository;
import com.loyius.blog_api_mongodb.service.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PostService {
    private PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Post findById(String id) {
        Optional<Post> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado"));
    }

    public List<Post> findByTitle(String text) {
        return repository.searchTitle(Pattern.quote(text));
    }

    public List<Post> fullSearch(String text, Date minDate, Date maxDate) {
        maxDate = new Date(maxDate.getTime() + 24 * 60 * 60 * 1000);
        return repository.fullSearch(Pattern.quote(text), minDate, maxDate);
    }
}

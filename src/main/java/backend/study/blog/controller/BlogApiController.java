package backend.study.blog.controller;

import backend.study.blog.domain.Article;
import backend.study.blog.domain.Comment;
import backend.study.blog.dto.ArticleDto;
import backend.study.blog.dto.CommentDto;
import backend.study.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("/articles")
    public ResponseEntity<ArticleDto> addArticle(@RequestBody @Validated ArticleDto articleDto,
                                                 Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ArticleDto.toDto(blogService.save(articleDto, principal.getName())));
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDto>> findAllArticles() {
        List<Article> articleList = blogService.findAll();

        return ResponseEntity.ok(articleList.stream()
                .map(ArticleDto::toDto)
                .toList());
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleDto> findArticle(@PathVariable("id") long id) {
        return ResponseEntity.ok(ArticleDto.toDto(blogService.findById(id)));
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") long id) {
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable("id") long id,
                                                    @RequestBody ArticleDto articleDto) {
        return ResponseEntity.ok(ArticleDto.toDto(blogService.update(id, articleDto)));
    }

    @PostMapping("/comments")
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto, Principal principal) {
        Comment savedComment = blogService.addComment(commentDto, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentDto(savedComment));
    }
}

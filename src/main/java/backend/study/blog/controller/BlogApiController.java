package backend.study.blog.controller;

import backend.study.blog.domain.Article;
import backend.study.blog.dto.ArticleDto;
import backend.study.blog.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class BlogApiController {

    private final BlogService blogService;

    @PostMapping("")
    public ResponseEntity<ArticleDto> addArticle(@RequestBody ArticleDto articleDto, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ArticleDto.toDto(blogService.save(articleDto, principal.getName())));
    }

    @GetMapping("")
    public ResponseEntity<List<ArticleDto>> findAllArticles() {
        List<Article> articleList = blogService.findAll();

        return ResponseEntity.ok(articleList.stream()
                .map(ArticleDto::toDto)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> findArticle(@PathVariable("id") long id) {
        return ResponseEntity.ok(ArticleDto.toDto(blogService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable("id") long id) {
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable("id") long id,
                                                    @RequestBody ArticleDto articleDto) {
        return ResponseEntity.ok(ArticleDto.toDto(blogService.update(id, articleDto)));
    }
}

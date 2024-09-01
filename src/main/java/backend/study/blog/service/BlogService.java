package backend.study.blog.service;

import backend.study.blog.config.error.ErrorCode;
import backend.study.blog.config.error.exception.ArticleNotFoundException;
import backend.study.blog.config.error.exception.NotFoundException;
import backend.study.blog.domain.Article;
import backend.study.blog.dto.ArticleDto;
import backend.study.blog.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public Article save(ArticleDto articleDto, String userName) {
       return blogRepository.save(articleDto.toEntity(userName));
    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
    }

    public void delete(long id) {
        Article article = blogRepository.findById(id)
                        .orElseThrow(ArticleNotFoundException::new);
        authorizeArticleAuthor(article);
        blogRepository.deleteById(article.getId());
    }

    @Transactional
    public Article update(long id, ArticleDto articleDto) {
        Article article = blogRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);

        authorizeArticleAuthor(article);
        article.update(articleDto.getTitle(), articleDto.getContent());

        return article;
    }

    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new NotFoundException(ErrorCode.INVALID_INPUT_VALUE);
        }
    }
}

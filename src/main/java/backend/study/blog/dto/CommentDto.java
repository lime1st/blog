package backend.study.blog.dto;

import backend.study.blog.domain.Article;
import backend.study.blog.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentDto {

    private Long articleId;

    private String content;

    public Comment toEntity(String author, Article article) {
        return Comment.builder()
                .article(article)
                .content(content)
                .author(author)
                .build();
    }

    public CommentDto(Comment comment) {
        this.articleId = comment.getId();
        this.content = comment.getContent();
    }
}

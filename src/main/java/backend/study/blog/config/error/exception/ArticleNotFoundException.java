package backend.study.blog.config.error.exception;

import backend.study.blog.config.error.ErrorCode;

public class ArticleNotFoundException extends NotFoundException {

    public ArticleNotFoundException() {
        super(ErrorCode.ARTICLE_NOT_FOUND);
    }
}

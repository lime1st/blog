package backend.study.blog.config.error.exception;

import backend.study.blog.config.error.ErrorCode;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }
}

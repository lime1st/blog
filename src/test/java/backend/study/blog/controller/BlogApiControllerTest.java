package backend.study.blog.controller;


import backend.study.blog.config.error.ErrorCode;
import backend.study.blog.domain.Article;
import backend.study.blog.domain.Comment;
import backend.study.blog.domain.User;
import backend.study.blog.dto.ArticleDto;
import backend.study.blog.dto.CommentDto;
import backend.study.blog.repository.BlogRepository;
import backend.study.blog.repository.CommentRepository;
import backend.study.blog.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    BlogRepository blogRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommentRepository commentRepository;

    User user;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        blogRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("user@email.com")
                .password("test").build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        user,
                        user.getPassword(),
                        user.getAuthorities()));
    }

    @DisplayName("addComment: 댓 추가")
    @Test
    public void addComment() throws Exception {
        //  given
        final String url = "/api/comments";

        Article savedArticle = createDefaultArticle();
        final Long articleId = savedArticle.getId();
        final String content = "content";
        final CommentDto commentDto = new CommentDto(articleId, content);
        final String requestBody = objectMapper.writeValueAsString(commentDto);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        //  when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        //  then
        resultActions.andExpect(status().isCreated());

        List<Comment> comments = commentRepository.findAll();

        assertThat(comments.size()).isEqualTo(1);
        assertThat(comments.get(0).getArticle().getId()).isEqualTo(articleId);
        assertThat(comments.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findArticle: 잘못된 HTTP 메서드로 조회하면 실패")
    @Test
    public void invalidHttpMethod() throws Exception {
        //  given
        final String url = "/api/articles/{id}";

        //  when
        final ResultActions resultActions = mockMvc.perform(post(url, 1));

        //  then
        resultActions
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.message").value(ErrorCode.METHOD_NOT_ALLOWED.getMessage()));
    }

    @DisplayName("addArticle: title 이 null 이면 실패")
    @Test
    public void addArticleNullValidation() throws Exception {
        //  given
        final String url = "/api/articles";
        final String title = null;
        final String content = "content";
        final ArticleDto userRequest = new ArticleDto(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        //  when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        //  then
        result
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @DisplayName("findArticle: 존재하지 않는 아티클 조회는 실패")
    @Test
    public void findArticleInvalidArticle() throws Exception {
        //  given
        final String url = "/api/articles/{id}";
        final long invalidId = 1;

        //  when
        final ResultActions resultActions = mockMvc.perform(get(url, invalidId));

        //  then
        resultActions
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.ARTICLE_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.code").value((ErrorCode.ARTICLE_NOT_FOUND.getCode())));
    }

    @DisplayName("addArticle: title 이 10글자 초과면 실패")
    @Test
    public void addArticleSizeValidation() throws Exception {
        //  given
        Faker faker = new Faker();

        final String url = "/api/articles";
        final String title = faker.lorem().characters(11);
        final String content = "content";
        final ArticleDto userRequest = new ArticleDto(title, content);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        //  when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        //  then
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("addArticle: 글 쓰기")
    @Test
    public void addArticle() throws Exception {
        //  given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";
        final ArticleDto userRequest = new ArticleDto(title, content);

        //  객체 -> json
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        //  when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        //  then
        result.andExpect(status().isCreated());
        List<Article> articles = blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("findAll: 목록 조회")
    @Test
    public void findAllArticles() throws Exception {
        //  given
        final String url = "/api/articles";
//        final String title = "title";
//        final String content = "content";

//        blogRepository.save(Article.builder()
//                .title(title)
//                .content(content)
//                .build());
        Article savedArticle = createDefaultArticle();

        //  when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //  then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
//                .andExpect(jsonPath("$[0].content").value(content))
//                .andExpect(jsonPath("$[0].title").value(title));
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
    }

    @DisplayName("findArticle: 글 조회")
    @Test
    public void findArticles() throws Exception {
        //  given
        final String url = "/api/articles/{id}";
//        final String title = "title";
//        final String content = "content";
//
//        Article savedArticle = blogRepository.save(Article.builder()
//                .title(title)
//                .content(content)
//                .build());
        Article savedArticle = createDefaultArticle();

        //  when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        //  then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
//                .andExpect(jsonPath("$.content").value(content))
//                .andExpect(jsonPath("$.title").value(title));
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
    }

    @DisplayName("deleteArticle: 글 삭제")
    @Test
    public void deleteArticle() throws Exception {
        //  given
        final String url = "/api/articles/{id}";
//        final String title = "title";
//        final String content = "content";
//
//        Article savedArticle = blogRepository.save(Article.builder()
//                .title(title)
//                .content(content)
//                .build());
        Article savedArticle = createDefaultArticle();

        //  when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        //  then
        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle: 글 수정")
    @Test
    public void updateArticle() throws Exception {
        //  given
        final String url = "/api/articles/{id}";
//        final String title = "title";
//        final String content = "content";
//
//        Article savedArticle = blogRepository.save(Article.builder()
//                .title(title)
//                .content(content)
//                .build());
        Article savedArticle = createDefaultArticle();

        final String newTitle = "new title";
        final String newContent = "new content";

        ArticleDto articleDto = new ArticleDto(newTitle, newContent);

        //  when
        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(articleDto)));

        //  then
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

    private Article createDefaultArticle() {
        return blogRepository.save(Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content").build());
    }
}
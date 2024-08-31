//package backend.study.blog.controller;
//
//import backend.study.blog.domain.Article;
//import backend.study.blog.domain.User;
//import backend.study.blog.dto.ArticleDto;
//import backend.study.blog.repository.BlogRepository;
//import backend.study.blog.repository.UserRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.security.Principal;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class BlogApiControllerTest {
//
//    @Autowired
//    protected MockMvc mockMvc;
//
//    @Autowired
//    protected ObjectMapper objectMapper;
//
//    @Autowired
//    private WebApplicationContext context;
//
//    @Autowired
//    BlogRepository blogRepository;
//
//    @Autowired
//    UserRepository userRepository;
//
//    User user;
//
//    @BeforeEach
//    public void mockMvcSetUp() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//
//        blogRepository.deleteAll();
//    }
//
//    @BeforeEach
//    void setSecurityContext() {
//        userRepository.deleteAll();
//        user = userRepository.save(User.builder()
//                .email("user@email.com")
//                .password("test").build());
//
//        SecurityContext context = SecurityContextHolder.getContext();
//        context.setAuthentication(
//                new UsernamePasswordAuthenticationToken(
//                        user,
//                        user.getPassword(),
//                        user.getAuthorities()));
//    }
//
//    @DisplayName("addArticle: 글 쓰기")
//    @Test
//    public void addArticle() throws Exception {
//        //  given
//        final String url = "/api/articles";
//        final String title = "title";
//        final String content = "content";
//        final ArticleDto userRequest = new ArticleDto(title, content);
//
//        //  객체 -> json
//        final String requestBody = objectMapper.writeValueAsString(userRequest);
//
//        Principal principal = Mockito.mock(Principal.class);
//        Mockito.when(principal.getName()).thenReturn("username");
//
//        //  when
//        ResultActions result = mockMvc.perform(post(url)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .principal(principal)
//                .content(requestBody));
//
//        //  then
//        result.andExpect(status().isCreated());
//        List<Article> articles = blogRepository.findAll();
//        assertThat(articles.size()).isEqualTo(1);
//        assertThat(articles.get(0).getTitle()).isEqualTo(title);
//        assertThat(articles.get(0).getContent()).isEqualTo(content);
//    }
//
//    @DisplayName("findAll: 목록 조회")
//    @Test
//    public void findAllArticles() throws Exception {
//        //  given
//        final String url = "/api/articles";
////        final String title = "title";
////        final String content = "content";
//
////        blogRepository.save(Article.builder()
////                .title(title)
////                .content(content)
////                .build());
//        Article savedArticle = createDefaultArticle();
//
//        //  when
//        final ResultActions resultActions = mockMvc.perform(get(url)
//                .accept(MediaType.APPLICATION_JSON));
//
//        //  then
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
////                .andExpect(jsonPath("$[0].content").value(content))
////                .andExpect(jsonPath("$[0].title").value(title));
//                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
//    }
//
//    @DisplayName("findArticle: 글 조회")
//    @Test
//    public void findArticles() throws Exception {
//        //  given
//        final String url = "/api/articles/{id}";
////        final String title = "title";
////        final String content = "content";
////
////        Article savedArticle = blogRepository.save(Article.builder()
////                .title(title)
////                .content(content)
////                .build());
//        Article savedArticle = createDefaultArticle();
//
//        //  when
//        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));
//
//        //  then
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
////                .andExpect(jsonPath("$.content").value(content))
////                .andExpect(jsonPath("$.title").value(title));
//                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
//    }
//
//    @DisplayName("deleteArticle: 글 삭제")
//    @Test
//    public void deleteArticle() throws Exception {
//        //  given
//        final String url = "/api/articles/{id}";
////        final String title = "title";
////        final String content = "content";
////
////        Article savedArticle = blogRepository.save(Article.builder()
////                .title(title)
////                .content(content)
////                .build());
//        Article savedArticle = createDefaultArticle();
//
//        //  when
//        mockMvc.perform(delete(url, savedArticle.getId()))
//                .andExpect(status().isOk());
//
//        //  then
//        List<Article> articles = blogRepository.findAll();
//
//        assertThat(articles).isEmpty();
//    }
//
//    @DisplayName("updateArticle: 글 수정")
//    @Test
//    public void updateArticle() throws Exception {
//        //  given
//        final String url = "/api/articles/{id}";
////        final String title = "title";
////        final String content = "content";
////
////        Article savedArticle = blogRepository.save(Article.builder()
////                .title(title)
////                .content(content)
////                .build());
//        Article savedArticle = createDefaultArticle();
//
//        final String newTitle = "new title";
//        final String newContent = "new content";
//
//        ArticleDto articleDto = new ArticleDto(newTitle, newContent);
//
//        //  when
//        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(objectMapper.writeValueAsString(articleDto)));
//
//        //  then
//        result.andExpect(status().isOk());
//
//        Article article = blogRepository.findById(savedArticle.getId()).get();
//
//        assertThat(article.getTitle()).isEqualTo(newTitle);
//        assertThat(article.getContent()).isEqualTo(newContent);
//    }
//
//    private Article createDefaultArticle() {
//        return blogRepository.save(Article.builder()
//                .title("title")
//                .author(user.getUsername())
//                .content("content").build());
//    }
//}
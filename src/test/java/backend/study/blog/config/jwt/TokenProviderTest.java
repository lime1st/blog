//package backend.study.blog.config.jwt;
//
//import backend.study.blog.domain.User;
//import backend.study.blog.repository.UserRepository;
//import io.jsonwebtoken.Jwts;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.time.Duration;
//import java.util.Date;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//class TokenProviderTest {
//
//    @Autowired
//    private TokenProvider tokenProvider;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private JwtProperties jwtProperties;
//
//    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰 생성")
//    @Test
//    void testGenerateToken() {
//        //  given
//        User testUser = userRepository.save(User.builder()
//                .email("test@test.com")
//                .password("test")
//                .build());
//        //  when
//        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));
//
//        //  then
//        Long userId = Jwts.parser()
//                .setSigningKey(jwtProperties.getSecretKey())
//                .parseClaimsJws(token)
//                .getBody()
//                .get("id", Long.class);
//
//        assertThat(userId).isEqualTo(testUser.getId());
//    }
//
//    @DisplayName("validToken(): 만료된 토큰은 유효성 검증에 실패")
//    @Test
//    void testValid_inValid_Token() {
//        //  given
//        String token = JwtFactory.builder()
//                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
//                .build()
//                .createToken(jwtProperties);
//        //  when
//        boolean result = tokenProvider.validToken(token);
//
//        //  then
//        assertThat(result).isFalse();
//    }
//
//    @DisplayName("getAuthentication(): 토큰 기반 인증 정보")
//    @Test
//    void testGetAuthentication() {
//        //  given
//        String userEmail = "user@email.com";
//        String token = JwtFactory.builder()
//                .subject(userEmail)
//                .build()
//                .createToken(jwtProperties);
//        //  when
//        Authentication authentication = tokenProvider.getAuthentication(token);
//
//        //  then
//        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
//    }
//
//    @DisplayName("getUserId(): 토큰으로 유저 ID")
//    @Test
//    void testGetUserId() {
//        //  given
//        Long userId = 1L;
//        String token = JwtFactory.builder()
//                .claims(Map.of("id", userId))
//                .build()
//                .createToken(jwtProperties);
//        //  when
//        Long userIdByToken = tokenProvider.getUserId(token);
//
//        //  then
//        assertThat(userIdByToken).isEqualTo(userId);
//    }
//}
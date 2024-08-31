package backend.study.blog.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtil {

    //  요청값(이름, 값, 만료 기간)을 바탕으로 쿠키 추가
    public static void addCooke(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    //  쿠키의 이름을 입력받아 쿠키 삭제
    public static void deleteCooke(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    //  객체를 직렬화해 쿠키의 값으로 변환
    public static String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }

    //  쿠키를 역직렬화해 객체로 변환
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }

//    SerializationUtils.deserialize 가 deprecated -> 아래의 메서드로 변경
//      변경하는 김에 위의 메서드 serialize 도 변경하여 아래의 메서드들로 대체할 수 있으나 호출하는 쪽에서 IOExeption 처리를 해주어야 한다.
//    public static <T> T deserialize(Cookie cookie, Class<T> cls) throws IOException, ClassNotFoundException {
//        // Base64로 인코딩된 쿠키 값을 디코딩하여 바이트 배열로 변환
//        byte[] data = Base64.getUrlDecoder().decode(cookie.getValue());
//
//        // ByteArrayInputStream을 통해 ObjectInputStream으로 변환
//        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
//             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
//
//            // 역직렬화된 객체를 반환
//            Object deserializedObject = objectInputStream.readObject();
//
//            // 역직렬화된 객체가 원하는 타입인지 확인
//            if (cls.isInstance(deserializedObject)) {
//                return cls.cast(deserializedObject);
//            } else {
//                throw new ClassCastException("Failed to cast deserialized object to " + cls.getName());
//            }
//        }
//    }
//    public static String serialize(Object obj) throws IOException {
//        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
//            objectOutputStream.writeObject(obj);
//            byte[] serializedBytes = byteArrayOutputStream.toByteArray();
//            return Base64.getUrlEncoder().encodeToString(serializedBytes);
//        }
//    }
}

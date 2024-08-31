package backend.study.blog.service;

import backend.study.blog.domain.User;
import backend.study.blog.dto.UserDto;
import backend.study.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

//    private final BCryptPasswordEncoder bCryptPasswordEncoder;

//    public Long save(UserDto userDto) {
//        return userRepository.save(User.builder()
//                .email(userDto.getEmail())
//                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
//                .build()).getId();
//    }

    public Long save(UserDto dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return userRepository.save(User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .build()).getId();
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }
}

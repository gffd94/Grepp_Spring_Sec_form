package io.gffd94.security_form.app;

import io.gffd94.security_form.dao.MemberRepository;
import io.gffd94.security_form.domain.Member;
import io.gffd94.security_form.dto.MemberDetails;
import io.gffd94.security_form.dto.SignUpForm;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// 비즈니스 로직
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public void save (SignUpForm signUpForm) {

        Member member = Member.builder()
                .username(signUpForm.getUsername())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .email(signUpForm.getEmail())
                .build();


        memberRepository.save(member);
    }


    // 로그인 시도할 때 호출하는 메서드
    // Member -> UserDetails 로 변환 해줘야 SpringSecurity가 알아먹음. 그 작업을 해주는 메서드
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> memberOptional = memberRepository.findByUsername(username);
        Member findMember = memberOptional.orElseThrow(() -> new UsernameNotFoundException(username + " not found"));

        // security 에서 제공하는 것
//        new User(findMember.getUsername(), findMember.getPassword(), List.of(new SimpleGrantedAuthority(findMember.getRole())));

        return new MemberDetails(findMember);
    }
}

// 구조의 흐름
/*
* 1. 사용자가 로그인폼에서 username, password 입력
     ↓
2. Spring Security가 `loadUserByUsername(username)` 호출
     ↓
3. 우리는 DB에서 Member 조회 → MemberDetails로 감싸서 리턴
     ↓
4. Security가 그 정보로 로그인 시도 (비밀번호 비교, 권한 확인)
     ↓
5. 성공하면 SecurityContext에 사용자 정보 저장

* */
package io.gffd94.security_form.dto;

import io.gffd94.security_form.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
// DB에 있는 Member객체를 기반으로 사용자 정보를 security에 넘겨줌
public class MemberDetails implements UserDetails {

    private final String username;
    private final String password;
    private final String role;

    // DB → Member → MemberDetails
    public MemberDetails(Member member) {
        this.username = member.getUsername();
        this.password = member.getPassword();
        this.role = member.getRole();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    // Role권한을 넘겨주는 부분
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //SimpleGrantedAuthority("MEMBER") 이런 식으로 Spring Security는 권한을 관리함.
        return List.of(new SimpleGrantedAuthority(this.role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

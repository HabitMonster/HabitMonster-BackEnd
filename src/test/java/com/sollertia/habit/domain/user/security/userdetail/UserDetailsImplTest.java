package com.sollertia.habit.domain.user.security.userdetail;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserDetailsImplTest {

    User testUser;
    UserDetailsImpl userDetails;

    @BeforeEach
    public void setUp() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
        userDetails = new UserDetailsImpl(testUser);
    }

    @Test
    void getPassword() {
        //when
        String password = userDetails.getPassword();

        //then
        assertThat(password).isEqualTo("");
    }

    @Test
    void getUsername() {
        //when
        String username = userDetails.getUsername();

        //then
        assertThat(username).isEqualTo(testUser.getSocialId());
    }

    @Test
    void isAccountNonExpired() {
        //when
        boolean accountNonExpired = userDetails.isAccountNonExpired();

        //then
        assertThat(accountNonExpired).isTrue();
    }

    @Test
    void isAccountNonLocked() {
        //when
        boolean accountNonLocked = userDetails.isAccountNonLocked();

        //then
        assertThat(accountNonLocked).isTrue();
    }

    @Test
    void isCredentialsNonExpired() {
        //when
        boolean credentialsNonExpired = userDetails.isCredentialsNonExpired();

        //then
        assertThat(credentialsNonExpired).isTrue();
    }

    @Test
    void isEnabled() {
        //when
        boolean enabled = userDetails.isEnabled();

        //then
        assertThat(enabled).isTrue();
    }
}
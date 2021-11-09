package com.sollertia.habit.domain.user.security.userdetail;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    User testUser;

    @BeforeEach
    public void setUp() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
    }

    @Test
    void loadUserByUsername() {
        //given
        given(userRepository.findBySocialId(testUser.getSocialId()))
                .willReturn(Optional.of(testUser));
        //when
        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getSocialId());

        //then
        assertThat(userDetails.getUsername()).isEqualTo(testUser.getSocialId());
        assertThat(userDetails.getPassword()).isEqualTo("");
        verify(userRepository).findBySocialId(testUser.getSocialId());
    }

    @Test
    void loadUserByUsernameNotFound() {
        //given
        given(userRepository.findBySocialId(testUser.getSocialId()))
                .willReturn(Optional.empty());
        //when, then
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(testUser.getSocialId()));
        verify(userRepository).findBySocialId(testUser.getSocialId());
    }
}
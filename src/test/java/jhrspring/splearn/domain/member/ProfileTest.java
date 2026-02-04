package jhrspring.splearn.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
    @Test
    void profile(){
        new Profile("12345");
    }

    @Test
    void profileFail(){
        assertThatThrownBy(() -> new Profile(""))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Profile("123412412985257819"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Profile("A"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Profile("프로필"))
                .isInstanceOf(IllegalArgumentException.class);
        ;
    }

    @Test
    void url(){
        var profile = new Profile("jhr");
        assertThat(profile.url())
                .isEqualTo("@jhr");

    }
}
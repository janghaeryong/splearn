package jhrspring.splearn.domain;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MemberTest {
    Member member;
    PasswordEncoder passwordEncoder;
    @BeforeEach
    void setUp() {
        this.passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
        member = Member.create(new MemberCreateRequest("jhr@naver.com","jhr","secret"), passwordEncoder);
    }

    @Test
    void createMember() {
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void activate(){
        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }

    @Test
    void activateFail(){
        member.activate();

        assertThatThrownBy(member::activate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void deactivate(){
        member.activate();

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATE);
    }

    @Test
    @DisplayName("ACTIVE 상태에서만 DEACTIVATE() 가 가능")
    void deactivateFail(){
        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);

        member.activate();
        member.deactivate();

        assertThatThrownBy(member::deactivate)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void verifyPassword(){
        assertThat(member.verifyPassword("secret", passwordEncoder)).isTrue();
    }

    @Test
    void changeNickName(){
        assertThat(member.getNickname()).isEqualTo("jhr");

        member.changeNickName("todo");

        assertThat(member.getNickname()).isEqualTo("todo");
    }

    @Test
    void changePassword(){
        member.changePassword("change", passwordEncoder);

        assertThat(member.verifyPassword("change", passwordEncoder)).isTrue();
    }

    @Test
    void isActive(){
        assertThat(member.isActive()).isFalse();

        member.activate();

        assertThat(member.isActive()).isTrue();

        member.deactivate();

        assertThat(member.isActive()).isFalse();
    }

    @Test
    void invalidEmail(){
        assertThatThrownBy(()->
                Member.create(new MemberCreateRequest("invalid","jhr","secret"), passwordEncoder)
        ).isInstanceOf(IllegalArgumentException.class);

        Member.create(new MemberCreateRequest("invalid@naver.com","jhr","secret"), passwordEncoder);
    }

}
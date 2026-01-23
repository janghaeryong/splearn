package jhrspring.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {
    @Test
    void equality(){
        var email1 = new Email("jhr7124@naver.com");
        var email2 = new Email("jhr7125@naver.com");


        assertThat(email1.address()).isEqualTo(email2.address());
        //assertThat(email1).isEqualTo(email2);
    }
}
package jhrspring.splearn.application.provided;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jhrspring.splearn.SplearnTestConfiguration;
import jhrspring.splearn.domain.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
public record MemberRegisterTest(
    MemberRegister memberRegister
) {
    //안티 패턴 이지만 테스트에서는 사용함
    @Test
    void register(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThat(member.getId()).isNotNull();
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmailFail(){
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        assertThatThrownBy(()-> memberRegister.register(MemberFixture.createMemberRegisterRequest()))
                .isInstanceOf(DuplicateEmailException.class);
    }
    @Test
    void memberRegisterRequestFail(){
        extracted(new MemberRegisterRequest("jhr@naver.com", "jhr", "longsecret"));
        extracted(new MemberRegisterRequest("jhr@naver.com", "Charlie__________________________", "longsecret"));
        extracted(new MemberRegisterRequest("jhrnaver.com", "Charlie", "longsecret"));
    }

    private void extracted(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }
}

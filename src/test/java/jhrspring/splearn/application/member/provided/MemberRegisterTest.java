package jhrspring.splearn.application.member.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jhrspring.splearn.SplearnTestConfiguration;
import jhrspring.splearn.domain.member.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberRegisterTest(
    MemberRegister memberRegister,
    EntityManager entityManager
) {
    //안티 패턴 이지만 테스트에서는 사용함
    @Test
    void register(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());

        System.out.println(member);

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
    void activate(){
        Member member = registerMember();

        member = memberRegister.activate(member.getId());

        entityManager.flush();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getDetail().getActivatedAt()).isNotNull();

    }

    @Test
    void deactivate(){
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        member = memberRegister.deactivate(member.getId());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.DEACTIVATE);
        assertThat(member.getDetail().getDeactivatedAt()).isNotNull();

    }

    @Test
    void updateInfo(){
        Member member = registerMember();

        memberRegister.activate(member.getId());
        entityManager.flush();
        entityManager.clear();

        var request = new MemberInfoUpdateRequest("Peter", "jhr100", "자기소개");

        member = memberRegister.updateInfo(member.getId(), request);

        assertThat(member.getNickname()).isEqualTo("Peter");
        assertThat(member.getDetail().getProfile().address()).isEqualTo("jhr100");
    }

    @Test
    void updateInfoFail(){
        Member member = registerMember();
        memberRegister.activate(member.getId());
        var request = new MemberInfoUpdateRequest("Peter", "jhr100", "자기소개");
        memberRegister.updateInfo(member.getId(), request);

        Member member2 = registerMember("jhr2@naver.com");
        memberRegister.activate(member2.getId());
        entityManager.flush();
        entityManager.clear();

        //member2 는 기존의 member와 같은 프로필을 사용 할 수 없다.
        assertThatThrownBy(() -> {
            var request2 = new MemberInfoUpdateRequest("james", "jhr100", "introduction");
            memberRegister.updateInfo(member2.getId(), request2);
        }).isInstanceOf(DuplicateProfileException.class);

        memberRegister.updateInfo(member2.getId(), new MemberInfoUpdateRequest("james", "", "introduction"));
    }


    @Test
    void memberRegisterRequestFail(){
        checkValidation(new MemberRegisterRequest("jhr@naver.com", "jhr", "longsecret"));
        checkValidation(new MemberRegisterRequest("jhr@naver.com", "Charlie__________________________", "longsecret"));
        checkValidation(new MemberRegisterRequest("jhrnaver.com", "Charlie", "longsecret"));
    }

    private void checkValidation(MemberRegisterRequest invalid) {
        assertThatThrownBy(() -> memberRegister.register(invalid))
                .isInstanceOf(ConstraintViolationException.class);
    }

    Member registerMember(){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        return member;
    }
    Member registerMember(String email){
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest(email));
        entityManager.flush();
        entityManager.clear();
        return member;
    }
}

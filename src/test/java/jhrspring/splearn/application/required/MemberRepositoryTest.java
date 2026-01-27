package jhrspring.splearn.application.required;


import jakarta.persistence.EntityManager;
import jhrspring.splearn.domain.Member;
import jhrspring.splearn.domain.MemberFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    void createMember(){
        Member member = Member.register(MemberFixture.createMemberRegisterRequest(), MemberFixture.createPasswordEncoder());

        assertThat(member.getId()).isNull();

        memberRepository.save(member);

        assertThat(member.getId()).isNotNull();

        entityManager.flush();
    }

    @Test
    void duplicateEmailFail(){
        Member member = Member.register(MemberFixture.createMemberRegisterRequest(), MemberFixture.createPasswordEncoder());
        memberRepository.save(member);

        Member member2 = Member.register(MemberFixture.createMemberRegisterRequest(), MemberFixture.createPasswordEncoder());

        assertThatThrownBy(()-> memberRepository.save(member2))
                .isInstanceOf(DataIntegrityViolationException.class);

    }
}
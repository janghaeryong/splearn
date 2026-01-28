package jhrspring.splearn.application.provided;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jhrspring.splearn.SplearnTestConfiguration;
import jhrspring.splearn.domain.Member;
import jhrspring.splearn.domain.MemberFixture;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Import(SplearnTestConfiguration.class)
record MemberFinderTest(
        MemberFinder memberFinder,
        MemberRegister memberRegister,
        EntityManager entityManager

) {
    @Test
    void find() {
        Member member = memberRegister.register(MemberFixture.createMemberRegisterRequest());
        entityManager.flush();
        entityManager.clear();
        Member found = memberFinder.find(member.getId());
        assertThat(found.getId()).isNotNull();
    }
    
    @Test
    void findFail(){
        assertThatThrownBy(() -> memberFinder.find(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
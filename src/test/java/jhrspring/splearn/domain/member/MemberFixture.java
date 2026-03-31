package jhrspring.splearn.domain.member;

import org.springframework.test.util.ReflectionTestUtils;

public class MemberFixture {
    public static MemberRegisterRequest createMemberRegisterRequest(String email) {
        return new MemberRegisterRequest(email, "Charlie", "verysecret");
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("jhr@naver.com");
    }

    public static PasswordEncoder createPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(String password) {
                return password.toUpperCase();
            }

            @Override
            public boolean matches(String password, String passwordHash) {
                return encode(password).equals(passwordHash);
            }
        };
    }
    public static Member createMember() {
        return Member.register(createMemberRegisterRequest(), createPasswordEncoder());
    }
    public static Member createMember(Long memberId) {
        Member member = Member.register(createMemberRegisterRequest(), createPasswordEncoder());
        ReflectionTestUtils.setField(member, "id", memberId);
        return member;
    }
}

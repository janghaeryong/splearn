package jhrspring.splearn.application.member;

import jakarta.transaction.Transactional;
import jhrspring.splearn.application.member.provided.MemberFinder;
import jhrspring.splearn.application.member.provided.MemberRegister;
import jhrspring.splearn.application.member.required.EmailSender;
import jhrspring.splearn.application.member.required.MemberRepository;
import jhrspring.splearn.domain.member.DuplicateEmailException;
import jhrspring.splearn.domain.member.Member;
import jhrspring.splearn.domain.member.MemberRegisterRequest;
import jhrspring.splearn.domain.member.PasswordEncoder;
import jhrspring.splearn.domain.shared.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberModifyService implements MemberRegister {
    private final MemberFinder memberFinder;

    private final MemberRepository memberRepository;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member register(MemberRegisterRequest registerRequest) {
        // check
        checkDuplicateEmail(registerRequest);
        // domain model
        Member member = Member.register(registerRequest, passwordEncoder);
        // repository
        memberRepository.save(member);
        // post process
        sendWelcomeEmail(member);
        return member;
    }

    @Override
    public Member activate(Long memberId) {
        Member member = memberFinder.find(memberId);

        member.activate();

        return memberRepository.save(member);
    }

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요.","아래 링크를 클릭해서 등록을 완료해주세요.");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if(memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()){
            throw new DuplicateEmailException("이미 사용중인 이메일 이니다" + registerRequest.email());
        };
    }
}

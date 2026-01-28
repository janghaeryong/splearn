package jhrspring.splearn.application;

import jakarta.transaction.Transactional;
import jhrspring.splearn.application.provided.MemberRegister;
import jhrspring.splearn.application.required.EmailSender;
import jhrspring.splearn.application.required.MemberRepository;
import jhrspring.splearn.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class MemberService implements MemberRegister {
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

    private void sendWelcomeEmail(Member member) {
        emailSender.send(member.getEmail(), "등록을 완료해주세요.","아래 링크를 클릭해서 등록을 완료해주세요.");
    }

    private void checkDuplicateEmail(MemberRegisterRequest registerRequest) {
        if(memberRepository.findByEmail(new Email(registerRequest.email())).isPresent()){
            throw new DuplicateEmailException("이미 사용중인 이메일 이니다" + registerRequest.email());
        };
    }
}

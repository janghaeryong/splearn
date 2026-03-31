package jhrspring.splearn.adapter.webapi;

import jakarta.validation.Valid;
import jhrspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import jhrspring.splearn.application.member.provided.MemberRegister;
import jhrspring.splearn.domain.member.Member;
import jhrspring.splearn.domain.member.MemberRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberApi {
    private final MemberRegister memberRegister;

    //register api -> /members POST
    @PostMapping("/api/members")
    public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request){
        Member member = memberRegister.register(request);
        return MemberRegisterResponse.of(member);
    }


    //activate api -> /members/{id}/activate POST
    //deactivate api -> /members/{id}/deactivate POST

}

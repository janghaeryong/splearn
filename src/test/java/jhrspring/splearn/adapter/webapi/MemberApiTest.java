package jhrspring.splearn.adapter.webapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import jhrspring.splearn.adapter.webapi.dto.MemberRegisterResponse;
import jhrspring.splearn.application.member.provided.MemberRegister;
import jhrspring.splearn.application.member.required.MemberRepository;
import jhrspring.splearn.domain.member.Member;
import jhrspring.splearn.domain.member.MemberFixture;
import jhrspring.splearn.domain.member.MemberRegisterRequest;
import jhrspring.splearn.domain.member.MemberStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.json.JsonPathValueAssert;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.io.UnsupportedEncodingException;
import java.util.function.Consumer;

import static jhrspring.splearn.AssertThatUtils.equalsTo;
import static jhrspring.splearn.AssertThatUtils.notNull;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional //
@RequiredArgsConstructor
public class MemberApiTest {
    final MockMvcTester mockMvcTester;
    final ObjectMapper objectMapper;

    final MemberRepository memberRepository;
    final MemberRegister memberRegister;
    @Test
    void testRegister() throws JsonProcessingException, UnsupportedEncodingException {
        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mockMvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON)
                .content(requestJson).exchange();

        assertThat(result)
                .hasStatusOk()
                .bodyJson().hasPathSatisfying("$.memberId", notNull())
                .hasPathSatisfying("$.email", equalsTo(request));

        MemberRegisterResponse response =
                objectMapper.readValue(result.getResponse().getContentAsString(), MemberRegisterResponse.class);

        Member member = memberRepository.findById(response.memberId()).orElseThrow();

        assertThat(member.getEmail().address()).isEqualTo(request.email());
        assertThat(member.getNickname()).isEqualTo(request.nickname());
        assertThat(member.getStatus()).isEqualTo(MemberStatus.PENDING);
    }

    @Test
    void duplicateEmail() throws JsonProcessingException {
        memberRegister.register(MemberFixture.createMemberRegisterRequest());

        MemberRegisterRequest request = MemberFixture.createMemberRegisterRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        MvcTestResult result = mockMvcTester.post().uri("/api/members").contentType(MediaType.APPLICATION_JSON).content(requestJson).exchange();
        assertThat(result)
                .apply(print())
                .hasStatus(HttpStatus.CONFLICT);
    }


}

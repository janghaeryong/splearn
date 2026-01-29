package jhrspring.splearn.adapter.integration;

import jhrspring.splearn.domain.Email;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DummyEmailSenderTest {
    @Test
    @StdIo
    void dummyEmailSender(StdOut out){
        DummyEmailSender dummyEmailSender = new DummyEmailSender();
        dummyEmailSender.send(new Email("jhr@naver.com"),"subject","body");

        assertThat(out.capturedLines()[0]).isEqualTo("DummyEmailSender email :Email[address=jhr@naver.com]");
    }
}
package jhrspring.splearn.domain.member;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jhrspring.splearn.domain.AbstractEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {
    @Embedded
    private Profile profile;

    private String introduction;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDateTime deactivatedAt;

    static MemberDetail create() {
        MemberDetail memberDetail = new MemberDetail();
        memberDetail.registeredAt = LocalDateTime.now();
        return memberDetail;
    }

    void activate() {
        Assert.isTrue(activatedAt == null, "이미 activatedAt 가 설정되어 있습니다.");

        this.activatedAt = LocalDateTime.now();
    }

    void deactivate(){
        Assert.isTrue(deactivatedAt == null, "이미 deactivatedAt 가 설정되어 있습니다.");

        this.deactivatedAt = LocalDateTime.now();
    }

    void updateInfo(MemberInfoUpdateRequest updateRequest) {
        this.profile = new Profile(updateRequest.profileAddress());
        this.introduction = Objects.requireNonNull(updateRequest.introduction());
    }
}


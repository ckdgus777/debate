package com.example.debate.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// TODO: 현재는 Auditing Class가 1개만 존재한다. 특정 테이블에서만 필요한 경우가 있는 컬럼들이다.
// TODO: 시간만 사용한다면, 따로 때서 그것만 사용할 수 있도록 할 수 있을 것이다.
// TODO: 인기도 부분은 Auditing과 연관이 없음. 단순히 중복 컬럼에 불과하다. 그렇다면, 해당 부분을 빼줄 수 있지 않을까?
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditingFields {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected LocalDateTime createdAt; // 생성일시

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    protected Long createdBy; // 생성자

    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime modifiedAt; // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    protected Long modifiedBy; // 수정자

    // 인기도
    protected Long like;
    protected Long dislike;
}

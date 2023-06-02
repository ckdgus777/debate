package com.example.debate.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Entity
public class DebateComment extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private DebateBoard debateBoard; // 게시글 (ID)

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "userAccount_id")
    private UserAccount userAccount;

    @Setter @Column(nullable = false, length = 1000) private String content; // 본문

    protected DebateComment() { }

    private DebateComment(DebateBoard debateBoard, UserAccount userAccount, String content) {
        this.debateBoard = debateBoard;
        this.userAccount = userAccount;
        this.content = content;
    }

    public static DebateComment of(DebateBoard debateBoard, UserAccount userAccount, String content) {
        return new DebateComment(debateBoard, userAccount, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DebateComment that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

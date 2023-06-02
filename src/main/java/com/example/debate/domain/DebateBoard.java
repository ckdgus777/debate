package com.example.debate.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class DebateBoard extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false) private String title;
    @Setter @Column(nullable = false, length = 5000) private String content;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "userAccount_id")
    private UserAccount userAccount;

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "debateBoard", cascade = CascadeType.ALL)
    @ToString.Exclude // 순환참조를 방지하기 위함. ToString에서 제외시킨다.
    private final Set<DebateComment> debateComments = new LinkedHashSet<>();

    protected DebateBoard() { }

    private DebateBoard(UserAccount userAccount, String title, String content) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    public static DebateBoard of(UserAccount userAccount, String title, String content) {
        return new DebateBoard(userAccount, title, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DebateBoard that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

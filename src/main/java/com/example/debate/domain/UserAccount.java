package com.example.debate.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "userId", unique = true),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt")
})
@Entity
public class UserAccount extends AuditingFields {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false, unique = true) private String userId;
    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(nullable = false, length = 100) private String userNm;
    @Setter @Column(nullable = false) private LocalDate birth;
    @Setter @Column(nullable = false) private String telephone;

    @Setter @Column(unique = true) private String email;
    @Setter private String description;

    protected UserAccount() { }

    private UserAccount(String userId,
                        String userPassword,
                        String userNm,
                        LocalDate birth,
                        String telephone,
                        String email,
                        String description) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userNm = userNm;
        this.birth = birth;
        this.telephone = telephone;
        this.email = email;
        this.description = description;
    }

    public static UserAccount of(String userId,
                                 String userPassword,
                                 String userNm,
                                 LocalDate birth,
                                 String telephone) {
        return UserAccount.of(userId, userPassword, userNm, birth, telephone, null, null);
    }

    public static UserAccount of(String userId,
                                String userPassword,
                                String userNm,
                                LocalDate birth,
                                String telephone,
                                String email,
                                String description) {
        return new UserAccount(userId, userPassword, userNm, birth, telephone, email, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

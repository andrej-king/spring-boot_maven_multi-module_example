package com.example.persistence.model.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.example.persistence.model.common.BaseModel;
import com.example.persistence.model.role.RoleModel;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_login", columnNames = "login"),
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")
})
public class UserModel extends BaseModel {
    public static final String TABLE_NAME = "users";

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private Boolean enabled = true;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_users_role"))
    private RoleModel role;

    private UUID refreshToken;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant refreshTokenExpire;

    @Builder
    public UserModel(String login, String email, String password, String fullName, RoleModel role) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
    }
}

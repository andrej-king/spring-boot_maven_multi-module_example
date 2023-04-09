package com.example.persistence.model.role;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import com.example.persistence.model.common.BaseModel;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "roles", uniqueConstraints = {
        @UniqueConstraint(name = "uk_roles_name", columnNames = "name")
})
public class RoleModel extends BaseModel {
    public static final String TABLE_NAME = "roles";

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleEnum name;
}

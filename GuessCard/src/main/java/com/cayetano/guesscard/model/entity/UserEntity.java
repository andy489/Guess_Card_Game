package com.cayetano.guesscard.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity(name = "uniqueUserEntity")
@Table(name = "users")
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class UserEntity extends GenericEntity {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Override
    public String toString() {
        return "UserEntity{" +
                "userName='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + (password != null ? "[PROVIDED]" : null) + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

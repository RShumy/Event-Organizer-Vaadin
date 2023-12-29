package com.eventorganizr.organizr.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@JsonIgnoreProperties({"participants","authorities"})
public class User {

    public User(){}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String userName;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean isActive;

    //Trying Lazy FetchType with ByteCode Enhancement plugin set-up referenced below:
    //https://vladmihalcea.com/the-best-way-to-lazy-load-entity-attributes-using-jpa-and-hibernate/

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(nullable = false)
    @JsonManagedReference(value = "participants")
    @ToString.Exclude
    Set<Participant> participants;

    @ManyToMany(targetEntity =Authority.class, cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = { @JoinColumn(name = "user_id", insertable = true, updatable = true) },
            inverseJoinColumns = { @JoinColumn(name = "authority_id", insertable = true, updatable = true) }
    )
    @JsonManagedReference(value = "authorities")
    Set<Authority> authorities = new HashSet<>();

    public User(String userName, String firstName, String lastName, String email, String password, boolean isActive) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
    }

    public void addAuthority(Authority authority){
        authorities.add(authority);
    }

    public void removeAuthority(Authority authority){
        authorities.remove(authority);
    }

}

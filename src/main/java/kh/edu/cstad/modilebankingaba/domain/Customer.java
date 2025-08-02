package kh.edu.cstad.modilebankingaba.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity

@Table(name ="customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique=true, length = 150)
    private String userName;
    @Column(unique=true, length = 15)
    private String phoneNumber;
    @Column( length = 10)
    private String gender;
    @Column(unique=true, length = 150)
    private String email;
    @Column( length = 15)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "customer")
    private List<Account> accounts;
}

package kh.edu.cstad.modilebankingaba.domain;


import jakarta.persistence.*;
        import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class KYC {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(unique = true, nullable = false, length = 12)
    private String nationalCodeId;

    @Column(nullable = false)
    private Boolean isVerified;

    @Column(nullable = false)
    private Boolean isDeleted;

    @OneToOne()
    @JoinColumn(name = "cust_id")
    private Customer customer;
}


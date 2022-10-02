package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
//@Table(name = "User")
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    private String username;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob
    private String description;

    // JPA는 Entity에 기본 생성자가 필요하다
    public Member() {
    }
}

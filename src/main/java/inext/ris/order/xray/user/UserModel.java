package inext.ris.order.xray.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "xray_user")
@AllArgsConstructor
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CODE", length = 50)
    private String code;
    @Column(name = "DF_CODE", length = 50)
    private String df_code;
    @Column(name = "LOGIN", length = 20)
    private String login;
    @Column(name = "NAME", length = 50)
    private String name;
    @Column(name = "LASTNAME", length = 50)
    private String lastname;
    @Column(name = "NAME_ENG", length = 50)
    private String name_eng;
    @Column(name = "LASTNAME_ENG", length = 50)
    private String lastname_eng;
    @Column(name = "USER_TYPE_CODE", length = 20)
    private String user_type_code;
    @Column(name = "PREFIX", length = 20)
    private String prefix;
    @Column(name = "PASSWORD", length = 100)
    private String password;
    @Column(name = "CENTER_CODE", length = 10)
    private String center_code;
    @Column(name = "CREATED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @CreationTimestamp
    private Date created_time;
    @Column(name = "SESSION", length = 100)
    private String session;
    @Column(name = "ENABLE", length = 1)
    private String enable;
    @Column(name = "ALL_CENTER", length = 1)
    private Integer all_center;
    @Column(name = "LOGINTIME")
    @Temporal(TemporalType.TIME)
    @JsonFormat(pattern="HH:mm:ss")
    private Date logintime;
    @Column(name = "TEXT_SIGNATURE", length = 200)
    private String text_signature;
    @Column(name = "PACS_LOGIN", length = 20)
    private String pacs_login;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserModel userModel = (UserModel) o;
        return getId() != null && Objects.equals(getId(), userModel.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

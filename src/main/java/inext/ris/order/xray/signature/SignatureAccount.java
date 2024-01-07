package inext.ris.order.xray.signature;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Table(name = "xray_signature_account")
@NoArgsConstructor
@AllArgsConstructor
public class SignatureAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_ID", length = 11)
    private Integer user_id;
    @Column(name = "USER_CODE", length = 255)
    private String user_code;
    @Column(name = "USER_HSM", length = 255)
    private String user_hsm;
    @Column(name = "PASS_HSM", length = 255)
    private String pass_hsm;
    @Column(name = "CTS", length = 255)
    private String cts;
    @Column(name = "PIN", length = 255)
    private String pin;
    @Column(name = "AVATAR", length = 255)
    private String avatar;
    @Column(name = "TEXT_SIGNATURE", length = 255)
    private String text_signature;
    @Column(name = "LAST_SIGN")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape=JsonFormat.Shape.STRING,pattern="yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Date last_sign;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        SignatureAccount that = (SignatureAccount) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}

package inext.ris.order.xray.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface UserRepository extends JpaRepository<UserModel, Long>{
    @Query(value="SELECT COUNT(id) > 0 FROM xray_user WHERE CODE = :code", nativeQuery = true)
    BigInteger existsByCode(@Param("code") String code);

    @Query(value="SELECT COUNT(id) > 0 FROM xray_user WHERE DF_CODE = :df_code", nativeQuery = true)
    BigInteger existsByDFCode(@Param("df_code") String df_code);

    @Query(value="SELECT COUNT(id) > 0 FROM xray_user WHERE LOGIN = :login", nativeQuery = true)
    BigInteger existsByLogin(@Param("login") String login);

    @Query(value="SELECT * FROM xray_user WHERE CODE = :code", nativeQuery = true)
    UserModel getUserByCode(@Param("code") String code);

    @Query(value="SELECT * FROM xray_user WHERE DF_CODE = :df_code", nativeQuery = true)
    UserModel getUserByDFCode(@Param("df_code") String df_code);

    @Query(value="SELECT * FROM xray_user WHERE LOGIN = :login", nativeQuery = true)
    UserModel getUserByLogin(@Param("login") String login);
}

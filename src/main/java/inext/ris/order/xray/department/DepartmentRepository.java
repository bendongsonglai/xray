package inext.ris.order.xray.department;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DepartmentRepository extends JpaRepository<DepartmentModel, Long>{
    @Query(value="SELECT COUNT(id) > 0 FROM xray_department WHERE NAME_VIE = :name", nativeQuery = true)
    BigInteger existsByNAME_VIE(@Param("name") String name);

    @Query(value="SELECT COUNT(id) > 0 FROM xray_department WHERE DEPARTMENT_ID = :department_id", nativeQuery = true)
    BigInteger existsByDepartment_id(@Param("department_id") String department_id);

    @Query(value="SELECT * FROM xray_department WHERE DEPARTMENT_ID = :department_id", nativeQuery = true)
    DepartmentModel getDepartmentByDepartment_id(@Param("department_id") String department_id);

    @Query(value="SELECT * FROM xray_department WHERE NAME_VIE = :name", nativeQuery = true)
    DepartmentModel getDepartmentByNAME_VIE(@Param("name") String name);
}

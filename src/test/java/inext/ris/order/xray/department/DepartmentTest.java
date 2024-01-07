package inext.ris.order.xray.department;

import inext.ris.order.xray.BKRISJpaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {BKRISJpaConfig.class})
@ActiveProfiles("dev")
public class DepartmentTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private DepartmentRepository departmentRepository;
    @Test
    public void should_find_department_repository_is_empty() {
        Iterable<DepartmentModel> departments = departmentRepository.findAll();
        assertThat(departments).isEmpty();
    }

    @Test
    public void should_store_a_department() {
        DepartmentModel newDepartment = departmentRepository.save(new DepartmentModel((long) 0, "BKRIS", "0000", "Khoa cấp cứu",
                "Khoa cap cuu", "BKRIS"));
        Optional<DepartmentModel> op = departmentRepository.findById(newDepartment.getId());
        assertThat(op.isPresent()).isTrue();
    }
}

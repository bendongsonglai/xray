package inext.ris.order.xray.department;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service

@RequiredArgsConstructor
public class DepartmentService {
	@Autowired
	DepartmentRepository departmentRepository;
	
	public List<DepartmentModel> findAll() {
		return departmentRepository.findAll();
	}
	
    public Optional<DepartmentModel> findById(Long id) {
    	return departmentRepository.findById(id);
    }
    
    public BigInteger existsByNAME_VIE(String name) {
    	return departmentRepository.existsByNAME_VIE(name);
    }

    public BigInteger existsByDepartment_id(String department_id) {
        return departmentRepository.existsByDepartment_id(department_id);
    }

    public DepartmentModel getDepartmentByNAME_VIE(String name) {
    	return departmentRepository.getDepartmentByNAME_VIE(name);
    }

    public DepartmentModel getDepartmentByDepartment_id(String department_id) {
        return departmentRepository.getDepartmentByDepartment_id(department_id);
    }

    public DepartmentModel getOne(Long id) {
    	return departmentRepository.getOne(id);
    }
    
    public DepartmentModel save(DepartmentModel referrer) {
        return departmentRepository.save(referrer);
    }
       
    public void deleteById(Long id) {
    	departmentRepository.deleteById(id);
    }
}

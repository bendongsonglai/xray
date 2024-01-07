package inext.ris.order.xray.user;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	@Autowired
	UserRepository userRepository;
	
	public List<UserModel> findAll() {
		return userRepository.findAll();
	}
	
    public Optional<UserModel> findById(Long id) {
    	return userRepository.findById(id);
    }

    public BigInteger existsByCode(String code) {
        return userRepository.existsByCode(code);
    }

    public BigInteger existsByDFCode(String df_code) {
        return userRepository.existsByDFCode(df_code);
    }

    public BigInteger existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
    
    public UserModel getOne(Long id) {
    	return userRepository.getOne(id);
    }

    public UserModel getUserByCode(String code) {
        return userRepository.getUserByCode(code);
    }

    public UserModel getUserByDFCode(String df_code) {
        return userRepository.getUserByDFCode(df_code);
    }

    public UserModel getUserByLogin(String login) {
        return userRepository.getUserByLogin(login);
    }

    public UserModel save(UserModel patient) {
        return userRepository.save(patient);
    }
       
    public void deleteById(Long id) {
    	userRepository.deleteById(id);
    }
}

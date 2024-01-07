package inext.ris.order.xray.user;

import java.math.BigInteger;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/user")
@Slf4j
@RequiredArgsConstructor
public class UserAPI {
	@Autowired
	UserService userService;

    @RequestMapping(value="/",method = RequestMethod.GET)
    public ResponseEntity<List<UserModel>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    @PostMapping
    public ResponseEntity<UserModel> create(@Valid @RequestBody UserModel user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserModel> findById(@PathVariable Long id) {
        Optional<UserModel> type = userService.findById(id);
        if (!type.isPresent()) {
            log.error("User Id " + id + " is not existed");
          return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(type.get());
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<UserModel> findByCode(@PathVariable String code) {
        byte[] decodedBytes = Base64.getDecoder().decode(code);
        String decodedString = new String(decodedBytes);
        UserModel user = userService.getUserByCode(decodedString);
        BigInteger bi = new BigInteger("0");
        int res;
        res = userService.existsByCode(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("User Code " + decodedString + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/dfcode/{code}")
    public ResponseEntity<UserModel> findByDFCode(@PathVariable String code) {
        byte[] decodedBytes = Base64.getDecoder().decode(code);
        String decodedString = new String(decodedBytes);
        BigInteger bi = new BigInteger("0");
        int res;
        res = userService.existsByDFCode(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("User DF_Code " + decodedString + " is not existed");
            return ResponseEntity.noContent().build();
        }
        UserModel user = userService.getUserByDFCode(decodedString);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/login")
    public ResponseEntity<UserModel> findBylogin(@RequestParam(required = false) String username) {
        UserModel user = userService.getUserByLogin(username);
        BigInteger bi = new BigInteger("0");
        int res;
        res = userService.existsByLogin(username).compareTo(bi);
        if (res <= 0) {
            log.error("User Login " + username + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserModel> update(@PathVariable Long id, @Valid @RequestBody UserModel user) {
        if (!userService.findById(id).isPresent()) {
            log.error("Type Id " + id + " is not existed");
           return ResponseEntity.badRequest().build();
        }
        UserModel userUpdate = userService.getOne(id);
        userUpdate.setCode(user.getCode());
        userUpdate.setDf_code(user.getDf_code());
        userUpdate.setLogin(user.getLogin());
        userUpdate.setName(user.getName());
        userUpdate.setLastname(user.getLastname());
        userUpdate.setName_eng(user.getName_eng());
        userUpdate.setLastname_eng(user.getLastname_eng());
        userUpdate.setUser_type_code(user.getUser_type_code());
        userUpdate.setPrefix(user.getPrefix());
        userUpdate.setPassword(user.getPassword());
        userUpdate.setCenter_code(user.getCenter_code());
        userUpdate.setSession(user.getSession());
        userUpdate.setEnable(user.getEnable());
        userUpdate.setAll_center(user.getAll_center());
        userUpdate.setLogintime(user.getLogintime());
        userUpdate.setText_signature(user.getText_signature());
        userUpdate.setPacs_login(user.getPacs_login());
        return ResponseEntity.ok(userService.save(userUpdate));
    }

    @GetMapping("/check/{code}")
    public Boolean checkCode(@PathVariable String code) {
        byte[] decodedBytes = Base64.getDecoder().decode(code);
        String decodedString = new String(decodedBytes);
        BigInteger bi = new BigInteger("0");
        int res;
        res = userService.existsByCode(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("User with code " + decodedString + " is not existed");
            return false;
        }
        return true;
    }

    @GetMapping("/checkdf/{code}")
    public Boolean checkDF(@PathVariable String code) {
        byte[] decodedBytes = Base64.getDecoder().decode(code);
        String decodedString = new String(decodedBytes);
        BigInteger bi = new BigInteger("0");
        int res;
        res = userService.existsByDFCode(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("User with df_code " + decodedString + " is not existed");
            return false;
        }
        return true;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!userService.findById(id).isPresent()) {
            log.error("Type Id " + id + " is not existed");
           return ResponseEntity.badRequest().build();
        }
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

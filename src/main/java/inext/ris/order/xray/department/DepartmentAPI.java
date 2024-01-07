package inext.ris.order.xray.department;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import inext.ris.order.xray.his.model.LocationData;
import inext.ris.order.xray.localService.WebClientConfig;
import inext.ris.order.xray.localService.WebServiceHapi;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/department")
@Slf4j
@RequiredArgsConstructor
public class DepartmentAPI {
    @Autowired
    DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<List<DepartmentModel>> findAll() {
        return ResponseEntity.ok(departmentService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody DepartmentModel department) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = departmentService.existsByNAME_VIE(department.getName_vie()).compareTo(bi);
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceHapi webServiceHapi = new WebServiceHapi(webClientConfig.hapiApiClient());
        if (res > 0) {
            log.error("NAME_VIE " + department.getName_vie() + " existed");
            try {
                String code = URLEncoder.encode(Base64.getEncoder().encodeToString(department.getName_vie().getBytes()));
                Boolean checkLocation = webServiceHapi.checkLocation(code);
                if (!checkLocation) {
                    LocationData locationData = new LocationData();
                    String makp = Base64.getEncoder().encodeToString(department.getName_vie().getBytes());
                    String tenkp = department.getName_vie();
                    String organizationCode = "00000000";
                    locationData.setMakp(makp);
                    locationData.setTenkp(tenkp);
                    locationData.setOrganizationCode(organizationCode);
                    String locationUUID = webServiceHapi.createLocation(locationData);
                    log.info("Update Department {}!", locationUUID);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error update Department {}", department.getName_vie());
            }
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            try {
                String code = URLEncoder.encode(Base64.getEncoder().encodeToString(department.getName_vie().getBytes()));
                Boolean checkLocation = webServiceHapi.checkLocation(code);
                if (!checkLocation) {
                    LocationData locationData = new LocationData();
                    String makp = Base64.getEncoder().encodeToString(department.getName_vie().getBytes());
                    String tenkp = department.getName_vie();
                    String organizationCode = "00000000";
                    locationData.setMakp(makp);
                    locationData.setTenkp(tenkp);
                    locationData.setOrganizationCode(organizationCode);
                    String locationUUID = webServiceHapi.createLocation(locationData);
                    log.info("Create Department!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Error create Department {}", department.getName_vie());
            }
        }
        return ResponseEntity.ok(departmentService.save(department));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentModel> findById(@PathVariable Long id) {
        Optional<DepartmentModel> department = departmentService.findById(id);
        if (!department.isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(department.get());
    }

    @GetMapping("/check/{department_id}")
    public Boolean checkDepartment(@PathVariable String department_id) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = departmentService.existsByDepartment_id(department_id).compareTo(bi);
        if (res <= 0) {
            log.error("Department " + department_id + " is not existed");
            return false;
        }
        return true;
    }

    @GetMapping("/checkname")
    public Boolean checkDepartmentByName(@RequestParam String name) {
        log.info("Name encode: {}", name);
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(name));
        String decodedString = new String(decodedBytes);
        BigInteger bi = new BigInteger("0");
        int res;
        res = departmentService.existsByNAME_VIE(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("Department Name " + decodedString + " is not existed");
            return false;
        }
        return true;
    }

    @GetMapping("/name_vi")
    public ResponseEntity<DepartmentModel> findByPrac(@RequestParam String name) {
        log.info("Name encode: {}", name);
        byte[] decodedBytes = Base64.getDecoder().decode(Urldecoder(name));
        String decodedString = new String(decodedBytes);
        DepartmentModel referrer = departmentService.getDepartmentByNAME_VIE(decodedString);
        BigInteger bi = new BigInteger("0");
        int res;
        res = departmentService.existsByNAME_VIE(decodedString).compareTo(bi);
        if (res <= 0) {
            log.error("NAME_VIE " + decodedString + " is not existed");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(referrer);
    }

    @GetMapping("/depart")
    public ResponseEntity<DepartmentModel> findByDepartmentID(@RequestParam String depart_id) {
        try {
            DepartmentModel department = departmentService.getDepartmentByDepartment_id(depart_id);
            return ResponseEntity.ok(department);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error get Department ID");
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<DepartmentModel> update(@PathVariable Long id, @Valid @RequestBody DepartmentModel department) {
        if (!departmentService.findById(id).isPresent()) {
            log.error("Department Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }
        DepartmentModel departmentModel = departmentService.getOne(id);
        departmentModel.setCenter(department.getCenter());
        departmentModel.setName_vie(department.getName_vie());
        departmentModel.setName_eng(department.getName_eng());
        departmentModel.setType(department.getType());
        return ResponseEntity.ok(departmentService.save(departmentModel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!departmentService.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        departmentService.deleteById(id);

        return ResponseEntity.ok().build();
    }

    private static String Urldecoder(String name) {
        String newName = "";
        try {
            newName = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return newName;
    }

    public static void main(String[] args) {
        /*String name = "UDRLLiBLaG9hIE5nb%2BG6oWkgVGnDqnUgaMOzYQ%3D%3D";
        String newName = "";
        try {
            newName = URLDecoder.decode(name, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] decodedBytes = Base64.getDecoder().decode(newName);
        String decodedString = new String(decodedBytes);
        System.out.println(decodedString);*/
        WebClientConfig webClientConfig = new WebClientConfig();
        WebServiceHapi webServiceHapi = new WebServiceHapi(webClientConfig.hapiApiClient());
        LocationData locationData = new LocationData();
        String makp = Base64.getEncoder().encodeToString("Khoa Ngoại Thần kinh".getBytes());
        String tenkp = "Khoa Ngoại Thần kinh";
        String organizationCode = "0301261735";
        locationData.setMakp(makp);
        locationData.setTenkp(tenkp);
        locationData.setOrganizationCode(organizationCode);
        String locationUUID = webServiceHapi.createLocation(locationData);
        log.info("Update Department {}!", locationUUID);
    }
}

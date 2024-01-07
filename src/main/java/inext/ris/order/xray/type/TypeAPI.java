package inext.ris.order.xray.type;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/type")
@Slf4j
@RequiredArgsConstructor
public class TypeAPI {
    @Autowired
    TypeService typeService;

    @GetMapping
    public ResponseEntity<List<TypeModel>> findAll() {
        return ResponseEntity.ok(typeService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody TypeModel type) {
        return ResponseEntity.ok(typeService.save(type));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeModel> findById(@PathVariable Long id) {
        Optional<TypeModel> type = typeService.findById(id);
        if (!type.isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(type.get());
    }

    @GetMapping("/get/{modality}")
    public ResponseEntity<?> findById(@PathVariable String modality) {
        TypeModel typeModel;
        try {
            typeModel = typeService.FindXrayType(modality);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Not Exist Xray Type match!!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<TypeModel>(typeModel, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<TypeModel> update(@PathVariable Long id, @Valid @RequestBody TypeModel type) {
        if (!typeService.findById(id).isPresent()) {
            log.error("Type Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }
        TypeModel typeUpdate = typeService.getOne(id);
        typeUpdate.setType_name(type.getType_name());
        typeUpdate.setType_name_eng(type.getType_name_eng());
        typeUpdate.setMod_type(type.getMod_type());
        return ResponseEntity.ok(typeService.save(typeUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!typeService.findById(id).isPresent()) {
            log.error("Type Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        typeService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}

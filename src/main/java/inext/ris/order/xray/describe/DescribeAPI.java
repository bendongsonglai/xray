package inext.ris.order.xray.describe;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/describe")
@Slf4j
@RequiredArgsConstructor
public class DescribeAPI {
	@Autowired
	DescribeService describeService;
	
	@GetMapping
    public ResponseEntity<List<DescribeModel>> findAll() {
        return ResponseEntity.ok(describeService.findAll());
    }
	
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody DescribeModel describe) {
        return ResponseEntity.ok(describeService.save(describe));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DescribeModel> findById(@PathVariable Long id) {
        Optional<DescribeModel> code = describeService.findById(id);
        if (!code.isPresent()) {
            log.error("Describe Id " + id + " does not existed");
          return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(code.get());
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<DescribeModel> update(@PathVariable Long id, @Valid @RequestBody DescribeModel describe) {
        if (!describeService.findById(id).isPresent()) {
            log.error("Describe Id " + id + " does not existed");
           return ResponseEntity.notFound().build();
        }
    DescribeModel describeUpdate = describeService.getOne(id);
    describeUpdate.setId(id);
    describeUpdate.setUser_id(describe.getUser_id());
    describeUpdate.setTerm(describe.getTerm());
    describeUpdate.setValue(describe.getValue());
    describeUpdate.setStatus(describe.getStatus());
        return ResponseEntity.ok(describeService.save(describeUpdate));
    }
    
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!describeService.findById(id).isPresent()) {
            log.error("Describe Id " + id + " does not existed");
            ResponseEntity.badRequest().build();
        }

        describeService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}

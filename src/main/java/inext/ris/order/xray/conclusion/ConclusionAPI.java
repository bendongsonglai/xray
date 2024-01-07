package inext.ris.order.xray.conclusion;

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
@RequestMapping("/api/v1/conclusion")
@Slf4j
@RequiredArgsConstructor
public class ConclusionAPI {
	@Autowired
	ConclusionService conclusionService;
	
	@GetMapping
    public ResponseEntity<List<ConclusionModel>> findAll() {
        return ResponseEntity.ok(conclusionService.findAll());
    }
	
    @PostMapping
    public ResponseEntity create(@Valid @RequestBody ConclusionModel conclusion) {
        return ResponseEntity.ok(conclusionService.save(conclusion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConclusionModel> findById(@PathVariable Long id) {
        Optional<ConclusionModel> code = conclusionService.findById(id);
        if (!code.isPresent()) {
            log.error("Conclusion Id " + id + " does not existed");
          return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(code.get());
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ConclusionModel> update(@PathVariable Long id, @Valid @RequestBody ConclusionModel conclusion) {
        if (!conclusionService.findById(id).isPresent()) {
            log.error("Conclusion Id " + id + " does not existed");
           return ResponseEntity.notFound().build();
        }
    ConclusionModel conclusionUpdate = conclusionService.getOne(id);
    conclusionUpdate.setId(id);
    conclusionUpdate.setUser_id(conclusion.getUser_id());
    conclusionUpdate.setTerm(conclusion.getTerm());
    conclusionUpdate.setValue(conclusion.getValue());
    conclusionUpdate.setStatus(conclusion.getStatus());
        return ResponseEntity.ok(conclusionService.save(conclusionUpdate));
    }
    
    
    
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!conclusionService.findById(id).isPresent()) {
            log.error("Conclusion Id " + id + " does not existed");
            ResponseEntity.badRequest().build();
        }

        conclusionService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}

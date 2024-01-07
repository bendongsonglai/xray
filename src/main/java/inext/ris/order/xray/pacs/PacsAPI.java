package inext.ris.order.xray.pacs;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/pacs")
@Slf4j
@RequiredArgsConstructor
public class PacsAPI {
    @Autowired
    PacsService pacsService;

    @GetMapping
    public ResponseEntity<List<PacsModel>> findAll() {
        return ResponseEntity.ok(pacsService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody PacsModel pacs) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = pacsService.existsByAetitle(pacs.getAetitle()).compareTo(bi);
        if (res > 0) {
            log.error("Aetitle " + pacs.getAetitle() + " is existed");
            ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(pacsService.save(pacs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacsModel> findById(@PathVariable Long id) {
        Optional<PacsModel> pacs = pacsService.findById(id);
        if (!pacs.isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(pacs.get());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PacsModel> update(@PathVariable Long id, @Valid @RequestBody PacsModel pacs) {
        if (!pacsService.findById(id).isPresent()) {
            log.error("Pacs Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }
        PacsModel pacsUpdate = pacsService.getOne(id);
        pacsUpdate.setHost(pacs.getHost());
        pacsUpdate.setPort(pacs.getPort());
        pacsUpdate.setHl7port(pacs.getHl7port());
        pacsUpdate.setWadoport(pacs.getWadoport());
        pacsUpdate.setWadocontext(pacs.getWadocontext());
        pacsUpdate.setLocal_archive_ris(pacs.getLocal_archive_ris());

        return ResponseEntity.ok(pacsService.save(pacsUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!pacsService.findById(id).isPresent()) {
            log.error("Id " + id + " is not existed");
            ResponseEntity.badRequest().build();
        }

        pacsService.deleteById(id);

        return ResponseEntity.ok().build();
    }
}

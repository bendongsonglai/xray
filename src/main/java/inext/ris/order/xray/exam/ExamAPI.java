package inext.ris.order.xray.exam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/exam")
@Slf4j
@RequiredArgsConstructor
public class ExamAPI {
    @Autowired
    ExamService examService;

    @GetMapping
    public ResponseEntity<List<Exam>> findAll() {
        return ResponseEntity.ok(examService.findAll());
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody Exam exam) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = examService.existsByAccession(exam.getAccession()).compareTo(bi);
        if (res > 0) {
            log.error("ACCESSION " + exam.getAccession() + " does existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(examService.save(exam));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> findById(@PathVariable Long id) {
        Optional<Exam> request = examService.findById(id);
        if (!request.isPresent()) {
            log.error("Exam Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(request.get());
    }

    @GetMapping("/accession/{acc}")
    public ResponseEntity<Exam> findByAccession(@PathVariable String acc) {
        Exam request = examService.getExamByAcc(acc);
        if (request.getAccession().isEmpty()) {
            log.error("Exam ACCESSION " + acc + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(request);
    }

    @GetMapping("/check/{acc}")
    public Boolean checkExam(@PathVariable String acc) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = examService.existsByAccession(acc).compareTo(bi);
        if (res <= 0) {
            log.error("Accession " + acc + " is not existed");
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/toexam", method = RequestMethod.POST)
    public ResponseEntity<String> toExam(@Valid @RequestBody ToExam toExam) {
        String src = toExam.getSource();
        String destination = toExam.getDestination();
        try {
            moveFile(src, destination);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error move to Exam!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Move to Exam!", HttpStatus.OK);
    }

    @RequestMapping(value = "/accession/{acc}", method = RequestMethod.PUT)
    public ResponseEntity<Exam> update(@PathVariable String acc, @Valid @RequestBody Exam exam) {
        BigInteger bi = new BigInteger("0");
        int res;
        res = examService.existsByAccession(acc).compareTo(bi);
        if (res <= 0) {
            log.error("Accession " + acc + " is not existed");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        Exam examUpdate = examService.getExamByAcc(acc);
        examUpdate.setAccession(exam.getAccession());
        examUpdate.setDirectory(exam.getDirectory());
        examUpdate.setStatus(exam.getStatus());
        examUpdate.setLast_status(exam.getLast_status());
        examUpdate.setCreated(null);
        examUpdate.setLast_update(exam.getCreated());
        examUpdate.setVersion(exam.getVersion() + 1);
        log.info("Update Exam by acc {} Success!", exam.getAccession());
        return ResponseEntity.ok(examService.save(examUpdate));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Exam> update(@PathVariable Long id, @Valid @RequestBody Exam exam) {
        if (!examService.findById(id).isPresent()) {
            log.error("Exam Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        Exam examUpdate = examService.getOne(id);
        examUpdate.setAccession(exam.getAccession());
        examUpdate.setDirectory(exam.getDirectory());
        examUpdate.setStatus(exam.getStatus());
        examUpdate.setLast_status(exam.getLast_status());
        examUpdate.setCreated(null);
        examUpdate.setLast_update(exam.getCreated());
        examUpdate.setVersion(exam.getVersion() + 1);
        log.info("Update Exam {} Success!", exam.getAccession());
        return ResponseEntity.ok(examService.save(examUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        if (!examService.findById(id).isPresent()) {
            log.error("Exam Id " + id + " does not existed");
            return ResponseEntity.badRequest().build();
        }
        examService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    private static void moveFile(String source, String destination) {
        try {
            Path temp = Files.move
                    (Paths.get(source),
                            Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);

            if (temp != null) {
                System.out.println("File renamed and moved successfully");
            } else {
                System.out.println("Failed to move the file");
            }
        } catch (Exception e) {
            log.error("Move file {} error!");
            e.printStackTrace();
        }
    }

}

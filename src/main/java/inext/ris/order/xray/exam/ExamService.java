package inext.ris.order.xray.exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ExamService {
    @Autowired
    ExamRepository examRepository;

    public List<Exam> findAll() {
        return examRepository.findAll();
    }

    public Optional<Exam> findById(Long id) {
        return examRepository.findById(id);
    }

    public Exam getOne(Long id) {
        return examRepository.getOne(id);
    }

    public BigInteger existsByAccession(String acc) {
        return examRepository.existsByAccNo(acc);
    }


    public Exam getExamByAcc (String accNo) {
        return examRepository.getExamByAcc(accNo);
    }

    public Exam save(Exam exam) {
        return examRepository.save(exam);
    }

    public void deleteById(Long id) {
        examRepository.deleteById(id);
    }
}

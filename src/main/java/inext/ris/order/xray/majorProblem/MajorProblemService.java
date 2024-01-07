package inext.ris.order.xray.majorProblem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class MajorProblemService {
    @Autowired
    MajorProblemRepository majorProblemRepository;

    public List<MajorProblem> findAll() {
        return majorProblemRepository.findAll();
    }

    public Optional<MajorProblem> findById(Long id) {
        return majorProblemRepository.findById(id);
    }

    public MajorProblem getOne(Long id) {
        return majorProblemRepository.getOne(id);
    }

    public BigInteger existsByName(String name) {
        return majorProblemRepository.existsByName(name);
    }

    public MajorProblem majorProblemByName(String name) {
        return majorProblemRepository.majorProblemByName(name);
    }

    public List<MajorProblem> clinicalElementsEnabled() {
        return majorProblemRepository.majorProblemsEnabled();
    }

    public void removeMajorProblemByName(String name) {
        majorProblemRepository.removeMajorProblemByName(name);
    }

    public MajorProblem save(MajorProblem clinicalElement) {
        return majorProblemRepository.save(clinicalElement);
    }

    public void deleteById(Long id) {
        majorProblemRepository.deleteById(id);
    }
}

package build.dream.learning.repositories;

import build.dream.learning.domains.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, BigInteger> {
    Optional<Branch> findById(BigInteger id);
}

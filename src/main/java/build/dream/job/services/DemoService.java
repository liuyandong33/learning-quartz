package build.dream.job.services;

import build.dream.common.api.ApiRest;
import build.dream.job.domains.Branch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.UUID;

@Service
public class DemoService {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public ApiRest obtainBranch() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Branch> criteriaQuery = criteriaBuilder.createQuery(Branch.class);
        Root<Branch> root = criteriaQuery.from(Branch.class);
        Predicate idEqualPredicate = criteriaBuilder.equal(root.get("id"), 1);
        Predicate nameEqualPredicate = criteriaBuilder.like(root.get("lastUpdateRemark"), "%%");
        criteriaQuery.where(criteriaBuilder.and(idEqualPredicate, nameEqualPredicate, criteriaBuilder.or(idEqualPredicate, nameEqualPredicate)));
        Branch branch = entityManager.createQuery(criteriaQuery).getSingleResult();

        return ApiRest.builder().data(branch).message(UUID.randomUUID().toString()).build();
    }
}

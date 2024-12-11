package ksiazkopol.library.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import ksiazkopol.library.reader.Reader;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ReaderSearchRequestRepository {

    private final EntityManager entityManager;

    public ReaderSearchRequestRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Reader> findByCriteria(ReaderSearchRequest readerSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reader> criteriaQuery = criteriaBuilder.createQuery(Reader.class);
        List<Predicate> predicates = new ArrayList<>();
        Root<Reader> root = criteriaQuery.from(Reader.class);

        if (readerSearchRequest.getName() != null) {
            Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + readerSearchRequest.getName() + "%");
            predicates.add(namePredicate);
        }
        if (readerSearchRequest.getSurname() != null) {
            Predicate surnamePredicate = criteriaBuilder.like(root.get("surname"), "%" + readerSearchRequest.getSurname() + "%");
            predicates.add(surnamePredicate);
        }

        criteriaQuery.where(
                criteriaBuilder.or((predicates.toArray(predicates.toArray(new Predicate[0]))))
        );

        TypedQuery<Reader> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}

package ksiazkopol.library.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import ksiazkopol.library.book.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BookSearchRequestRepository {

    private final EntityManager entityManager;
    public BookSearchRequestRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Book> findByCriteria(BookSearchRequest bookSearchRequest) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        List<Predicate> predicates = new ArrayList<>();
        Root<Book> root = criteriaQuery.from(Book.class);

        if (bookSearchRequest.getTitle() != null) {
            Predicate titlePredicate = criteriaBuilder.like(root.get("title"), "%" + bookSearchRequest.getTitle() + "%");
            predicates.add(titlePredicate);
        }
        if (bookSearchRequest.getAuthor() != null) {
            Predicate authorPredicate = criteriaBuilder.like(root.get("author"), "%" + bookSearchRequest.getAuthor() + "%");
            predicates.add(authorPredicate);
        }
        if (bookSearchRequest.getGenre() != null) {
            Predicate genrePredicate = criteriaBuilder.like(root.get("genre"), "%" + bookSearchRequest.getGenre() + "%");
            predicates.add(genrePredicate);
        }
        if (bookSearchRequest.getPublisher() != null) {
            Predicate publisherPredicate = criteriaBuilder.like(root.get("publisher"), "%" + bookSearchRequest.getPublisher() + "%");
            predicates.add(publisherPredicate);
        }
        if (bookSearchRequest.getPublicationDate() != null) {
            Predicate datePredicate = criteriaBuilder.equal(root.get("publicationDate"), bookSearchRequest.getPublicationDate());
            predicates.add(datePredicate);
        }

        if (bookSearchRequest.getPublicationYear() != null) {
            Expression<Integer> year = criteriaBuilder.function("date_part", Integer.class, criteriaBuilder.literal("YEAR"), root.get("publicationDate"));
            Predicate yearPredicate = criteriaBuilder.equal(year, bookSearchRequest.getPublicationYear());
            predicates.add(yearPredicate);
        }
        if (bookSearchRequest.getISBN() != null) {
            Predicate ISBNPredicate = criteriaBuilder.equal(root.get("ISBN"), bookSearchRequest.getISBN());
            predicates.add(ISBNPredicate);
        }


        criteriaQuery.where(
                criteriaBuilder.or((predicates.toArray(predicates.toArray(new Predicate[0]))))
        );

        TypedQuery<Book> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}

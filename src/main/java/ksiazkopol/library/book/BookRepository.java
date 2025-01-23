package ksiazkopol.library.book;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, PagingAndSortingRepository<Book, Long> {


    @Query("select b from Book b where b.reader.id = :readerId ")
    List<Book> showAllBooksByReader(@Param("readerId") Long readerId, Sort sort);

    @Query("select count(b) from Book b")
    long countAllBooks();

    @Query("select count(b) from Book b where b.borrowDate is not null and b.reader is not null")
    long countBorrowedBooks();

    @Query("select count(b) from Book b where b.reader is null and b.borrowDate is null and b.expectedReturnDate is null")
    long countBooksAvailableToBorrow();


}

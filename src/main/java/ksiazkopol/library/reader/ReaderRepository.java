package ksiazkopol.library.reader;

import ksiazkopol.library.book.Book;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {

}

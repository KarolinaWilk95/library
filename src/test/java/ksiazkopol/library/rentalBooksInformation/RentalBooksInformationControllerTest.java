package ksiazkopol.library.rentalBooksInformation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RentalBooksInformationControllerTest {

    @Mock
    RentalBooksInformationService rentalBooksInformationService;

    @InjectMocks
    RentalBooksInformationController rentalBooksInformationController;

    @Test
    void showAllRecords() {
        //given
        List<RentalBooksInformation> rentalBooksInformationList = new ArrayList<>();

        when(rentalBooksInformationService.showAllRecords()).thenReturn(rentalBooksInformationList);

        //when
        var result = rentalBooksInformationController.showAllRecords();

        //then
        verify(rentalBooksInformationService).showAllRecords();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(rentalBooksInformationList);
    }
}
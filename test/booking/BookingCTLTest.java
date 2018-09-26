package hotel.test.booking;

import hotel.HotelHelper;
import hotel.booking.BookingCTL;
import hotel.booking.BookingUI;
import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
class BookingCTLTest {

    @Mock
    private Hotel hotel;
    @Mock
    CreditCard card;
    @Mock
    BookingUI mockBookingUI;
    @Mock
    Guest mockGuest;
    @Mock
    Room mockRoom;
    @Mock
    CreditAuthorizer authorizer;
    @InjectMocks
    private BookingCTL bookingCTL;
    Date date;
    Date anotherDate;

    @BeforeEach
    void setUp() throws Exception {
        bookingCTL = new BookingCTL(hotel);
    }


    @Test
    public void testCreditDetailsEnteredIsNotAuthorized() {
        bookingCTL.phoneNumberEntered(2);
        bookingCTL.roomTypeAndOccupantsEntered(RoomType.SINGLE, 1);
        bookingCTL.bookingTimesEntered(date, 5);
        bookingCTL.creditDetailsEntered(CreditCardType.MASTERCARD, 6, 123);
    }


    @Test
    public void testCreditDetailsEnteredWhenStateIsNotCredit() {
        Executable e = () -> bookingCTL.creditDetailsEntered(CreditCardType.MASTERCARD, 1234, 231);
        assertThrows(RuntimeException.class, e);
    }

    @org.junit.jupiter.api.Test
    void creditDetailsEntered() {

    }

}
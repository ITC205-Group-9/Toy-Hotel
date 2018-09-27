package hotel.test.booking;

import hotel.booking.BookingCTL;
import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
class BookingCTLTest {

    @Mock Hotel mockHotel;
    @Mock CreditAuthorizer mockCreditAuthorizer;
    Date date;

    @InjectMocks
    BookingCTL bookingCTL;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        date = format.parse("10-12-2018");
        cal.setTime(date);
    }

    @Test
    public void testCreditDetailsEntered() {
        when(mockHotel.isRegistered(anyInt())).thenReturn(true);
        when(mockHotel.findGuestByPhoneNumber(anyInt())).thenReturn(new Guest("Steven", "Marrickville", 2));
        bookingCTL.phoneNumberEntered(anyInt());
        bookingCTL.roomTypeAndOccupantsEntered(RoomType.TWIN_SHARE, 2);
        when(mockHotel.findAvailableRoom(any(RoomType.class), any(Date.class), anyInt())).thenReturn(new Room(301, RoomType.TWIN_SHARE));
        bookingCTL.bookingTimesEntered(date, 2);
        when(mockCreditAuthorizer.authorize(any(CreditCard.class), anyDouble())).thenReturn(true);
        when(mockHotel.book(any(Room.class), any(Guest.class), any(Date.class), anyInt(), anyInt(), any(CreditCard.class))).thenReturn(Long.parseLong("20181012301"));
        bookingCTL.creditDetailsEntered(CreditCardType.MASTERCARD, 1, 1);
    }


    @Test
    public void testCreditDetailsEnteredThrowExceptionWhenTheStateIsNotCredit() {
        Executable e = () -> bookingCTL.creditDetailsEntered(CreditCardType.MASTERCARD, 1, 321);
        assertThrows(RuntimeException.class, e);
    }
}
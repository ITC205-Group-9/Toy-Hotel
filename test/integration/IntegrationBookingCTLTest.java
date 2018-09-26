package hotel.test.integration;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class IntegrationBookingCTLTest {
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
        hotel = HotelHelper.loadHotel();
        bookingCTL = new BookingCTL(hotel);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        date = format.parse("01-01-0001");
        anotherDate = format.parse("02-02-0001");
        cal.setTime(date);
    }

    @Test
    public void testExistingGuestBookAnAvailableRoom() {
        when(hotel.findGuestByPhoneNumber(2)).thenReturn(mockGuest);
        doNothing().when(mockBookingUI).displayGuestDetails(mockGuest.getName(), mockGuest.getAddress(), mockGuest.getPhoneNumber());
        doNothing().when(mockBookingUI).setState(BookingUI.State.ROOM);
        bookingCTL.phoneNumberEntered(2);
        bookingCTL.roomTypeAndOccupantsEntered(RoomType.SINGLE, 1);
        bookingCTL.bookingTimesEntered(date, 5);
        bookingCTL.creditDetailsEntered(CreditCardType.VISA, 1, 321);
        bookingCTL.completed();
    }


    @Test
    public void testNewGuestBookAnAvailableRoom() {
        bookingCTL.phoneNumberEntered(123456);
        bookingCTL.guestDetailsEntered("stephen", "111 USA");
        bookingCTL.roomTypeAndOccupantsEntered(RoomType.TWIN_SHARE, 4);
        bookingCTL.bookingTimesEntered(date, 7);
        bookingCTL.creditDetailsEntered(CreditCardType.MASTERCARD, 3, 422);
        bookingCTL.completed();
    }


    @Test
    public void testExistingGuestAttemptsToBookUnavailableRoomThenBooksAnotherRoom() {
        bookingCTL.phoneNumberEntered(2);
        bookingCTL.roomTypeAndOccupantsEntered(RoomType.DOUBLE, 2);
        bookingCTL.bookingTimesEntered(date, 7);
        bookingCTL.bookingTimesEntered(anotherDate, 7);
        bookingCTL.creditDetailsEntered(CreditCardType.MASTERCARD, 3, 422);
        bookingCTL.completed();
    }


    @Test
    public void testExistingGuestAttemptsToBookUnavailableRoomThenCancel() {
        bookingCTL.phoneNumberEntered(2);
        bookingCTL.roomTypeAndOccupantsEntered(RoomType.DOUBLE, 2);
        bookingCTL.bookingTimesEntered(date, 7);
        bookingCTL.cancel();
    }


    @Test
    public void testExistingGuestCreditCardRefusedThenEnterNewCardIsAuthorized() {
        bookingCTL.phoneNumberEntered(2);
        bookingCTL.roomTypeAndOccupantsEntered(RoomType.TWIN_SHARE, 1);
        bookingCTL.bookingTimesEntered(anotherDate, 5);
        bookingCTL.creditDetailsEntered(CreditCardType.MASTERCARD, 6, 321);
        bookingCTL.creditDetailsEntered(CreditCardType.VISA, 5, 321);
        bookingCTL.completed();
    }


    @Test
    public void testExistingGuestCreditCardRefusedThenCancel() {
        bookingCTL.phoneNumberEntered(2);
        bookingCTL.roomTypeAndOccupantsEntered(RoomType.TWIN_SHARE, 1);
        bookingCTL.bookingTimesEntered(anotherDate, 5);
        bookingCTL.creditDetailsEntered(CreditCardType.MASTERCARD, 6, 321);
        bookingCTL.cancel();
    }

}

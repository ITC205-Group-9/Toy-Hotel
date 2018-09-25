package hotel.test.booking;

import hotel.booking.BookingCTL;
import hotel.booking.BookingUI;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.Room;
import hotel.entities.RoomType;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class BookingCTLTest {

    @Mock private BookingUI bookingUI;
    @Mock private Hotel hotel;

    @Mock private Guest guest;
    @Mock private Room room;
    private BookingCTL bookingCTL;
    private double cost;

    private int phoneNumber;
    private RoomType selectedRoomType;
    private int occupantNumber;
    private Date arrivalDate;
    private int stayLength;

    @BeforeEach
    void setUp() {
        bookingCTL = new BookingCTL(hotel);
    }

    @Test
    public void testCreditDetailsEntered() {
        // CreditCardType type, int number, int ccv
        // throws a RuntimeException if state is not CREDIT
        //	creates a new CreditCard
        //	calls CreditAuthorizer.authorise()
        //	if approved
        //		calls entities.book()
        //		calls UI.displayConfirmedBooking()
        //		sets state to COMPLETED
        //		sets UI state to COMPLETED
        //	else
        //		calls UI.displayMessage with credit not authorised message
    }

    @Test
    public void testCreditDetailsEnteredWhenStateIsNotCredit() {

    }
}
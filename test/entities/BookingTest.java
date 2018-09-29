package hotel.test.entities;

import hotel.HotelHelper;
import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class BookingTest {

    @Mock Room room;
    @Mock Guest guest;
    @Mock CreditCard card;
    @InjectMocks Booking booking;
    Date date;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        date = format.parse("01-01-2019");
        cal.setTime(date);

        booking = new Booking(guest, room, date, 2, 2, card);
    }

    @Test
    void checkIn() {
        doNothing().when(room).checkIn();
        booking.checkIn();
        verify(room, times(1)).checkIn();
    }

    @Test
    void checkInException() {
        checkIn();
        assertThrows(RuntimeException.class, () -> booking.checkIn());
    }

    @Test
    void addServiceCharge() {
        checkIn();
        assertEquals(0, booking.getCharges().size());
        booking.addServiceCharge(ServiceType.ROOM_SERVICE, 20);
        assertEquals(1, booking.getCharges().size());
    }

    @Test
    void checkOut() {
        checkIn();
        booking.checkOut();
    }

    @Test
    void checkOutException() {
        assertThrows(RuntimeException.class, () -> booking.checkOut());
    }
}
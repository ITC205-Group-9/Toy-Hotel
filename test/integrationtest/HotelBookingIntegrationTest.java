package hotel.test.integrationtest;

import hotel.HotelHelper;
import hotel.booking.BookingCTL;
import hotel.credit.CreditAuthorizer;
import hotel.credit.CreditCardType;
import hotel.entities.Guest;
import hotel.entities.Hotel;
import hotel.entities.RoomType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HotelBookingIntegrationTest {

    Date date;
    int phoneNumber;
    int stayLength;
    Hotel hotel;
    BookingCTL bookingCTL;


    @BeforeEach
    void setUp() throws Exception{
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        date = format.parse("1-1-2019");
        cal.setTime(date);
        hotel = HotelHelper.loadHotel();
        bookingCTL = new BookingCTL(hotel, CreditAuthorizer.getInstance());
    }

    @Test
    void creditDetailsEntered() {
        phoneNumber = 3;
        stayLength = 2;
        Guest guest = new Guest("Nick", "Enmore", phoneNumber);
        bookingCTL.phoneNumberEntered(guest.getPhoneNumber());
        bookingCTL.guestDetailsEntered(guest.getName(), guest.getAddress());
        bookingCTL.roomTypeAndOccupantsEntered(RoomType.DOUBLE, 2);
        bookingCTL.bookingTimesEntered(date, stayLength);
        bookingCTL.creditDetailsEntered(CreditCardType.MASTERCARD, 12, 123);
        bookingCTL.creditDetailsEntered(CreditCardType.VISA, 2, 123);
        assertNotNull(hotel.findGuestByPhoneNumber(phoneNumber));
        assertEquals(2, hotel.bookingsByConfirmationNumber.size());
    }
}
package hotel.test.entities;


import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class HotelTest {

    @Mock Room room;
    @Mock Guest guest;
    @Mock CreditCard card;
    @Mock Booking booking;
    Hotel hotel;

    Date arrivalDate;
    int stayLength;
    int occupantNumber;
    long confirmationNumber;
    // testing steps 1. arrange mock object, initial values... 2. act 3. assert value

    @BeforeEach
    void setUp() throws ParseException {
        MockitoAnnotations.initMocks(this);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        arrivalDate = format.parse("10-11-2018");

        cal.setTime(arrivalDate);
        stayLength = 10;
        occupantNumber = 2;

        hotel = new Hotel();
        hotel.addRoom(RoomType.SINGLE, 101);
        hotel.addRoom(RoomType.DOUBLE, 201);
        hotel.addRoom(RoomType.TWIN_SHARE, 301);

        guest = new Guest("Stephen", "Chou", 4667891);
        card = new CreditCard(CreditCardType.VISA, 2, 2);

    }

    @Test
    public void testBook() {
        room = hotel.findAvailableRoom(RoomType.DOUBLE, arrivalDate, stayLength);
        assertNotNull(room);
        confirmationNumber = hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, card);
        assertNotEquals(0L, confirmationNumber);
        booking = hotel.findBookingByConfirmationNumber(confirmationNumber);
        assertNotNull(booking);
        assertNull(hotel.findAvailableRoom(RoomType.DOUBLE, arrivalDate, stayLength));
    }

    @Test
    public void testCheckIn() {
        room = hotel.findAvailableRoom(RoomType.DOUBLE, arrivalDate, stayLength);
        confirmationNumber = hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, card);
        hotel.checkin(confirmationNumber);
        booking = hotel.findActiveBookingByRoomId(room.getId());
        assertNotNull(booking);
        assertTrue(booking.isCheckedIn());
    }

    @Test
    public void testCheckInShouldThrowRuntimeExceptionIfNoBookingExists() {
        Executable e = () -> hotel.checkin(30110);
        Throwable t = assertThrows(RuntimeException.class, e);
        assertEquals("There is not booking for the confirmation number exists!", t.getMessage());
    }


    @Test
    public void testAddServiceCharge() {
        room = hotel.findAvailableRoom(RoomType.DOUBLE, arrivalDate, stayLength);
        confirmationNumber = hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, card);
        hotel.checkin(confirmationNumber);
        booking = hotel.findBookingByConfirmationNumber(confirmationNumber);
        assertEquals(0, booking.getCharges().size());
        hotel.addServiceCharge(room.getId(), ServiceType.ROOM_SERVICE, 20);
        assertEquals(1, booking.getCharges().size());
    }


    @Test
    public void testAddServiceChargeWhenThereIsNoActiveBookingAssociatedWithTheRoom() {
        Executable e = () -> hotel.addServiceCharge(room.getId(), ServiceType.ROOM_SERVICE, 20);
        Throwable t = assertThrows(RuntimeException.class, e);
        assertEquals("The room has not checked in yet!", t.getMessage());
    }


    @Test
    public void testCheckOut() {
        room = hotel.findAvailableRoom(RoomType.DOUBLE, arrivalDate, stayLength);
        confirmationNumber = hotel.book(room, guest, arrivalDate, stayLength, occupantNumber, card);
        hotel.checkin(confirmationNumber);
        booking = hotel.findBookingByConfirmationNumber(confirmationNumber);
        hotel.checkout(room.getId());
        assertTrue(booking.isCheckedOut());
    }


    @Test
    public void testCheckOutWhenThereIsNoActiveBookingAssociatedWithTheRoom() {
        Executable e = () -> hotel.checkout(room.getId());
        Throwable t = assertThrows(RuntimeException.class, e);
        assertEquals("The room has not checked in yet!", t.getMessage());
    }

}
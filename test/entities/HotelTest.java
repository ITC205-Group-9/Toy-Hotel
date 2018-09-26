package hotel.test.entities;


import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
class HotelTest {

    Room room;
    Guest guest;
    CreditCard card;
    @Mock Booking mockBooking;
    @Mock Room mockRoom;
    @InjectMocks Hotel hotel;

    Date arrivalDate;
    int stayLength;
    int occupantNumber;
    long confirmationNumber;

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
        confirmationNumber = Long.parseLong("10112018201");
        when(mockRoom.book(guest, arrivalDate, stayLength, occupantNumber, card)).thenReturn(mockBooking);
        when(mockBooking.getConfirmationNumber()).thenReturn(confirmationNumber);
        assertEquals(confirmationNumber, hotel.book(mockRoom, guest, arrivalDate, stayLength, occupantNumber, card));
        verify(mockRoom, times(1)).book(guest, arrivalDate, stayLength, occupantNumber, card);
        verify(mockBooking, times(1)).getConfirmationNumber();
    }


    @Test
    public void testCheckIn() {
        confirmationNumber = Long.parseLong("10112018201");
        when(mockRoom.book(guest, arrivalDate, stayLength, occupantNumber, card)).thenReturn(mockBooking);
        when(mockBooking.getConfirmationNumber()).thenReturn(confirmationNumber);
        doNothing().when(mockBooking).checkIn();
        when(mockBooking.getRoom()).thenReturn(mockRoom);
        when(mockRoom.getId()).thenReturn(201);
        doNothing().when(mockRoom).checkin();
        assertEquals(0, hotel.activeBookingsByRoomId.size());
        hotel.checkIn(hotel.book(mockRoom, guest, arrivalDate, stayLength, occupantNumber, card));
        assertEquals(1, hotel.activeBookingsByRoomId.size());
        verify(mockBooking, times(1)).checkIn();
        verify(mockRoom, times(1)).checkin();
    }

    @Test
    public void testCheckInShouldThrowRuntimeExceptionIfNoBookingExists() {
        Executable e = () -> hotel.checkIn(Long.parseLong("10112019201"));
        assertThrows(RuntimeException.class, e);
    }


    @Test
    public void testAddServiceCharge() {
        testCheckIn();
        doNothing().when(mockBooking).addServiceCharge(ServiceType.ROOM_SERVICE, 20);
        hotel.addServiceCharge(201, ServiceType.ROOM_SERVICE, 20);
    }


    @Test
    public void testAddServiceChargeWhenThereIsNoActiveBookingAssociatedWithTheRoom() {
        Executable e = () -> hotel.addServiceCharge(room.getId(), ServiceType.ROOM_SERVICE, 20);
        assertThrows(RuntimeException.class, e);
    }


    @Test
    public void testCheckOut() {
        testCheckIn();
        doNothing().when(mockBooking).checkOut();
        hotel.checkOut(201);
    }


    @Test
    public void testCheckOutWhenThereIsNoActiveBookingAssociatedWithTheRoom() {
        Executable e = () -> hotel.checkOut(301);
        assertThrows(RuntimeException.class, e);
    }

}
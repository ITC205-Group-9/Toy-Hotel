package hotel.test.service;

import hotel.entities.Booking;
import hotel.entities.Hotel;
import hotel.entities.ServiceType;
import hotel.service.RecordServiceCTL;
import hotel.service.RecordServiceUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class RecordServiceCTLTest {
    @Mock
    Hotel hotel;

    @Mock
    RecordServiceUI recordServiceUI;

    @Mock
    Booking booking;

    @InjectMocks
    RecordServiceCTL recordServiceCTL;

    @BeforeEach
    void setUp () {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void serviceDetailsEntered() {
        when(hotel.findActiveBookingByRoomId(anyInt())).thenReturn(booking);
        recordServiceCTL.roomNumberEntered(2);
        verify(hotel, times(1)).findActiveBookingByRoomId(anyInt());
        doNothing().when(hotel).addServiceCharge(anyInt(), any(ServiceType.class), anyDouble());
        recordServiceCTL.serviceDetailsEntered(ServiceType.ROOM_SERVICE, 20);
        verify(hotel, times(1)).addServiceCharge(anyInt(), any(ServiceType.class), anyDouble());
    }

    @Test
    void serviceDetailsEnteredException() {
        assertThrows(RuntimeException.class, () -> recordServiceCTL.serviceDetailsEntered(ServiceType.ROOM_SERVICE, 20));
    }
}
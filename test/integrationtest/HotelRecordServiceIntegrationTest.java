package hotel.test.integrationtest;

import hotel.entities.Booking;
import hotel.entities.Hotel;
import hotel.entities.ServiceType;
import hotel.service.RecordServiceCTL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class HotelRecordServiceIntegrationTest {

    @Mock
    Hotel hotel;
    @Mock
    Booking booking;
    @InjectMocks
    RecordServiceCTL recordServiceCTL;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void hotelRecordService() {
        when(hotel.findActiveBookingByRoomId(anyInt())).thenReturn(booking);
        recordServiceCTL.roomNumberEntered(2);
        doNothing().when(hotel).addServiceCharge(anyInt(), any(ServiceType.class), anyDouble());
        recordServiceCTL.serviceDetailsEntered(ServiceType.ROOM_SERVICE, 20);
        verify(hotel, times(1)).addServiceCharge(anyInt(), any(ServiceType.class), anyDouble());
    }

}
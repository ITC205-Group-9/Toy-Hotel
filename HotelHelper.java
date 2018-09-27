package hotel;

import hotel.credit.CreditCard;
import hotel.credit.CreditCardType;
import hotel.entities.*;
import hotel.utils.IOUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class HotelHelper {

	public static Hotel loadHotel() throws Exception {
		
		//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//Date date = format.parse("01-01-2001");
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date = format.parse("10-12-2018");
		cal.setTime(date);
		
		
		Hotel hotel = new Hotel();
		hotel.addRoom(RoomType.SINGLE, 101);
		hotel.addRoom(RoomType.DOUBLE, 201);
		hotel.addRoom(RoomType.TWIN_SHARE, 301);
		
		Guest guest = hotel.registerGuest("Fred", "Nurke", 2);
		CreditCard card = new CreditCard(CreditCardType.VISA, 2, 2);
		
		Room room = hotel.findAvailableRoom(RoomType.TWIN_SHARE, date, 1);
		long confNo = hotel.book(room, guest, date, 1, 2, card);
		Booking booking = hotel.findBookingByConfirmationNumber(confNo);
		hotel.checkIn(confNo);
		booking.addServiceCharge(ServiceType.ROOM_SERVICE, 7.00);
		
		IOUtils.trace("HotelHelper");
		for (RoomType rt : RoomType.values()) {
			Map<Integer, Room> rooms = hotel.roomsByType.get(rt);
			for (Integer id : rooms.keySet()) {
				IOUtils.outputln(rooms.get(id));
			}
		}		
		return hotel;
	}

	
	public static void saveHotel() {}

}

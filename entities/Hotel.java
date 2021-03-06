package hotel.entities;

import hotel.credit.CreditCard;
import hotel.utils.IOUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Hotel {
	
	private Map<Integer, Guest> guests;
	public Map<RoomType, Map<Integer,Room>> roomsByType;
	public Map<Long, Booking> bookingsByConfirmationNumber;
	public Map<Integer, Booking> activeBookingsByRoomId;
	
	
	public Hotel() {
		guests = new HashMap<>();
		roomsByType = new HashMap<>();
		for (RoomType rt : RoomType.values()) {
			Map<Integer, Room> rooms = new HashMap<>();
			roomsByType.put(rt, rooms);
		}
		bookingsByConfirmationNumber = new HashMap<>();
		activeBookingsByRoomId = new HashMap<>();
	}

	
	public void addRoom(RoomType roomType, int id) {
		IOUtils.trace("Hotel: addRoom");
		for (Map<Integer, Room> rooms : roomsByType.values()) {
			if (rooms.containsKey(id)) {
				throw new RuntimeException("Hotel: addRoom : room number already exists");
			}
		}
		Map<Integer, Room> rooms = roomsByType.get(roomType);
		Room room = new Room(id, roomType);
		rooms.put(id, room);
	}

	
	public boolean isRegistered(int phoneNumber) {
		return guests.containsKey(phoneNumber);
	}

	
	public Guest registerGuest(String name, String address, int phoneNumber) {
		if (guests.containsKey(phoneNumber)) {
			throw new RuntimeException("Phone number already registered");
		}
		Guest guest = new Guest(name, address, phoneNumber);
		guests.put(phoneNumber, guest);		
		return guest;
	}

	
	public Guest findGuestByPhoneNumber(int phoneNumber) {
		Guest guest = guests.get(phoneNumber);
		return guest;
	}

	
	public Booking findActiveBookingByRoomId(int roomId) {
		Booking booking = activeBookingsByRoomId.get(roomId);
		return booking;
	}


	public Room findAvailableRoom(RoomType selectedRoomType, Date arrivalDate, int stayLength) {
		IOUtils.trace("Hotel: checkRoomAvailability");
		Map<Integer, Room> rooms = roomsByType.get(selectedRoomType);
		for (Room room : rooms.values()) {
			IOUtils.trace(String.format("Hotel: checking room: %d",room.getId()));
			if (room.isAvailable(arrivalDate, stayLength)) {
				return room;
			}			
		}
		return null;
	}

	
	public Booking findBookingByConfirmationNumber(long confirmationNumber) {
		return bookingsByConfirmationNumber.get(confirmationNumber);
	}


	public long book(Room room, Guest guest, 
			Date arrivalDate, int stayLength, int occupantNumber,
			CreditCard creditCard) {
        // return unique confirmation number for a booking
        // The confirmation number has this format: ddMMYYYYrrr, where ddMMYYYY is the date of the booking and rrr is the room Id
        // After calling this method:
        //      1. A booking should exist for the room (this method should call room.book())
        //      2. The room should not be available for the specified arrivalDate and staylength
        //      3. The booking should be returned from findBookingByConfirmationNumber()
        Booking booking = room.book(guest, arrivalDate, stayLength, occupantNumber, creditCard);
        long confirmationNumber = 0L;
        if (booking != null) {
        	confirmationNumber = booking.getConfirmationNumber();
            bookingsByConfirmationNumber.put(confirmationNumber, booking);
        }
		return confirmationNumber;
	}

	
	public void checkIn(long confirmationNumber) {
	    // throws a RuntimeException if no booking for confirmation number exists
        // After calling this method:
        //      1. The Booking referenced by confirmationNumber should be returned by getActiveBookingByRoomId()
        //      2. The Booking referenced by confirmationNumber should have a state of CHECKED_IN
	    Booking booking = findBookingByConfirmationNumber(confirmationNumber);
		if ( booking == null) {
			throw new RuntimeException("There is not booking for the confirmation number exists!");
		} else {
		    booking.checkIn();
		    activeBookingsByRoomId.put(booking.getRoom().getId(), booking);
        }
	}


	public void addServiceCharge(int roomId, ServiceType serviceType, double cost) {
		// throws a RuntimeException if no active booking associated with the room identified by roomID can be found
        // After this method is called:
        //      1. A ServiceCharge should have been added to the active booking.
        Booking booking = activeBookingsByRoomId.get(roomId);
        if (booking == null) {
            throw new RuntimeException("The room has not checked in yet!");
        } else {
            booking.addServiceCharge(serviceType, cost);
        }
	}

	
	public void checkOut(int roomId) {
		// throws a RuntimeException if no active booking associated with the room identified by roomID can be found
        // The Booking referenced by confirmationNumber should have a state of CHECKED_OUT
        Booking booking = activeBookingsByRoomId.get(roomId);
        if (booking == null) {
            throw new RuntimeException("The room has not checked in yet!");
        } else {
            booking.checkOut();
        }
	}


}

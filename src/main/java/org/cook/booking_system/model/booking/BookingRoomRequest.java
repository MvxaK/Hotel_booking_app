    package org.cook.booking_system.model.booking;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class BookingRoomRequest {
        private Long roomId;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
    }

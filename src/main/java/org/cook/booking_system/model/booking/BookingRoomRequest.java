    package org.cook.booking_system.model.booking;

    import jakarta.validation.constraints.FutureOrPresent;
    import jakarta.validation.constraints.NotNull;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class BookingRoomRequest {

        @NotNull
        private Long roomId;
        @NotNull @FutureOrPresent
        private LocalDate checkInDate;
        @NotNull @FutureOrPresent
        private LocalDate checkOutDate;

    }

package org.cook.booking_system.model.images;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequest {

    @NotBlank(message = "Image URL cannot be empty")
    private String imageUrl;

}

package com.zuno.dto;

import com.zuno.model.Listing;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class CreateListingRequest {

    @NotBlank(message = "PG name is required")
    @Size(max = 100, message = "PG name must be under 100 characters")
    private String pgName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Area is required")
    private String area;

    @NotBlank(message = "City is required")
    private String city;

    private Double latitude;
    private Double longitude;

    @NotNull(message = "Gender preference is required")
    private Listing.GenderPreference genderPreference;

    @NotNull(message = "Monthly rent is required")
    @Min(value = 1000, message = "Monthly rent must be at least ₹1,000")
    @Max(value = 100000, message = "Monthly rent must be under ₹1,00,000")
    private Integer monthlyRent;

    @Min(value = 0, message = "Security deposit cannot be negative")
    private Integer securityDeposit;

    @NotNull(message = "Food included field is required")
    private Boolean foodIncluded;

    private List<String> amenities;
    private List<String> houseRules;
    private List<String> photos;
}

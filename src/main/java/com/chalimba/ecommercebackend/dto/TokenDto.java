package com.chalimba.ecommercebackend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class represents the response payload for the authentication.
 */
@Data
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class TokenDto {
    private String accessToken;
}

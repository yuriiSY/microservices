package com.yurii.customer;

public record CustomerRegistrationRequest(
    String firstName,
    String lastName,
    String email) {
}

package edu.eci.arsw.blueprints.controllers;

/**
 * Standardized API response wrapper.
 *
 * @param code    HTTP status code
 * @param message Response message or error description
 * @param data    The actual response payload (can be null)
 * @param <T>     The type of the data object
 */
public record ApiResponse<T>(int code, String message, T data) {
}

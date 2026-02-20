package com.fitness.gateway.user;

import com.fitness.gateway.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

	private final WebClient userServiceWebClient;

	public Mono<Boolean> validateUser(String userId) {
		log.info("Calling User Validation {}", userId);
		return userServiceWebClient.get()
								   .uri("/api/users/{userId}/validate", userId)
								   .retrieve()
								   .bodyToMono(Boolean.class)

								   .onErrorResume(WebClientResponseException.class, e -> {
									   if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
										   return Mono.error(
												   new UserNotFoundException("User Not Found " + userId)
										   );
									   }
									   return Mono.error(e);
								   });

	}

	public Mono<UserResponse> registerUser(RegisterRequest request) {
		log.info("Calling User Registration {}", request.getEmail());
		return userServiceWebClient.post()
				.uri("/api/users/register")
				.bodyValue(request)
				.retrieve()
				.bodyToMono(UserResponse.class)

				.onErrorResume(WebClientResponseException.class, e -> {
					if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
						return Mono.error(
								new UserNotFoundException("BAD REQUEST " )
						);
					}
					else if (e.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
						return Mono.error(
								new UserNotFoundException("INTERNAL SERVER ERROR")
						);
					}
					return Mono.error(e);
				});
	}
}

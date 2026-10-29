package sk.ukf.vd.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import sk.ukf.vd.response.ResponseFactory;
import sk.ukf.vd.response.ResponseWrapper;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	private final ResponseFactory response;

	public GlobalExceptionHandler(ResponseFactory response) {
		this.response = response;
	}

	// Validacne chyby
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseWrapper<Void>> handleValidation(MethodArgumentNotValidException ex) {
		List<Map<String, String>> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.map(error -> Map.of(
						"field", error.getField(),
						"message", error.getDefaultMessage()
				))
				.toList();

		return response.error(HttpStatus.BAD_REQUEST, "Validačné chyby", errors);
	}

	// Neplatny JSON alebo zly typ hodnoty v tele poziadavky, napr. "birthDate": "abc"
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ResponseWrapper<Void>> handleNotReadable(HttpMessageNotReadableException ex) {
		return response.error(HttpStatus.BAD_REQUEST,
				"Telo požiadavky nie je platný JSON alebo obsahuje hodnotu nesprávneho typu");
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ResponseWrapper<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		String message = "Parameter '" + ex.getName() + "' má neplatnú hodnotu";
		return response.error(HttpStatus.BAD_REQUEST, message);
	}

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<ResponseWrapper<Void>> handleObjectNotFound(ObjectNotFoundException ex) {
		return response.error(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ResponseWrapper<Void>> handleEmailExists(EmailAlreadyExistsException ex) {
		List<Map<String, String>> errors = List.of(
				Map.of("field", "email", "message", ex.getMessage())
		);
		return response.error(HttpStatus.CONFLICT, "Email už existuje", errors);
	}

	// Neexistujuci endpoint, napr. /api/zamestnanci
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ResponseWrapper<Void>> handleNoResource(NoResourceFoundException ex) {
		return response.error(HttpStatus.NOT_FOUND, "Endpoint neexistuje");
	}

	// Nepodporovana HTTP metoda, napr. DELETE /api/employees
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ResponseWrapper<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		return response.error(HttpStatus.METHOD_NOT_ALLOWED,
				"Metóda " + ex.getMethod() + " nie je pre tento endpoint podporovaná");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseWrapper<Void>> handleGeneral(Exception ex) {
		log.error("Neočakávaná chyba", ex);
		return response.error(HttpStatus.INTERNAL_SERVER_ERROR, "Nastala neočakávaná chyba");
	}
}

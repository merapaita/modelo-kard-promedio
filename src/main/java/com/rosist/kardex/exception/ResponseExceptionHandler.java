//package com.rosist.kardex.exception;
//
//import java.time.LocalDateTime;
//import java.util.stream.Collectors;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
////import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//@ControllerAdvice
//@RestController
//public class ResponseExceptionHandler extends ResponseEntityExceptionHandler  {
//
//	private static final Logger logger = LoggerFactory.getLogger(ResponseExceptionHandler.class);
//
//	@ExceptionHandler(Exception.class)
//	public final ResponseEntity<ExceptionResponse> manejarTodasExcepciones(Exception ex, WebRequest request){
//		ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
//		return new ResponseEntity<>(er, HttpStatus.INTERNAL_SERVER_ERROR);
//	}
//
//	@ExceptionHandler(Excepciones.class)
//	public final ResponseEntity<ExceptionResponse> manejarExcepciones(Exception ex, WebRequest request) {
//		ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(),
//				request.getDescription(false));
//		return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
//	}
//
//
////	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//																  HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//		String mensaje = ex.getBindingResult().getAllErrors().stream().map(e -> {
//			return e.getDefaultMessage().concat(", ");
//		}).collect(Collectors.joining());
//
//		ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), mensaje, request.getDescription(false));
//		return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
//
//	}
//
//	@ExceptionHandler(ModelNotFoundException.class)
//	public ResponseEntity<ExceptionResponse> manejarModelNotFoundException(ModelNotFoundException ex, WebRequest request) {
//		ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
//		return new ResponseEntity<>(er, HttpStatus.NOT_FOUND);
//	}
//
//	@ExceptionHandler(ResourceNotFoundException.class)
//	public ResponseEntity<ExceptionResponse> manejarResourceNotFoundException(ResourceNotFoundException ex,
//																			  WebRequest request) {
//		ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(),
//				request.getDescription(false));
//		return new ResponseEntity<>(er, HttpStatus.BAD_REQUEST);
//	}
//
//	@ExceptionHandler(AccessDeniedException.class)
//	public ResponseEntity<ExceptionResponse> accesDeniedException(AccessDeniedException ex, WebRequest request) {
//		String mensaje = "Acceso denegado. no tiene permiso para acceder a testa funcion. Contacte al Administrador si cree que esto es un error. ";
//		ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), mensaje, request.getDescription(false));
////		ExceptionResponse er = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
//		return new ResponseEntity<>(er, HttpStatus.UNAUTHORIZED);
//	}
//
//}
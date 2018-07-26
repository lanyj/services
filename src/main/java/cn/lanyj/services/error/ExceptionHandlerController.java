package cn.lanyj.services.error;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * General error handler for the application.
 */
@ControllerAdvice
class ExceptionHandlerController {
	private Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);
	
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public void notFound(HttpServletRequest request, NotFoundException exception) {
        String uri = request.getRequestURI();

        log.error("Request page: {} raised NotFoundException {}", uri, exception);

//        ModelAndView model = new ModelAndView("error/general");
//        model.addObject("status", HttpStatus.NOT_FOUND.value());
//        model.addObject("error", HttpStatus.NOT_FOUND.getReasonPhrase());
//        model.addObject("path", uri);
//        model.addObject("customMessage", exception.getMessage());
//
//        return model;
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceProcessException.class)
    public void serviceProcessException(HttpServletRequest request, ServiceProcessException exception) {
    	log.error("Service process raised Exception", exception);
    	
    }

    /**
     * Handle all exceptions
     */
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(Exception.class)
    public void exception(HttpServletRequest request, Exception exception) {
        String uri = request.getRequestURI();
        log.error("Request page: {} raised exception {}", uri, exception);

//        ModelAndView model = new ModelAndView("error/general");
//        model.addObject("error", Throwables.getRootCause(exception).getMessage());
//        model.addObject("status", Throwables.getRootCause(exception).getCause());
//        model.addObject("path", uri);
//        model.addObject("customMessage", exception.getMessage());
//
//        return model;
    }
}
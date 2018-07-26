package cn.lanyj.services.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Raysmond
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public final class NotFoundException extends ARuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7075801080281030058L;

    public NotFoundException() {
    }

    public NotFoundException(String message) {
    	super(message);
    }

}
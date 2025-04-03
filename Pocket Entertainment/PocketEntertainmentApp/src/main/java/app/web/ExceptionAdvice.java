package app.web;

import app.exception.NoEuroException;
import app.exception.NoRefundsLeft;
import app.exception.NotEnoughPT;
import app.exception.UsernameAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.UUID;

@ControllerAdvice
public class ExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoResourceFoundException.class, MethodArgumentTypeMismatchException.class, AccessDeniedException.class, MissingRequestValueException.class})
    public ModelAndView handleNotFoundExceptions(Exception ex){

        ModelAndView mav = new ModelAndView();
        mav.setViewName("notFound");
        mav.addObject("exception", ex.getMessage());

        return mav;
    }

    @ExceptionHandler(UsernameAlreadyExists.class)
    public String handleUserAlreadyExists(RedirectAttributes redirectAttributes, Exception ex){

        redirectAttributes.addFlashAttribute("alreadyExists", ex.getMessage());

        return "redirect:/register";
    }

    @ExceptionHandler({NoRefundsLeft.class, NotEnoughPT.class})
    public String handleNoRefundsLeft(RedirectAttributes redirectAttributes, Exception ex){
        redirectAttributes.addFlashAttribute("cosmeticError", ex.getMessage());
        return "redirect:/home";
    }

    @ExceptionHandler(NoEuroException.class)
    public String handleNoEuroException(RedirectAttributes redirectAttributes, Exception ex){
        redirectAttributes.addFlashAttribute("noEuro", "EUR Balance not enough.");
        UUID id = UUID.fromString(ex.getMessage());
        return String.format("redirect:/wallets/" + id);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException() {

        ModelAndView mav = new ModelAndView();
        mav.setViewName("exception");

        return mav;
    }




}

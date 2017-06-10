package ru.web.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.ArrayList;
import java.util.ResourceBundle;

@FacesValidator
public class LoginValidator implements Validator {
    @Override
    public void validate(FacesContext facesContext,
                         UIComponent uiComponent, Object o)
            throws ValidatorException {

        ResourceBundle bundle = ResourceBundle.getBundle(
                "ru.web.loc.messages", FacesContext.getCurrentInstance()
                        .getViewRoot().getLocale());
        try {
            String nv = o.toString();

            if (nv.length() < 5) {
                throw new IllegalArgumentException(bundle.getString("login_length_error"));
            }
            if (!Character.isLetter(nv.charAt(0))) {
                throw new IllegalArgumentException(bundle.getString("first_letter_error"));
            }
            if(getTestArray().contains(nv)){
                throw new IllegalArgumentException(bundle.getString("used_name"));
            }


        } catch (IllegalArgumentException exception) {
            FacesMessage message = new FacesMessage(exception.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);

        }
    }
    private ArrayList<String> getTestArray(){
        ArrayList<String> list=new ArrayList<>();
        list.add("name");
        list.add("login");
        return list;
    }
}

package org.jasig.ssp.service.impl;

import org.jasig.ssp.model.EarlyAlert;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class EarlyAlertValidatorUtils {

    public void earlyAlertCannotBeNull(EarlyAlert earlyAlert, RuntimeException ex ) {
        //1
        if (earlyAlert == null) {
            throw ex;
        }
    }

    public void earlyAlertPersonCannotBeNull(EarlyAlert earlyAlert, RuntimeException ex ) {
        //1
        if (earlyAlert.getPerson() == null) {
            throw ex;
        }
    }

    public void earlyAlertPersonCannotBeNull(	EarlyAlert earlyAlert, ValidationException ex ) throws ValidationException {
       //1
        if (earlyAlert.getPerson() == null) {
            throw ex;
        }
    }

    public void earlyAlertCreateByCannotBeNull(EarlyAlert earlyAlert, IllegalArgumentException e) {
        //1
        if (earlyAlert.getCreatedBy() == null  || earlyAlert.getCreatedBy().getFirstName() == null) {
            throw e;
        }

    }

    public void earlyAlertCreateByAndCampus(){

    }

}

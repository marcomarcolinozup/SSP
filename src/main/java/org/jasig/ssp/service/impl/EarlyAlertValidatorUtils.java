package org.jasig.ssp.service.impl;

import org.jasig.ssp.model.EarlyAlert;
import org.jasig.ssp.web.api.validation.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class EarlyAlertValidatorUtils {

    public void earlyAlertCannotBeNull(EarlyAlert earlyAlert, RuntimeException ex ) {
        if (earlyAlert == null) {
            throw ex;
        }
    }

    public void earlyAlertPersonCannotBeNull(EarlyAlert earlyAlert, RuntimeException ex ) {
        if (earlyAlert.getPerson() == null) {
            throw ex;
        }
    }

    public void earlyAlertPersonCannotBeNull(	EarlyAlert earlyAlert, ValidationException ex ) throws ValidationException {
        if (earlyAlert.getPerson() == null) {
            throw ex;
        }
    }
}

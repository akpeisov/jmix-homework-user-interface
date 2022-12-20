package com.company.homeworkloans.screen.requestloan;

import com.company.homeworkloans.entity.Client;
import com.company.homeworkloans.entity.Loan;
import com.company.homeworkloans.entity.LoanStatus;
import io.jmix.core.DataManager;
import io.jmix.core.common.util.Preconditions;
import io.jmix.ui.Notifications;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.EntityComboBox;
import io.jmix.ui.component.TextField;
import io.jmix.ui.component.validation.DecimalMinValidator;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.screen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@UiController("RequestLoanScreen")
@UiDescriptor("request-loan-screen.xml")
//@DialogMode(forceDialog = true, resizable = true)
public class RequestLoanScreen extends Screen {
    @Autowired
    private CollectionLoader<Client> clientsDl;
    @Autowired
    private Notifications notifications;
    @Autowired
    private DataManager dataManager;

    private static final Logger log = LoggerFactory.getLogger(RequestLoanScreen.class);
    @Autowired
    private TextField<Long> amount;
    @Autowired
    private EntityComboBox<Client> client;

    public RequestLoanScreen withClient(@Nullable Client client) {
        if (client != null) {
            this.client.setValue(client);
        }
        else
            log.info("client not selected");
        return this;
    }

    @Subscribe("windowClose")
    public void onWindowClose(Action.ActionPerformedEvent event) {
        closeWithDefaultAction();
    }

    @Subscribe("request")
    public void onRequest(Action.ActionPerformedEvent event) {
        log.info("request click");
        client.validate();
        amount.validate();

        /*
        // try custom validation
        if (amount == null || amount.getValue() == 0) {
            notifications.create(Notifications.NotificationType.ERROR)
                    .withCaption("Error")
                    .show();
            return;
        }*/
        Loan loan = dataManager.create(Loan.class);
        loan.setAmount(BigDecimal.valueOf(amount.getValue()));
        loan.setClient(client.getValue());
        loan.setStatus(LoanStatus.REQUESTED);
        loan.setRequestDate(LocalDate.now());
        dataManager.save(loan);
        close(StandardOutcome.COMMIT);
        notifications.create()
                .withCaption("Loan created")
                .show();

    }


}
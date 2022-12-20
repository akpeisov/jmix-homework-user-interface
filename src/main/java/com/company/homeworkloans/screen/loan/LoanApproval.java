package com.company.homeworkloans.screen.loan;

import com.company.homeworkloans.entity.LoanStatus;
import io.jmix.core.DataManager;
import io.jmix.ui.Notifications;
import io.jmix.ui.component.Button;
import io.jmix.ui.model.CollectionContainer;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import com.company.homeworkloans.entity.Loan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;

@UiController("Loan.approval")
@UiDescriptor("loan-Approval.xml")
@LookupComponent("loansTable")
public class LoanApproval extends StandardLookup<Loan> {
    private static final Logger log = LoggerFactory.getLogger(LoanApproval.class);

    @Autowired
    private DataManager dataManager;
    @Autowired
    private CollectionLoader<Loan> prevLoansDl;
    @Autowired
    private CollectionContainer<Loan> loansDc;
    @Autowired
    private Notifications notifications;
    @Autowired
    private MessageBundle messageBundle;
    @Autowired
    private CollectionLoader<Loan> loansDl;

    @Subscribe(id = "loansDc", target = Target.DATA_CONTAINER)
    public void onLoansDcItemChange(InstanceContainer.ItemChangeEvent<Loan> event) {
        //log.info("client " + event.getItem().getClient().getFirstName());
        if (event.getItem() == null || event.getItem().getClient() == null)
            return;
        prevLoansDl.setParameter("clientId", event.getItem().getClient());
        prevLoansDl.setParameter("loadId", event.getItem().getId());
        prevLoansDl.load();
    }

    @Subscribe("approve")
    public void onApproveClick(Button.ClickEvent event) {
        Loan loan = loansDc.getItem();
        loan.setStatus(LoanStatus.APPROVED);
        dataManager.save(loan);
        loansDl.load();
        notifications.create()
                .withCaption(messageBundle.getMessage("approved"))
                .show();
    }

    @Subscribe("reject")
    public void onRejectClick(Button.ClickEvent event) {
        Loan loan = loansDc.getItem();
        loan.setStatus(LoanStatus.REJECTED);
        dataManager.save(loan);
        loansDl.load();
        notifications.create()
                .withCaption(messageBundle.getMessage("rejected"))
                .show();
    }
}
package com.company.homeworkloans.screen.client;

import com.company.homeworkloans.screen.requestloan.RequestLoanScreen;
import io.jmix.ui.Notifications;
import io.jmix.ui.ScreenBuilders;
import io.jmix.ui.action.Action;
import io.jmix.ui.component.GroupTable;
import io.jmix.ui.screen.*;
import com.company.homeworkloans.entity.Client;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("Client.browse")
@UiDescriptor("client-browse.xml")
@LookupComponent("clientsTable")
public class ClientBrowse extends StandardLookup<Client> {
    @Autowired
    private Notifications notifications;
    @Autowired
    private ScreenBuilders screenBuilders;
    @Autowired
    private GroupTable<Client> clientsTable;

    @Subscribe("requestLoanAction")
    public void onRequestLoanAction(Action.ActionPerformedEvent event) {

        Client client = clientsTable.getSingleSelected();

        screenBuilders.screen(this)
                .withScreenClass(RequestLoanScreen.class)
                .build()
                .withClient(client)
                .show();
    }

}
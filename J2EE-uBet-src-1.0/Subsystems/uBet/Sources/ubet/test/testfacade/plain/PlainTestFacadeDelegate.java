package ubet.test.testfacade.plain;

import javax.sql.DataSource;

import ubet.model.util.GlobalNames;
import ubet.test.testfacade.delegate.TestFacadeDelegate;
import ubet.test.testfacade.plain.actions.RemoveAccountAction;
import ubet.test.testfacade.plain.actions.RemoveBetAction;
import ubet.test.testfacade.plain.actions.RemoveBetTypeAction;
import ubet.test.testfacade.plain.actions.RemoveEventAction;
import ubet.test.testfacade.plain.actions.RemoveUserAction;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.PlainActionProcessor;

public class PlainTestFacadeDelegate implements TestFacadeDelegate {

        private DataSource getDataSource() throws InternalErrorException {
                return DataSourceLocator
                                .getDataSource(GlobalNames.UBET_DATA_SOURCE);
        }

        public void removeUser(String login) throws InstanceNotFoundException,
                        InternalErrorException {
                try {

                        RemoveUserAction action = new RemoveUserAction(login);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void removeBetType(Long betTypeID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {

                        RemoveBetTypeAction action = new RemoveBetTypeAction(
                                        betTypeID);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void removeEvent(Long eventID) throws InstanceNotFoundException,
                        InternalErrorException {
                try {

                        RemoveEventAction action = new RemoveEventAction(
                                        eventID);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void removeBet(Long betID) throws InstanceNotFoundException,
                        InternalErrorException {
                try {

                        RemoveBetAction action = new RemoveBetAction(betID);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void removeAccount(Long accountID)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {

                        RemoveAccountAction action = new RemoveAccountAction(
                                        accountID);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}

package ubet.model.adminfacade.plain;

import java.util.List;

import javax.sql.DataSource;

import ubet.model.adminfacade.delegate.AdminFacadeDelegate;
import ubet.model.adminfacade.plain.actions.InsertBetTypeAction;
import ubet.model.adminfacade.plain.actions.InsertEventAction;
import ubet.model.adminfacade.plain.actions.PublishResultsAction;
import ubet.model.adminfacade.plain.actions.SetHighlightedAction;
import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.to.EventTO;
import ubet.model.util.GlobalNames;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;
import es.udc.fbellas.j2ee.util.sql.DataSourceLocator;
import es.udc.fbellas.j2ee.util.sql.PlainActionProcessor;

public class PlainAdminFacadeDelegate implements AdminFacadeDelegate {

        private DataSource getDataSource() throws InternalErrorException {
                return DataSourceLocator
                                .getDataSource(GlobalNames.UBET_DATA_SOURCE);
        }

        public EventTO insertEvent(EventTO eventTO, BetTypeTO betTypeTO,
                        List<BetOptionTO> options)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                try {

                        InsertEventAction action = new InsertEventAction(
                                        eventTO, betTypeTO, options);

                        return (EventTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (DuplicateInstanceException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public BetTypeTO insertBetType(BetTypeTO betTypeTO,
                        List<BetOptionTO> options)
                        throws DuplicateInstanceException,
                        InternalErrorException {
                try {

                        InsertBetTypeAction action = new InsertBetTypeAction(
                                        betTypeTO, options);

                        return (BetTypeTO) PlainActionProcessor.process(
                                        getDataSource(), action);

                } catch (DuplicateInstanceException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void publishResults(List<Long> winnerOptions)
                        throws InternalErrorException {
                try {

                        PublishResultsAction action = new PublishResultsAction(
                                        winnerOptions);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

        public void setHighlighted(Long eventID, boolean highlighted)
                        throws InstanceNotFoundException,
                        InternalErrorException {
                try {

                        SetHighlightedAction action = new SetHighlightedAction(
                                        eventID, highlighted);

                        PlainActionProcessor.process(getDataSource(), action);

                } catch (InstanceNotFoundException e) {
                        throw e;
                } catch (InternalErrorException e) {
                        throw e;
                } catch (Exception e) {
                        throw new InternalErrorException(e);
                }
        }

}

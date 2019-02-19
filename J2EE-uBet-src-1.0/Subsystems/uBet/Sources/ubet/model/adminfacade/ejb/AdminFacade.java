package ubet.model.adminfacade.ejb;

import java.util.List;

import ubet.model.betoption.to.BetOptionTO;
import ubet.model.bettype.to.BetTypeTO;
import ubet.model.event.to.EventTO;
import es.udc.fbellas.j2ee.util.exceptions.DuplicateInstanceException;
import es.udc.fbellas.j2ee.util.exceptions.InstanceNotFoundException;
import es.udc.fbellas.j2ee.util.exceptions.InternalErrorException;

public interface AdminFacade {

        /**
         * Inserts an event in the database.
         * 
         * @param eventTO
         *                a transfer object with all of the event data, but the
         *                ID
         * @param betTypeTO
         *                a transfer object with all of the bet type data, but
         *                the ID, of the default bet type associated with the
         *                event
         * @param options
         *                a collection with all of the bet options data of the
         *                default bet type associated with the event
         * @return a transfer object with all of the event data, including the
         *         <code>eventID</code> and the <code>betTypeID</code>
         * @throws DuplicateInstanceException
         *                 if there is another event (or another bet type) with
         *                 the same ID in the database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public EventTO insertEvent(EventTO eventTO, BetTypeTO betTypeTO,
                        List<BetOptionTO> options)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        /**
         * Inserts a bet type in the database.
         * 
         * @param betTypeTO
         *                a transfer object with all of the bet type data, but
         *                the ID
         * @param options
         *                a collection with all of the bet options data of the
         *                bet type
         * @return the same transfer object but with all of the bet type data
         *         (including the ID)
         * @throws DuplicateInstanceException
         *                 if there is another bet type with the same ID in the
         *                 database
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public BetTypeTO insertBetType(BetTypeTO betTypeTO,
                        List<BetOptionTO> options)
                        throws DuplicateInstanceException,
                        InternalErrorException;

        /**
         * Publish the results of an bet type
         * 
         * @param winnerOptions
         *                a collection with the identifiers of the winner
         *                options
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void publishResults(List<Long> winnerOptions)
                        throws InternalErrorException;

        /**
         * Set or unset highlighted an event
         * 
         * @param eventID
         *                the identifier of the event to be set or unset
         *                highlighted
         * @param highlighted
         *                <code>true</code> if we want the event to be set
         *                highlighted<br>
         *                <code>false</code> otherwise
         * @throws InstanceNotFoundException
         *                 if there is no event with the eventID
         * @throws InternalErrorException
         *                 if a severe error occurs
         */
        public void setHighlighted(Long eventID, boolean highlighted)
                        throws InstanceNotFoundException,
                        InternalErrorException;

}

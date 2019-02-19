package ubet.model.searchfacade.to;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

import ubet.model.util.DateOperations;

/**
 * A custom transfer object representing the information of an event.
 * <p>
 * NOTES:
 * <ul>
 * <li> <b>WARNING:</b> The constructor creates a date equals to the one passed
 * as a parameter, but with the <code>Calendar.MILLISECOND</code> field set to
 * 0, since some JDBC drivers (example: the one that comes with PostgreSQL)
 * throw an <code>SQLException</code> when invoking
 * <code>ResultSet.getTimestamp(int)</code> over an attribute that has
 * milliseconds (<code>PreparedStatement.setTimestamp(int, Timestamp)</code>
 * may be invoked, but not <code>ResultSet.getTimestamp(int)</code>), which
 * indicates a parsing error in the date (since the implementation of
 * <code>ResultSet.getTimestamp(int)</code> does not expect milliseconds).
 * </li>
 * <br>
 * </ul>
 * </li>
 * </ul>
 */
public class EventResultTO implements Serializable {

        /**
         * The event identifier
         */
        private Long eventID;

        /**
         * The description of the event
         */
        private String description;

        /**
         * The date when the event starts
         */
        private Calendar date;

        /**
         * The category associated with the event
         */
        private CategoryChunkTO category;

        /**
         * The default bet type associated with the event
         */
        private BetTypeResultTO betType;

        /**
         * Indicates if the event is highlighted. It could be
         * <code>EventTO.YES</code>, <code>EventTO.NO</code>,
         * <code>EventTO.READY</code> or <code>EventTO.WAITING</code>
         */
        private String highlighted;

        /**
         * A constructor to create <code>EventTO</code> objects
         * 
         * @param eventID
         *                the event identifier
         * @param description
         *                the description of the event
         * @param date
         *                the date when the event starts
         * @param category
         *                the category associated with the event
         * @param betType
         *                the default bet type associated with the event
         * @param highlighted
         *                <code>EventTO.YES</code>, <code>EventTO.NO</code>,
         *                <code>EventTO.READY</code> or
         *                <code>EventTO.WAITING</code>
         */
        public EventResultTO(Long eventID, String description, Calendar date,
                        CategoryChunkTO category, BetTypeResultTO betType,
                        String highlighted) {
                this.eventID = eventID;
                this.description = description;
                Calendar dateWithoutMilliSeconds = DateOperations
                                .getDateWithoutMilliSeconds(date);
                this.date = dateWithoutMilliSeconds;
                this.category = category;
                this.betType = betType;
                this.highlighted = highlighted;
        }

        /**
         * @return the <code>eventID</code>
         */
        public Long getEventID() {
                return this.eventID;
        }

        /**
         * @return the <code>description</code>
         */
        public String getDescription() {
                return this.description;
        }

        /**
         * @return the <code>date</code>
         */
        public Calendar getDate() {
                return this.date;
        }

        /**
         * @return the <code>category</code>
         */
        public CategoryChunkTO getCategory() {
                return this.category;
        }

        /**
         * @return the <code>betType</code>
         */
        public BetTypeResultTO getBetType() {
                return this.betType;
        }

        /**
         * @return <code>EventTO.YES</code><br>
         *         <code>EventTO.NO</code><br>
         *         <code>EventTO.READY</code><br>
         *         <code>EventTO.WAITING</code>
         */
        public String getHighlighted() {
                return this.highlighted;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof EventResultTO) {
                        EventResultTO event = (EventResultTO) object;
                        return (event.getBetType().equals(this.betType)
                                        && event.getCategory().equals(
                                                        this.category)
                                        && event.getDate().equals(this.date)
                                        && event.getDescription().equals(
                                                        this.description)
                                        && event.getEventID().equals(
                                                        this.eventID) && event
                                        .getHighlighted().equals(
                                                        this.highlighted));
                }

                return false;
        }

        @Override
        public String toString() {
                DateFormat dateFormater = DateFormat.getDateTimeInstance();
                String dateString = dateFormater.format(date.getTime());

                return ("\nBetType = " + this.betType + "\nCategory = "
                                + this.category + "\nDate = " + dateString
                                + "\nDescription = " + this.description
                                + "\nEvent ID = " + this.eventID
                                + "\nHighlighted = " + this.highlighted + "\n");
        }

}

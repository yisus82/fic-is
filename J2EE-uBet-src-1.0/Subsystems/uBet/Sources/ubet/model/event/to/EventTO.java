package ubet.model.event.to;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;

import ubet.model.util.DateOperations;

/**
 * A transfer object representing the information of an event.
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
 * <li> The <code>highlighted</code> status of the event may be one of the
 * following:
 * <ul>
 * <li><code>YES</code> (It is one of the three highlighted events at this
 * moment)</li>
 * <li><code>NO</code> (It will never be one of the three highlighted events)</li>
 * <li><code>READY</code> (It is ready to become one of the three highlighted
 * events)</li>
 * <li><code>WAITING</code> (It is waiting to become one of the three
 * highlighted events again)</li>
 * </ul>
 * </li>
 * </ul>
 */
public class EventTO implements Serializable {

        /**
         * A constant used when the event is one of the three highlighted events
         * at this moment
         */
        public static String YES = "Y";

        /**
         * A constant used whe the event will never be one of the three
         * highlighted events
         */
        public static String NO = "N";

        /**
         * A constant used when the event is ready to become one of the three
         * highlighted events
         */
        public static String READY = "R";

        /**
         * A constant used when the event is waiting to become one of the three
         * highlighted events again
         */
        public static String WAITING = "W";

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
         * The identifier of the category associated with the event
         */
        private String categoryID;

        /**
         * The identifier of the default bet type associated with the event
         */
        private Long betTypeID;

        /**
         * Indicates if the event is highlighted. It could be <code>YES</code>,
         * <code>NO</code>, <code>READY</code> or <code>WAITING</code>
         */
        private String highlighted;

        /**
         * The date when the event was inserted into the DB
         */
        private Calendar insertionDate;

        /**
         * A constructor to create <code>EventTO</code> objects
         * 
         * @param eventID
         *                the event identifier
         * @param description
         *                the description of the event
         * @param date
         *                the date when the event starts
         * @param categoryID
         *                the identifier of the category associated with the
         *                event
         * @param betTypeID
         *                the identifier of the default bet type associated with
         *                the event
         * @param highlighted
         *                <code>YES</code>, <code>NO</code>,
         *                <code>READY</code> or <code>WAITING</code>
         * @param insertionDate
         *                the date when the event was inserted into the DB
         */
        public EventTO(Long eventID, String description, Calendar date,
                        String categoryID, Long betTypeID, String highlighted,
                        Calendar insertionDate) {
                this.eventID = eventID;
                this.description = description;
                Calendar dateWithoutMilliSeconds = DateOperations
                                .getDateWithoutMilliSeconds(date);
                this.date = dateWithoutMilliSeconds;
                this.categoryID = categoryID;
                this.betTypeID = betTypeID;
                this.highlighted = highlighted;
                dateWithoutMilliSeconds = DateOperations
                                .getDateWithoutMilliSeconds(insertionDate);
                this.insertionDate = dateWithoutMilliSeconds;
        }

        /**
         * @return the <code>eventID</code>
         */
        public Long getEventID() {
                return this.eventID;
        }

        /**
         * @param eventID
         *                the eventID to set
         */
        public void setEventID(Long eventID) {
                this.eventID = eventID;
        }

        /**
         * @return the <code>description</code>
         */
        public String getDescription() {
                return this.description;
        }

        /**
         * @param description
         *                the descripiton to set
         */
        public void setDescription(String description) {
                this.description = description;
        }

        /**
         * @return the <code>date</code>
         */
        public Calendar getDate() {
                return this.date;
        }

        /**
         * @param date
         *                the date to set
         */
        public void setDate(Calendar date) {
                this.date = date;
        }

        /**
         * @return the <code>categoryID</code>
         */
        public String getCategoryID() {
                return this.categoryID;
        }

        /**
         * @param categoryID
         *                the categoryID to set
         */
        public void setCategoryID(String categoryID) {
                this.categoryID = categoryID;
        }

        /**
         * @return the <code>betTypeID</code>
         */
        public Long getBetTypeID() {
                return this.betTypeID;
        }

        /**
         * @param betTypeID
         *                the betTypeID to set
         */
        public void setBetTypeID(Long betTypeID) {
                this.betTypeID = betTypeID;
        }

        /**
         * @return <code>YES</code><br>
         *         <code>NO</code><br>
         *         <code>READY</code><br>
         *         <code>WAITING</code>
         */
        public String getHighlighted() {
                return this.highlighted;
        }

        /**
         * @param highlighted
         *                <code>YES</code>, <code>NO</code>,
         *                <code>READY</code> or <code>WAITING</code>
         */
        public void setHighlighted(String highlighted) {
                this.highlighted = highlighted;
        }

        /**
         * @return the <code>insertionDate</code>
         */
        public Calendar getInsertionDate() {
                return this.insertionDate;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof EventTO) {
                        EventTO event = (EventTO) object;
                        return (event.getBetTypeID().equals(this.betTypeID)
                                        && event.getCategoryID().equals(
                                                        this.categoryID)
                                        && event.getDate().equals(this.date)
                                        && event.getDescription().equals(
                                                        this.description)
                                        && event.getEventID().equals(
                                                        this.eventID)
                                        && event.getHighlighted().equals(
                                                        this.highlighted) && event
                                        .getInsertionDate().equals(
                                                        this.insertionDate));
                }

                return false;
        }

        @Override
        public String toString() {
                DateFormat dateFormater = DateFormat.getDateTimeInstance();
                String dateString = dateFormater.format(date.getTime());
                String insertionDateString = dateFormater.format(insertionDate
                                .getTime());

                return ("\nBetTypeID = " + this.betTypeID + "\nCategoryID = "
                                + this.categoryID + "\nDate = " + dateString
                                + "\nDescription = " + this.description
                                + "\nEvent ID = " + this.eventID
                                + "\nHighlighted = " + this.highlighted
                                + "\nInsertion Date = " + insertionDateString + "\n");
        }

}

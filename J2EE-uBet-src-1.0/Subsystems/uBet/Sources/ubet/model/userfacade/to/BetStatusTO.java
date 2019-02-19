package ubet.model.userfacade.to;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * A custom transfer object representing the relevant data of a bet.
 */
public class BetStatusTO implements Serializable {

        /**
         * The bet identifier
         */
        private Long betID;

        /**
         * The date when the bet was made
         */
        private Calendar date;

        /**
         * The description of the event associated with the bet
         */
        private String event;

        /**
         * The date when the event associated with the bet starts
         */
        private Calendar eventDate;

        /**
         * The description of the question associated with the bet
         */
        private String question;

        /**
         * The description of the bet option associated with the bet
         */
        private String chosenOption;

        /**
         * The ratio associated with the bet
         */
        private Double odds;

        /**
         * A collection of <code>String</code> with the descriptions of the
         * winner options of the bet type associated with the bet. If the event
         * isn't over this collection has zero elements
         */
        private List<String> winnerOptions;

        /**
         * The current status of the bet. It could be <code>GAINED</code>,
         * <code>PENDING</code> or <code>LOST</code>
         */
        private String status;

        /**
         * A constructor to create <code>BetStatusTO</code> objects
         * 
         * @param betID
         *                the bet identifier
         * @param date
         *                the date when the bet was made
         * @param event
         *                the description of the event associated with the bet
         * @param eventDate
         *                the date when the event associated with the bet starts
         * @param question
         *                the description of the question associated with the
         *                bet
         * @param chosenOption
         *                the description of the bet option associated with the
         *                bet
         * @param odds
         *                the ratio associated with the bet
         * @param winnerOptions
         *                a collection of <code>String</code> with the
         *                descriptions of the winner options of the bet type
         *                associated with the bet. If the event isn't over this
         *                collection has zero elements
         * @param status
         *                the current status of the bet. It could be
         *                <code>GAINED</code>, <code>PENDING</code> or
         *                <code>LOST</code>
         */
        public BetStatusTO(Long betID, Calendar date, String event,
                        Calendar eventDate, String question,
                        String chosenOption, Double odds,
                        List<String> winnerOptions, String status) {
                this.betID = betID;
                this.date = date;
                this.event = event;
                this.eventDate = eventDate;
                this.question = question;
                this.chosenOption = chosenOption;
                this.odds = odds;
                this.winnerOptions = winnerOptions;
                this.status = status;
        }

        /**
         * @return the <code>betID</code>
         */
        public Long getBetID() {
                return this.betID;
        }

        /**
         * @return the <code>date</code>
         */
        public Calendar getDate() {
                return this.date;
        }

        /**
         * @return the <code>event</code>
         */
        public String getEvent() {
                return this.event;
        }

        /**
         * @return the <code>eventDate</code>
         */
        public Calendar getEventDate() {
                return this.eventDate;
        }

        /**
         * @return the <code>question</code>
         */
        public String getQuestion() {
                return this.question;
        }

        /**
         * @return the <code>chosenOption</code>
         */
        public String getChosenOption() {
                return this.chosenOption;
        }

        /**
         * @return the <code>odds</code>
         */
        public Double getOdds() {
                return this.odds;
        }

        /**
         * @return the <code>winnerOptions</code>
         */
        public List<String> getWinnerOptions() {
                return this.winnerOptions;
        }

        /**
         * @return the <code>status</code>
         */
        public String getStatus() {
                return this.status;
        }

        @Override
        public boolean equals(Object object) {
                if (this == object)
                        return true;

                if (object instanceof BetStatusTO) {
                        BetStatusTO betStatus = (BetStatusTO) object;
                        return (betStatus.getBetID().equals(this.betID)
                                        && betStatus.getChosenOption().equals(
                                                        this.chosenOption)
                                        && betStatus.getDate()
                                                        .equals(this.date)
                                        && betStatus.getEvent().equals(
                                                        this.event)
                                        && betStatus.getEventDate().equals(
                                                        this.eventDate)
                                        && betStatus.getOdds()
                                                        .equals(this.odds)
                                        && betStatus.getQuestion().equals(
                                                        this.question)
                                        && betStatus.getStatus().equals(
                                                        this.status) && betStatus
                                        .getWinnerOptions().equals(
                                                        this.winnerOptions));
                }
                return false;
        }

        @Override
        public String toString() {
                DateFormat dateFormater = DateFormat.getDateTimeInstance();
                String dateString = dateFormater.format(date.getTime());
                String eventDateString = dateFormater.format(eventDate
                                .getTime());

                return ("\nBet ID = " + this.betID + "\nChosen option = "
                                + this.chosenOption + "\nDate = " + dateString
                                + "\nEvent = " + this.event + "\nEvent date ="
                                + eventDateString + "\nOdds = " + this.odds
                                + "\nQuestion = " + this.question
                                + "\nStatus = " + this.status
                                + "\nWinner options = " + this.winnerOptions + "\n");
        }

}

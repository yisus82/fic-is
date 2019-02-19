package ibei.model.vote.to;

import java.io.Serializable;
import java.util.Calendar;

public class VoteTO implements Serializable {

        public static final int POSITIVE = 1;

        public static final int NEUTRAL = 0;

        public static final int NEGATIVE = -1;

        public static final String BUYER = "B";

        public static final String SELLER = "S";

        private Long voteID;

        private int rating;

        private String comment;

        private String voterID;

        private String type;

        private Calendar date;

        private String votedID;

        private Long productID;

        public VoteTO(Long voteID, int rating, String comment, String voterID,
                        String type, Calendar date, String votedID,
                        Long productID) {
                this.voteID = voteID;
                this.rating = rating;
                this.comment = comment;
                this.voterID = voterID;
                this.type = type;
                this.date = date;
                this.votedID = votedID;
                this.productID = productID;
        }

        public Long getVoteID() {
                return this.voteID;
        }

        public void setVoteID(Long voteID) {
                this.voteID = voteID;
        }

        public int getRating() {
                return this.rating;
        }

        public String getComment() {
                return this.comment;
        }

        public String getVoterID() {
                return this.voterID;
        }

        public String getType() {
                return this.type;
        }

        public Calendar getDate() {
                return this.date;
        }

        public String getVotedID() {
                return this.votedID;
        }

        public Long getProductID() {
                return this.productID;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof VoteTO) {
                        VoteTO vote = (VoteTO) object;
                        return (vote.getComment().equals(comment)
                                        && vote.getDate().equals(date)
                                        && vote.getProductID()
                                                        .equals(productID)
                                        && (vote.getRating() == rating)
                                        && vote.getType().equals(type)
                                        && vote.getVotedID().equals(votedID)
                                        && vote.getVoteID().equals(voteID) && vote
                                        .getVoterID().equals(voterID));
                }

                return false;

        }

}

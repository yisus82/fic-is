package ubet.model.userfacade.to;

import java.io.Serializable;
import java.util.List;

public class BetChunkTO implements Serializable {

        private List<BetStatusTO> betStatusTOs;

        private boolean existMoreBetStatus;

        public BetChunkTO(List<BetStatusTO> betStatusTOs,
                        boolean existMoreBetStatus) {
                this.betStatusTOs = betStatusTOs;
                this.existMoreBetStatus = existMoreBetStatus;
        }

        public List<BetStatusTO> getBetStatusTOs() {
                return betStatusTOs;
        }

        public boolean getExistMoreBetStatus() {
                return existMoreBetStatus;
        }

        public String toString() {
                return new String("betStatusTOs = " + betStatusTOs + " | "
                                + "existMoreBetStatus = " + existMoreBetStatus);
        }

}

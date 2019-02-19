package ubet.model.searchfacade.to;

import java.io.Serializable;
import java.util.List;

public class BetTypeChunkTO implements Serializable {

        private List<BetTypeResultTO> betTypeTOs;

        private boolean existMoreBetTypes;

        public BetTypeChunkTO(List<BetTypeResultTO> betTypeTOs,
                        boolean existMoreAccounts) {
                this.betTypeTOs = betTypeTOs;
                this.existMoreBetTypes = existMoreAccounts;
        }

        public List<BetTypeResultTO> getBetTypeTOs() {
                return betTypeTOs;
        }

        public boolean getExistMoreBetTypes() {
                return existMoreBetTypes;
        }

        public String toString() {
                return new String("betTypeTOs = " + betTypeTOs + " | "
                                + "existMoreBetTypes = " + existMoreBetTypes);
        }

}

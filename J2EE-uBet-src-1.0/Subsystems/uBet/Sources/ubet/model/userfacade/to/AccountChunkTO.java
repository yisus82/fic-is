package ubet.model.userfacade.to;

import java.io.Serializable;
import java.util.List;

import ubet.model.account.to.AccountTO;

public class AccountChunkTO implements Serializable {

        private List<AccountTO> accountTOs;

        private boolean existMoreAccounts;

        public AccountChunkTO(List<AccountTO> accountTOs,
                        boolean existMoreAccounts) {
                this.accountTOs = accountTOs;
                this.existMoreAccounts = existMoreAccounts;
        }

        public List<AccountTO> getAccountTOs() {
                return accountTOs;
        }

        public boolean getExistMoreAccounts() {
                return existMoreAccounts;
        }

        public String toString() {
                return new String("accountTOs = " + accountTOs + " | "
                                + "existMoreAccounts = " + existMoreAccounts);
        }

}

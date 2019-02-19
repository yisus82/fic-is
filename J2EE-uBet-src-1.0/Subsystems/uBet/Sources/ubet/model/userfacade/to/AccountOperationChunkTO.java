package ubet.model.userfacade.to;

import java.io.Serializable;
import java.util.List;

import ubet.model.accountoperation.to.AccountOperationTO;

public class AccountOperationChunkTO implements Serializable {

        private List<AccountOperationTO> accountOperationTOs;

        private boolean existMoreAccountOperations;

        public AccountOperationChunkTO(
                        List<AccountOperationTO> accountOperationTOs,
                        boolean existMoreAccountOperations) {
                this.accountOperationTOs = accountOperationTOs;
                this.existMoreAccountOperations = existMoreAccountOperations;
        }

        public List<AccountOperationTO> getAccountOperationTOs() {
                return accountOperationTOs;
        }

        public boolean getExistMoreAccountOperations() {
                return existMoreAccountOperations;
        }

        public String toString() {
                return new String("accountOperationTOs = "
                                + accountOperationTOs + " | "
                                + "existMoreAccountOperations = "
                                + existMoreAccountOperations);
        }

}

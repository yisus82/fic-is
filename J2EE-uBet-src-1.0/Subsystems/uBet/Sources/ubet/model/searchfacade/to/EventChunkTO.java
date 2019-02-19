package ubet.model.searchfacade.to;

import java.io.Serializable;
import java.util.List;

public class EventChunkTO implements Serializable {

        private List<EventResultTO> eventTOs;

        private boolean existMoreEvents;

        public EventChunkTO(List<EventResultTO> eventTOs,
                        boolean existMoreAccounts) {
                this.eventTOs = eventTOs;
                this.existMoreEvents = existMoreAccounts;
        }

        public List<EventResultTO> getEventTOs() {
                return eventTOs;
        }

        public boolean getExistMoreEvents() {
                return existMoreEvents;
        }

        public String toString() {
                return new String("eventTOs = " + eventTOs + " | "
                                + "existMoreEvents = " + existMoreEvents);
        }

}

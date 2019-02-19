package ibei.model.increment.to;

import java.io.Serializable;

public class IncrementTO implements Serializable {

        private Double minValue;

        private Double maxValue;

        private Double increment;

        public IncrementTO(Double minValue, Double maxValue, Double increment) {
                this.minValue = minValue;
                this.maxValue = maxValue;
                this.increment = increment;
        }

        public Double getMinValue() {
                return this.minValue;
        }

        public Double getMaxValue() {
                return this.maxValue;
        }

        public Double getIncrement() {
                return this.increment;
        }

        public boolean equals(Object object) {

                if (this == object)
                        return true;

                if (object instanceof IncrementTO) {
                        IncrementTO incrementVO = (IncrementTO) object;
                        return (incrementVO.getIncrement().equals(increment)
                                        && incrementVO.getMaxValue().equals(
                                                        maxValue) && incrementVO
                                        .getMinValue().equals(minValue));
                }

                return false;

        }

        public String toString() {
                return ("\nIncrement = " + this.increment + "\nMax value = "
                                + this.maxValue + "\nMin value = "
                                + this.minValue + "\n");
        }

}

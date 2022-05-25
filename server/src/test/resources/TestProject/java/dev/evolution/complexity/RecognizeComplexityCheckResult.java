package dev.evolution.complexity;

import java.util.Arrays;
import java.util.stream.Stream;

public class RecognizeComplexityCheckResult {
    private int baseline;
    private String message;
    private Item[] items = new Item[]{};

    class Item {
        private String method;
        private int pre;
        private int current;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public int getPre() {
            return pre;
        }

        public void setPre(int pre) {
            this.pre = pre;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "method='" + method + '\'' +
                    ", pre=" + pre +
                    ", current=" + current +
                    '}';
        }
    }

    public boolean isOk() {
        return items == null || items.length == 0;
    }
    public String getMessage() {
        if(items != null && items.length > 0) {
            return "ok";
        } else {
            return "error: " + message;
        }
    }
    public RecognizeComplexityCheckResult(String message) {
        this.message = message;
    }

    public RecognizeComplexityCheckResult addItems(Item[] items) {
        this.items = (Item[]) Stream.concat(Arrays.stream(this.items), Arrays.stream(items)).toArray();
        return this;
    }

    @Override
    public String toString() {
        return "RecognizeComplexityCheckResult{" +
                "message=" + getMessage() +
                ", baseline=" + baseline +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}

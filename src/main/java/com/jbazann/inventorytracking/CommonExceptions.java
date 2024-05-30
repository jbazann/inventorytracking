package com.jbazann.inventorytracking;

public abstract class CommonExceptions {
    
    static public IllegalArgumentException nullArgument(String arg) {
        return new IllegalArgumentException(
                arg == null ? "Unexpected null argument found."
                    :   arg+" can't be null."
        );
    }

    static public IllegalArgumentException nullArgument() {
        return nullArgument(null);
    }

    static public IllegalArgumentException invalidPage() {
        return new IllegalArgumentException("page can't be less than zero.");
    }

    static public IllegalArgumentException invalidPageSize() {
        return new IllegalArgumentException("pageSize can't be less than one.");
    }



}

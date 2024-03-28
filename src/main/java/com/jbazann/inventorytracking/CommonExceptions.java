package com.jbazann.inventorytracking;

public abstract class CommonExceptions {
    
    static public String nullArgument(String arg) {
        return arg == "" ? "Unexpected null argument found."
            :   arg+" can't be null.";
    }

    static public String nullArgument() {
        return nullArgument("");
    }

}

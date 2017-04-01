package com.jackiez.zgithub.util;

import android.util.Printer;

/**
 * Created by zsigui on 17-3-30.
 */

public class LaggyPrinter implements Printer {

    private final String START = ">>>>> Dispatching";
    private final String END = "<<<<< Finished";

    public LaggyPrinter() {
    }

    @Override
    public void println(String msg) {
        if (msg.startsWith(START)) {
            LaggyMonitor.get().startMonitor();
        } else if (msg.startsWith(END)) {
            LaggyMonitor.get().stopMonitor();
        }
    }
}

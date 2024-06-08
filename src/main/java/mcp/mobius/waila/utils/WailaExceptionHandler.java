package mcp.mobius.waila.utils;

import java.util.ArrayList;
import java.util.List;

import mcp.mobius.waila.api.BackwardCompatibility;
import org.apache.logging.log4j.Level;

import mcp.mobius.waila.Waila;

@BackwardCompatibility
public class WailaExceptionHandler {

    public WailaExceptionHandler() {}

    private static ArrayList<String> errs = new ArrayList<String>();

    public static List<String> handleErr(Throwable e, String className, List<String> currenttip) {
        if (!errs.contains(className)) {
            errs.add(className);

            for (StackTraceElement elem : e.getStackTrace()) {
                Waila.log.log(
                        Level.WARN,
                        String.format("%s.%s:%s", elem.getClassName(), elem.getMethodName(), elem.getLineNumber()));
                if (elem.getClassName().contains("waila")) break;
            }

            Waila.log.log(Level.WARN, String.format("Catched unhandled exception : [%s] %s", className, e));
        }
        if (currenttip != null) currenttip.add("<ERROR>");

        return currenttip;
    }

}

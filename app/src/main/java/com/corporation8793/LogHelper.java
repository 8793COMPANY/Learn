package com.corporation8793;

import android.os.Environment;

import androidx.annotation.NonNull;

import org.apache.log4j.lf5.Log4JLogRecord;

import java.io.PrintWriter;
import java.io.StringWriter;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class LogHelper implements Thread.UncaughtExceptionHandler {

    private final String LINE_SEPARATOR="\n";

    private org.apache.log4j.Logger mLogger = org.apache.log4j.Logger.getLogger(LogHelper.class);
    String pattern = "[%d{yyyy-MM-dd HH:mm:ss}] %-5p [%l] - %m%n";

    public LogHelper(){

        final LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Environment.getExternalStorageDirectory()+ "/logfile.log");
        //   logConfigurator.setFilePattern(pattern);
        logConfigurator.configure();

    }
    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append(stackTrace.toString());

        mLogger.error(errorReport);



        //   android.os.Process.killProcess(android.os.Process.myPid());
        //   System.exit(10);
    }
}

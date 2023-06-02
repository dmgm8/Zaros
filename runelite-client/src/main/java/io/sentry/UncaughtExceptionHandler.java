/*
 * Decompiled with CFR 0.150.
 */
package io.sentry;

interface UncaughtExceptionHandler {
    public Thread.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler();

    public void setDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler var1);

    public static final class Adapter
    implements UncaughtExceptionHandler {
        private static final Adapter INSTANCE = new Adapter();

        static UncaughtExceptionHandler getInstance() {
            return INSTANCE;
        }

        private Adapter() {
        }

        @Override
        public Thread.UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
            return Thread.getDefaultUncaughtExceptionHandler();
        }

        @Override
        public void setDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler) {
            Thread.setDefaultUncaughtExceptionHandler(handler);
        }
    }
}


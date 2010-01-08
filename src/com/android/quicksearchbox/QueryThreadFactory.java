/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.quicksearchbox;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A thread factory that creates background threads with unique names.
 *
 */
public class QueryThreadFactory implements ThreadFactory {

    /** Used to give a unique name to each thread. */
    private final AtomicInteger mCount = new AtomicInteger(1);

    private final int mPriority;

    /**
     * Creates a new thread factory.
     *
     * @param priority The thread priority of the threads created by this factory.
     *        For values, see {@link android.os.Process}.
     */
    public QueryThreadFactory(int priority) {
        mPriority = priority;
    }

    public Thread newThread(Runnable r) {
        return new QueryThread(r, "QSB #" + mCount.getAndIncrement(), mPriority);
    }

    /**
     * A thread with a set name and priority.
     */
    private static class QueryThread extends Thread {

        private final int mPriority;

        private QueryThread(Runnable runnable, String threadName, int priority) {
            super(runnable, threadName);
            mPriority = priority;
        }

        @Override
        public void run() {
            // take it easy on the UI thread
            android.os.Process.setThreadPriority(mPriority);
            super.run();
        }
    }
}
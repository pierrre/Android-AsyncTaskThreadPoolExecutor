package org.pierrre.attpe;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import android.os.AsyncTask;
import android.util.Log;

public final class AsyncTaskThreadPoolExecutorHelper {
	private static final String LOG_TAG = "AsyncTaskThreadPoolExecutorHelper";
	
	private static Executor threadPoolExecutor = null;
	private static Method executeOnExecutorMethod = null;
	
	static {
		try {
			AsyncTaskThreadPoolExecutorHelper.threadPoolExecutor = (Executor) AsyncTask.class.getField("THREAD_POOL_EXECUTOR").get(null);
			AsyncTaskThreadPoolExecutorHelper.executeOnExecutorMethod = AsyncTask.class.getMethod("executeOnExecutor", Executor.class, Object[].class);
		} catch (Exception e) {
			Log.d(AsyncTaskThreadPoolExecutorHelper.LOG_TAG, "Thread pool executor not available");
		}
	}
	
	private AsyncTaskThreadPoolExecutorHelper() {
	}
	
	@SuppressWarnings("unchecked")
	public static <Params, Progress, Result> AsyncTask<Params, Progress, Result> execute(AsyncTask<Params, Progress, Result> asyncTask, Params... params) {
		if (AsyncTaskThreadPoolExecutorHelper.threadPoolExecutor != null && AsyncTaskThreadPoolExecutorHelper.executeOnExecutorMethod != null) {
			try {
				return (AsyncTask<Params, Progress, Result>) AsyncTaskThreadPoolExecutorHelper.executeOnExecutorMethod.invoke(asyncTask, AsyncTaskThreadPoolExecutorHelper.threadPoolExecutor, params);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return asyncTask.execute(params);
		}
	}
}

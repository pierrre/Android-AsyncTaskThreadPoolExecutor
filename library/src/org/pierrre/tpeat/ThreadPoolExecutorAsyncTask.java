package org.pierrre.tpeat;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import android.os.AsyncTask;

public abstract class ThreadPoolExecutorAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	private static Method executeOnExecutorMethod;
	private static Executor threadPoolExecutor;
	
	static {
		try {
			ThreadPoolExecutorAsyncTask.executeOnExecutorMethod = AsyncTask.class.getMethod("executeOnExecutor", Executor.class, Object[].class);
			ThreadPoolExecutorAsyncTask.threadPoolExecutor = (Executor) AsyncTask.class.getField("THREAD_POOL_EXECUTOR").get(null);
		} catch (Exception e) {
			ThreadPoolExecutorAsyncTask.executeOnExecutorMethod = null;
			ThreadPoolExecutorAsyncTask.threadPoolExecutor = null;
		}
	}
	
	public final void executeOnThreadPoolExecutor(Params... params) {
		if (ThreadPoolExecutorAsyncTask.executeOnExecutorMethod != null && ThreadPoolExecutorAsyncTask.threadPoolExecutor != null) {
			try {
				ThreadPoolExecutorAsyncTask.executeOnExecutorMethod.invoke(this, ThreadPoolExecutorAsyncTask.threadPoolExecutor, params);
				
				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.execute(params);
	}
}

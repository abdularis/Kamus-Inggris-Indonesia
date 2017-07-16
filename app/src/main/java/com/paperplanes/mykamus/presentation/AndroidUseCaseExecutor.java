package com.paperplanes.mykamus.presentation;

import android.os.Handler;

import com.paperplanes.mykamus.domain.usecases.UseCase;
import com.paperplanes.mykamus.domain.usecases.UseCaseExecutor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by abdularis on 04/07/17.
 */

public class AndroidUseCaseExecutor implements UseCaseExecutor {

    private static final int POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 4;
    private static final int TIMEOUT = 30;

    private final Handler mHandler = new Handler();

    private ThreadPoolExecutor mThreadPool;

    public AndroidUseCaseExecutor() {
        mThreadPool =
                new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT, TimeUnit.SECONDS,
                        new ArrayBlockingQueue<Runnable>(MAX_POOL_SIZE));
    }

    @Override
    public <P extends UseCase.Params, O extends UseCase.Result>
    void execute(final UseCase<P, O> useCase, UseCase.Callback<O> callback) {
        useCase.setCallback(new UiThreadCallbackAdapter<>(callback));

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                useCase.run();
            }
        });
    }

    private final class UiThreadCallbackAdapter
            <O extends UseCase.Result> implements UseCase.Callback<O> {

        UseCase.Callback<O> mCallback;

        UiThreadCallbackAdapter(UseCase.Callback<O> callback) {
            mCallback = callback;
        }

        @Override
        public void onSuccess(final O result) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onSuccess(result);
                }
            });
        }

        @Override
        public void onFailed(final Throwable err) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.onFailed(err);
                }
            });
        }
    }
}

package com.paperplanes.mykamus.domain.usecases;

/**
 * Created by abdularis on 08/07/17.
 */

public abstract class UseCase<P extends UseCase.Params, O extends UseCase.Result> {

    private Callback<O> mCallback;
    private P mParams;

    public void run() {
        execute(mParams);
    }

    protected abstract void execute(P p);

    public Callback<O> getCallback() {
        return mCallback;
    }

    public void setCallback(Callback<O> callback) {
        mCallback = callback;
    }

    public P getParams() {
        return mParams;
    }

    public void setParams(P params) {
        mParams = params;
    }

    public interface Callback<O extends Result> {
        void onSuccess(O result);
        void onFailed(Throwable err);
    }

    public static class CbAdapter<O extends Result> implements Callback<O> {
        @Override
        public void onSuccess(O result) {}

        @Override
        public void onFailed(Throwable err) {}
    }

    public interface Params {}
    public interface Result {}
}

package com.paperplanes.mykamus.domain.usecases;

/**
 * Created by abdularis on 09/07/17.
 */

public interface UseCaseExecutor {

    <P extends UseCase.Params, O extends UseCase.Result>
    void execute(UseCase<P, O> useCase, UseCase.Callback<O> cb);

}

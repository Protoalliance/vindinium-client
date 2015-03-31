package com.protoalliance.vindiniumclient.bot.advanced.murderbot;

/**

 *
 * @param <S> state used to make a decision
 * @param <R> result of decision
 */
public interface Decision<S, R> {

    /**
     * Given a state, return a result
     * @param state
     * @return
     */
    public R makeDecision(S state);
}

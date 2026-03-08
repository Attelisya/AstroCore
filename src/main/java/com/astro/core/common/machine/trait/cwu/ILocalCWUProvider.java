package com.astro.core.common.machine.trait.cwu;

/**
 * Implemented by machines that provide CWU/t directly to an adjacent
 * CWUInputHatch without using the optical fiber network.
 * This is intentionally NOT IOpticalComputationProvider — it cannot bridge
 * into or out of the optical pipe network.
 */
public interface ILocalCWUProvider {

    /**
     * Request up to {@code cwut} CWU/t from this provider for the current tick.
     * The provider tracks its per-tick budget internally; subsequent calls in the
     * same tick will return 0 once the budget is exhausted.
     *
     * @param cwut     The maximum CWU/t the caller wants.
     * @param simulate If true, do not actually consume the budget.
     * @return How much CWU/t was (or would be) provided.
     */
    int requestCWUt(int cwut, boolean simulate);

    /**
     * The theoretical maximum CWU/t this provider can output per tick,
     * regardless of current budget state.
     */
    int getMaxCWUt();

    /**
     * Whether this provider is currently active and able to produce CWU/t.
     * Used by the hatch for display/diagnostic purposes.
     */
    boolean isProviderActive();
}

package org.defendev.hibernate.envers.demo.eaa.model;



public enum NormalBalance {
    /*
     * [EN] Side of a T-account where an account's balance is expected to be. Put simply, it's the side
     *  (debit or credit) that increases the account's value.
     *
     * [PL] W polskiej rachunkowości określenia "konto aktywne" i "konto pasywne" odnoszą się do
     *   "sposobu prowadzenia konta" (czyli tego, po której stronie zwykle występuje saldo i
     *   jak zwiększamy/zmniejszamy wartość).
     *   W języku angielskim:
     *   Nie używa się dosłownie terminów active account i passive account. Najczęściej spotyka się określenia
     *   opisowe:
     *     - Debit-balance account -> odpowiednik konta aktywnego (101 Cash)
     *     - Credit-balance account -> odpowiednik konta pasywnego (301 Owner's Capital)
     *
     */
    debit,
    credit,
    dual
}

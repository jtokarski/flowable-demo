package org.defendev.hibernate.envers.demo.eaa.model;

import static org.defendev.hibernate.envers.demo.eaa.model.AccountGroup.CurrentAssets;
import static org.defendev.hibernate.envers.demo.eaa.model.BalanceOrResultAccount.BalanceAccount;
import static org.defendev.hibernate.envers.demo.eaa.model.NormalBalance.debit;



/*
 * Note on naming:
 *   General ledger account = Synthetic account = Financial account
 *      (they all refer to the statutory accounts in the CoA)
 * But since "General ledger account" is the best name since it is universal term in Anglo-Saxon accounting
 * (as opposed to Continental European).
 *
 * GeneralLedgerAccount does not enumerate
 *   Subsidiary Ledger Accounts
 *   Analytic Accounts
 *
 * Instead of enum, this could be a database table.
 *
 *
 * [PL] Różnice Polsko-Amerykańskie wprowadzjące w błąd.
 *
 * 1. W polskim planie kont jest taka podwójna nieintujcyjność:
 *    200 Rozrachunki z odbiorcami (jest "aktywne")
 *    210 Rozrachunki z dostawcami (jest "pasywne")
 * A więc
 *   - "2" z przodu nie determinuje sposobu prowadzenia konta (czy aktywne czy pasywne),
 *     inaczej mówiąc - w jednym zespole kont mamy zarówno aktywne, jak i pasywne.
 *   - zarówno 200 jak i 210 mają w nazwie "rozrachunki" co nie daje podpowidzi w tym zakresie
 *
 * W amerykańskim planie kont jest (jak dla mnie) lepiej:
 *   - 106 Accounts Receivable
 *   - 201 Accounts Payable
 *
 *
 * 2. W polskim planie kont mogą być wymienione tak zwane "konta analityczne". Tylko, że
 *   - to, co polska rachunkowość nazywa "kontem analicznym" to w Anglo-Saxon raczej
 *     możnaby przetłumaczyć jako "Subsidiary Ledger Account" i jest to nadal financial accounting.
 *   - z kolei to co w Anglo-Saxon jest nazywane "Analytic Account" to jeszcze coś innego
 *     i to już jest całkiem management accounting
 *    W Anglo-Saxon "Subsidiary Ledger Account" się nie pokazuje w planie kont.
 *    Natomiast w obu systemach "Subsidiary Ledger Account" zachowuje się tak, że "mirroruje"
 *    wpisy z księgi głównej i nie musi się tak równoważyć debit-credit.
 * Możnaby podsumować, że w Anglo-Saxon jest tak:
 *   - General Ledger Account - rachunkowość finansowa do celów statutowych, sprawozdawczości ustawowej
 *   - Subsidiary Ledger Account - rachunkowość finansowa na potrzeby własne (jako pierwszy przykład podawane jest
 *                                 podzielenie należności per klient.
 *   - Analytic Account - rachunkowość zarządcza, więc potrzeby włąsne ale raczej w sensie statystyki, KPI'ów,
 *                        predykcji.
 *
 */
public enum GeneralLedgerAccount {

    _101_cash("101", CurrentAssets, BalanceAccount, debit),
    _106_accounts_receivable("106", CurrentAssets, BalanceAccount, debit),
    _128_office_equipment("128", CurrentAssets, BalanceAccount, debit);

    private final String accountNumber;

    private final AccountGroup accountGroup;

    /*
     * This is probably redundant to accountGroup information. But leaving it separate for verbosity
     * and handle if possibly there are any exceptions.
     *
     */
    private final BalanceOrResultAccount balanceOrResultAccount;

    private final NormalBalance normalBalance;

    GeneralLedgerAccount(String accountNumber, AccountGroup accountGroup, BalanceOrResultAccount balanceOrResultAccount,
                         NormalBalance normalBalance) {
        this.accountNumber = accountNumber;
        this.accountGroup = accountGroup;
        this.balanceOrResultAccount = balanceOrResultAccount;
        this.normalBalance = normalBalance;
    }

    public NormalBalance getNormalBalance() {
        return normalBalance;
    }
}

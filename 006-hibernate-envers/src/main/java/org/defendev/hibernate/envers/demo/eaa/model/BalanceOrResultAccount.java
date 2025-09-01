package org.defendev.hibernate.envers.demo.eaa.model;

/*

Both balance accounts and result accounts are general ledger (GL) accounts in the Chart of Accounts.

*/
public enum BalanceOrResultAccount {

    /*
      Balance Accounts
      ----------------
        Also called: Permanent accounts, Real accounts, or simply Balance sheet accounts.
        Represent the company's financial position at a point in time.
        They are carried forward from one accounting period to the next.
      Categories:
        Assets (e.g., Cash, Inventory, Accounts Receivable)
        Liabilities (e.g., Loans, Accounts Payable)
        Equity (e.g., Share Capital, Retained Earnings)
      Appear on: Balance Sheet.
    */
    BalanceAccount,

    /*
      Result Accounts
      ---------------
        Also called: Temporary accounts, Nominal accounts, or Income Statement accounts.
        Represent the company’s performance (results) during a period.
        They are closed at year-end to retained earnings (or profit/loss account).
      Categories:
        Revenues / Income (e.g., Sales Revenue, Interest Income)
        Expenses (e.g., Salaries, Rent, Depreciation)
        Appear on: Income Statement (Profit & Loss).
    */
    ResultAccount

}

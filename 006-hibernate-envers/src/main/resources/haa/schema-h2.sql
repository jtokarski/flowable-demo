

CREATE TABLE "FinancialTransaction" (
  "id" BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (NO CACHE),
  "recordedBy" VARCHAR(255) NOT NULL,
  "memo" VARCHAR(255),
  "transactionDateTimeZulu" TIMESTAMP WITHOUT TIME ZONE,
  "lifecycleStatus" VARCHAR(255),
  CONSTRAINT PK_FinancialTransaction PRIMARY KEY ("id")
);

CREATE TABLE "GeneralLedgerPosting" (
  "id" BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY (NO CACHE),
  "idOfFinancialTransaction" BIGINT NOT NULL,
  "generalLedgerAccount" VARCHAR(255) NOT NULL,
  "debitOrCredit" VARCHAR(1) NOT NULL,
  "amount" NUMERIC(16, 2) NOT NULL,
  CONSTRAINT PK_GeneralLedgerPosting PRIMARY KEY ("id"),
  CONSTRAINT FK_GeneralLedgerPosting_FinancialTransaction_1 FOREIGN KEY ("idOfFinancialTransaction")
    REFERENCES "FinancialTransaction"("id")
);




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
  CONSTRAINT PK_GeneralLedgerPosting PRIMARY KEY ("id")
);


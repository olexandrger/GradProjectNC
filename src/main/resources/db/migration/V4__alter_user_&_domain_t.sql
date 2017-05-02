ALTER TABLE "user" DROP COLUMN "account_id";

ALTER TABLE "user" ADD UNIQUE("email");

ALTER TABLE "domain" ADD UNIQUE("domain_name");
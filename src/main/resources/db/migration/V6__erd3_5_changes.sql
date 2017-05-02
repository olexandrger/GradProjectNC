ALTER TABLE "domain"
  DROP CONSTRAINT "Ref_domain_to_domain_type";
DROP TABLE "domain_type";
ALTER TABLE "domain"
  ADD CONSTRAINT "Ref_domain_to_domain_type" FOREIGN KEY ("domain_type_id")
REFERENCES "category" ("category_id")
MATCH SIMPLE
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
INSERT INTO "category_type" ("category_type_name") VALUES
  ('DOMAIN_TYPE');
INSERT INTO "category" ("category_name", "category_type_id") VALUES
  ('PRIVATE', 6),
  ('CORPORATIVE', 6);

ALTER TABLE "product"
  ADD COLUMN "is_active" BOOLEAN
ALTER TABLE "product_characteristic_value"
  DROP CONSTRAINT "product_characteristic_value_pkey";

ALTER TABLE "product_characteristic_value"
  ADD COLUMN "value_id" SERIAL NOT NULL PRIMARY KEY;
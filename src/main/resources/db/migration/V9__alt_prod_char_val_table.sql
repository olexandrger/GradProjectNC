ALTER TABLE "product_characteristic_value"
    ALTER COLUMN "date_value" TYPE timestamp(0) with time zone;

ALTER TABLE "product_characteristic_value"
  DROP CONSTRAINT "Ref_product_characteristic_value_to_product_characteristic";

ALTER TABLE "product_characteristic_value" ADD CONSTRAINT "Ref_product_characteristic_value_to_product_characteristic" FOREIGN KEY ("product_characteristic_id")
	REFERENCES "product_characteristic"("product_characteristic_id")
	MATCH SIMPLE
	ON DELETE CASCADE
	ON UPDATE NO ACTION
	NOT DEFERRABLE;
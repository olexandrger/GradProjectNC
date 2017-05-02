INSERT INTO "role" ("role_name") VALUES
    ('ADMIN'),
    ('CLIENT'),
    ('CSR'),
    ('PMG');

INSERT INTO "domain_type" ("domain_type_name") VALUES
    ('PRIVATE'),
    ('CORPORATIVE');

INSERT INTO "data_type" ("data_type") VALUES
    ('NUMBER'),
    ('DATE'),
    ('STRING');

INSERT INTO "category_type" ("category_type_name") VALUES
    ('PRODUCT_ORDER_STATUS'),
    ('COMPLAIN_STATUS'),
    ('PRODUCT_INSTANCE_STATUS'),
    ('PRODUCT_ORDER_AIM'),
    ('COMPLAIN_REASON');

INSERT INTO "category" ("category_name", "category_type_id") VALUES
    ('CREATED', 1),
    ('IN_PROGRESS', 1),
    ('CANCELLED', 1),
    ('COMPLETED', 1);

INSERT INTO "category" ("category_name", "category_type_id") VALUES
    ('CREATED', 2),
    ('UNDER_CONSIDERATION', 2),
    ('CONSIDERATION_COMPLETED', 2),
    ('REJECTED', 2);

INSERT INTO "category" ("category_name", "category_type_id") VALUES
    ('CREATED', 3),
    ('ACTIVATED', 3),
    ('SUSPENDED', 3),
    ('DEACTIVATED', 3);

INSERT INTO "category" ("category_name", "category_type_id") VALUES
    ('CREATE', 4),
    ('SUSPEND', 4),
    ('DEACTIVATE', 4),
    ('CANCEL', 4);

INSERT INTO "category" ("category_name", "category_type_id") VALUES
    ('SERVICE_PROBLEMS', 5),
    ('ORDER_PROBLEMS', 5),
    ('TECHNICAL_PROBLEMS', 5);
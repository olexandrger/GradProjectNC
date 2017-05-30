DELETE FROM report
WHERE report_name = 'Client orders report';

INSERT INTO report (report_name, report_description, report_script, parameters) VALUES
  ('Client orders report',

   'The report
should contain following information: total amount of orders and list of
orders with: order number; product ordered; price; date of order.',

   'WITH completed_user_orders AS (
    SELECT DISTINCT
      po.product_order_id                                 AS "order_number",
      aim.category_id                                     AS "order_aim_Id",
      aim.category_name                                   AS "order_aim",
      po.open_date                                        AS "open_date",
      po.close_date                                       AS "close_date",
      prod.product_name                                   AS "product",
      pi.instance_id                                      AS "instance_id",
      prp.price                                           AS "product_prise",
      COALESCE(MAX(dis.discount)
               OVER (
                 PARTITION BY (po.product_order_id) ), 0) AS "discount",
      rank()
      OVER (
        PARTITION BY (po.order_aim_id IN (13, 25)), po.product_instance_id, date_part(''month'', po.close_date),
          date_part(''year'', po.close_date)
        ORDER BY po.close_date )                          AS num
    FROM product_order po
      INNER JOIN "user" usr
        ON usr.user_id = po.user_id
      INNER JOIN CATEGORY aim
        ON aim.category_id = po.order_aim_id
      INNER JOIN CATEGORY status
        ON po.status_id = status.category_id
      INNER JOIN product_instance pi
        ON pi.instance_id = po.product_instance_id
      INNER JOIN DOMAIN D
        ON D.domain_id = pi.domain_id
      INNER JOIN product_region_price prp
        ON pi.price_id = prp.price_id
      INNER JOIN product prod
        ON prp.product_id = prod.product_id
      LEFT JOIN discount_price dp
        ON dp.price_id = prp.price_id
      LEFT JOIN discount dis
        ON dis.discount_id = dp.discount_id AND po.close_date >= dis.start_date AND po.close_date <= dis.end_date
    WHERE po.status_id = 4 AND usr.email = ? AND (po.open_date >= ? :: TIMESTAMP) AND (po.close_date <= ? :: TIMESTAMP)
)
SELECT
  cuo.order_number AS "Number",
  cuo.order_aim    AS "Aim",
  cuo.product      AS "Product",
  cuo.open_date    AS "Open",
  cuo.close_date   AS "Close",
  CASE
  WHEN (cuo."order_aim_Id" = 13 OR (cuo."order_aim_Id" = 25 AND cuo.num = 1))
    THEN cuo.product_prise
  ELSE 0
  END              AS "prise",
  CASE
  WHEN (cuo."order_aim_Id" = 13 OR (cuo."order_aim_Id" = 25 AND cuo.num = 1))

    THEN (round((cuo.product_prise - cuo.product_prise * cuo.discount / 100), 2))
  ELSE 0
  END              AS "Discounted prise",
  cuo.discount ||
  ''%''              AS "Discount"
FROM completed_user_orders cuo
ORDER BY cuo.instance_id, cuo.close_date NULLS FIRST ;',
   '[
     {
       "id": 0,
       "name": "User e-mail",
       "type": "STRING"
     },
     {
       "id": 1,
       "name": "Start date",
       "type": "DATETIME"
     },
     {
       "id": 2,
       "name": "End date",
       "type": "DATETIME"
     }
   ]' :: JSON);
create table order_items (
  id 				 integer identity,
  order_id              integer,
  item_code_l 		 varchar(200),
  item_code_m 		 varchar(200),
  item_code_s 		 varchar(200),
  item_description_s longvarchar,
  item_description_l longvarchar,
  quantity 			 integer
)
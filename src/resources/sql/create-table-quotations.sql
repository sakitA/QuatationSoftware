create table quotations (
  id 					integer identity,
  bathroom_id			integer,
  customer_name 		varchar(200),
  customer_address 		varchar(200),
  contact_number 		varchar(200),
  email_id 				varchar(200),
  enquiry_reference_no  varchar(200),
  enquiry_date 			date,
  reference_remark 		longvarchar,
  quote_format 			varchar(200),
  headings 				varchar(200),
  vat 					varchar(200),
  display 				varchar(200),
  quotation_date		date
)
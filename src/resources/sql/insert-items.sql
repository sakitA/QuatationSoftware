insert
  into items
  	   (quotation_id, bathroom_id, serial_no, item_code_l, item_code_m, item_code_s, item_description_s,
  	    item_description_l, quantity, mrp, nrp, discount, vat_percentage, nrp_without_vat,
  	    rd_without_vat, vat_amount, rd_including_vat, margin_amount, margin_percentage, image,
  	    bathroom_element, total_amount)
values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
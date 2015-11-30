select distinct bat.* 
  from bathrooms bat, 
       items itm
 where bat.id = itm.bathroom_id 
   and itm.quotation_id = ?

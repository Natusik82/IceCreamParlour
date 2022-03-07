# Ice Cream Parlour Application

##Domain objects:
  * I used enum for declaring a **flavour** to ensure only the values declared can enter the system thus excluding any unknown flavours. Same choice for **promo** options.
  * **PromoStrategy** describes the calculation strategy. In the real world application this can be populated from the database, text files, API or any external source. 
    * **quantityQualifiedForPromo** can be any number where the total number qualifies for a promo being applied.
    * **promoPay** can be any number between 0 and 1 where 0 means you pay nothing for one item within **quantityQualifiedForPromo**, 0.5 mean you pay half price for one item within **quantityQualifiedForPromo**
  * **IceCream** describes the flavour, price and whether the promo is applied for this flavour. In the real world appplication the price can come from the external source like the database or API call but for the sake of simplicity I set it directly on the data class.
  * **IceCreamOrderItem** is what enters the calculator or rather list of items if multiple flavours are in the order.
  * **IceCreamOrder** is what the calculator returns: the items with individual price and the totals.


##Implementation:
  * **IceCreamParlourApplication** is an entry point to the application for the purpose of demo. In this exercise the ice cream order items are coded but in the real world application they can come externally (for example via API call) or internally from another application.
  * **OrderCalculator** contains the main logic and implemented in a functional way. The strategy is passed when the class is initialized assuming the strategy does not change while the application is running.
  * **calculateTotalPriceForOrder** function is the only public function which is exposed outside the **OrderCalculator** class. It accepts ice cream order items which require pricing and returns these items priced individually as well as totals. The calculation is single threaded in this exercise but it is possible to parallelize the calculation if we need to calculate a large number of items and the performance is critical.  
  * **calculatePrice** function is a private function which calculates the price of each ice cream order item. This function contains the main logic which takes promo into account. The logic is generic and is based on the promo presence. If promo is set it uses it in the calculation otherwise not. This function has a side effect in a form of thrown exceptions if the input (quantity or price) is invalid. The requirements did not specify the behaviour for the invalid data. So I just handled it in a very simple way by throwing an exception. In the real world the requirements to handle invalid data can include logging, return the error from the function (to be truly functional), filter the invalid items and price the valid one, etc depending on the requirements.


##Testing:
  * Since this application is very simple where the main logic sits in the **OrderCalculator** class the unit tests are sufficient in this exercise. In more complex system I would also add integration tests to test across multiple components of the system.
  * Unit test cases include various scenarios (cases with and without promo applied), multiple items with promo and edge cases such as zero or negative quantity or negative price.
  * **assertj** library is used for test assertions. I like this library because it has a very rich set of assertions which are very intuitive to use, can be chained like in the plain english sentence. 


##Improvements:
  * Add logging to improve supportability. Due to the time constrain I did not add logging but it would be the first thing to do next.
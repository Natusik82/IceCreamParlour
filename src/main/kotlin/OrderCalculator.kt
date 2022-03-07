package main.kotlin

import java.math.BigDecimal

class OrderCalculator(private val strategyByPromo: Map<Promo, PromoStrategy>) {

    fun calculateTotalPriceForOrder(iceCreamOrderItems: List<IceCreamOrderItem>): IceCreamOrder {
        val iceCreamOrderItemsWithPrice = iceCreamOrderItems.map { calculatePrice(it) }

        return IceCreamOrder(
            iceCreamItems = iceCreamOrderItemsWithPrice,
            totalOrderPrice = iceCreamOrderItemsWithPrice.sumByBigDecimal { it.totalPriceWithoutDiscount }.setScale(2),
            totalPromoDiscount = iceCreamOrderItemsWithPrice.sumByBigDecimal { it.discount }.setScale(2)
        )
    }

    private fun calculatePrice(iceCreamOrderItem: IceCreamOrderItem): IceCreamOrderItemWithPrice {
        if (iceCreamOrderItem.quantity < 1) {
            val errorMessage = "Ice cream order item quantity is ${iceCreamOrderItem.quantity}"
            throw Exception(errorMessage)
        }

        val itemPrice = iceCreamOrderItem.iceCream.unitPrice

        if (itemPrice < BigDecimal.ZERO) {
            val errorMessage = "Ice cream price = $itemPrice cannot be negative"
            throw Exception(errorMessage)
        }

        val totalPrice = itemPrice * BigDecimal(iceCreamOrderItem.quantity)

        return when (val strategy = strategyByPromo[iceCreamOrderItem.iceCream.promo]) {
             null -> IceCreamOrderItemWithPrice(iceCreamOrderItem, totalPrice, totalPrice)
             else -> {
                 val promoQuantity = iceCreamOrderItem.quantity / strategy.quantityQualifiedForPromo
                 val nonPromoQuantity = iceCreamOrderItem.quantity - promoQuantity

                 val portionPriceWithDiscount = itemPrice * BigDecimal(promoQuantity) * strategy.promoPay
                 val portionPriceWithoutDiscount = itemPrice * BigDecimal(nonPromoQuantity)

                 val totalPriceWithDiscount = (portionPriceWithDiscount + portionPriceWithoutDiscount)

                 IceCreamOrderItemWithPrice(iceCreamOrderItem, totalPrice, totalPriceWithDiscount)
             }
         }
    }

    private fun <T> Iterable<T>.sumByBigDecimal(transform: (T) -> BigDecimal): BigDecimal {
        return this.fold(BigDecimal.ZERO) { acc, e -> acc + transform.invoke(e) }
    }
}
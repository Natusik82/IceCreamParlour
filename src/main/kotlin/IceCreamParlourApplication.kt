package main.kotlin

import java.math.BigDecimal

object IceCreamParlourApplication {

    @JvmStatic
    fun main(args: Array<String>) {

        val strategyByPromo = listOf(
            PromoStrategy(
                promo = Promo.Buy2Get1Free,
                quantityQualifiedForPromo = 3,
                promoPay = BigDecimal("0.0")
            ),
            PromoStrategy(
                promo = Promo.Buy2Get1HalfPrice,
                quantityQualifiedForPromo = 3,
                promoPay = BigDecimal("0.5")
            )
        ).associateBy { it.promo }

        val orderCalculator = OrderCalculator(strategyByPromo)

        val iceCreamOrderItems = listOf(
            IceCreamOrderItem(
                quantity = 1,
                iceCream = IceCream(
                    flavour = Flavour.RockyRoad,
                    unitPrice = BigDecimal("8.0").setScale(2),
                    promo = Promo.Buy2Get1Free
                )
            ),
            IceCreamOrderItem(
                quantity = 3,
                iceCream = IceCream(
                    flavour = Flavour.CockiesAndCream,
                    unitPrice = BigDecimal("10.0").setScale(2),
                    promo = Promo.Buy2Get1HalfPrice
                )
            ),
            IceCreamOrderItem(
                quantity = 2,
                iceCream = IceCream(
                    flavour = Flavour.NetflixAndChill,
                    unitPrice = BigDecimal("12.0").setScale(2)
                )
            )
        )

        println("Start pricing ${iceCreamOrderItems.size} ice cream order item(s)")

        val iceCreamOrder = orderCalculator.calculateTotalPriceForOrder(iceCreamOrderItems)

        println("Total Order: ${iceCreamOrder.totalOrderPrice} ${iceCreamOrder.currency}")
        println("Total Promos: -${iceCreamOrder.totalPromoDiscount} ${iceCreamOrder.currency}")
        println("Total To Pay: ${iceCreamOrder.totalToPay} ${iceCreamOrder.currency}")
    }
}
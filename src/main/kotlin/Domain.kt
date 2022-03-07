package main.kotlin

import java.math.BigDecimal

enum class Flavour(val value: String) {
    RockyRoad ("Rocky Road"),
    CockiesAndCream ("Cockies & Cream"),
    NetflixAndChill ("Netflix & Chill")
}

enum class Promo {
    Buy2Get1Free,
    Buy2Get1HalfPrice
}

data class PromoStrategy(
    val promo: Promo,
    val quantityQualifiedForPromo: Int,
    val promoPay: BigDecimal
)

data class IceCream(
    val flavour: Flavour,
    val unitPrice: BigDecimal,
    val promo: Promo? = null
)

data class IceCreamOrderItem(
    val iceCream: IceCream,
    val quantity: Int
)

data class IceCreamOrderItemWithPrice(
    val iceCreamOrderItem: IceCreamOrderItem,
    val totalPriceWithoutDiscount: BigDecimal,
    val totalPriceWithDiscount: BigDecimal,
    val discount: BigDecimal = totalPriceWithoutDiscount - totalPriceWithDiscount
)

data class IceCreamOrder(
    val iceCreamItems: List<IceCreamOrderItemWithPrice>,
    val totalOrderPrice: BigDecimal,
    val totalPromoDiscount: BigDecimal,
    val totalToPay: BigDecimal = totalOrderPrice - totalPromoDiscount,
    val currency: String = "ZDR"
)
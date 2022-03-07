package unit

import main.kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class DomainTest {

    @Test
    fun `calculates a discount in IceCreamOrderItemWithPrice`() {
        val iceCreamOrderItemWithPrice = IceCreamOrderItemWithPrice(
            iceCreamOrderItem = IceCreamOrderItem(
                quantity = 3,
                iceCream = IceCream(
                    flavour = Flavour.RockyRoad,
                    unitPrice = BigDecimal("8.0"),
                    promo = Promo.Buy2Get1Free
                )
            ),
            totalPriceWithoutDiscount = BigDecimal("30.00"),
            totalPriceWithDiscount = BigDecimal("25.00")
        )

        assertThat(iceCreamOrderItemWithPrice.discount).isEqualTo(BigDecimal("5.00"))
    }

    @Test
    fun `calculates total to pay in IceCreamOrder`() {
        val iceCreamOrder = IceCreamOrder(
            iceCreamItems = listOf(),
            totalOrderPrice = BigDecimal("50.00"),
            totalPromoDiscount = BigDecimal("15.00")
        )

        assertThat(iceCreamOrder.totalToPay).isEqualTo(BigDecimal("35.00"))
    }
}
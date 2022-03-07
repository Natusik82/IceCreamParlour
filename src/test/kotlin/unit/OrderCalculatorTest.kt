package test.unit

import main.kotlin.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal


class OrderCalculatorTest {

    private val strategyByPromo = listOf(
        PromoStrategy(Promo.Buy2Get1Free, 3, BigDecimal("0.0")),
        PromoStrategy(Promo.Buy2Get1HalfPrice, 3, BigDecimal("0.5"))
    ).associateBy { it.promo }

    @Test
    fun `calculate total order price with promo applied`() {

        val iceCreamOrderItems = listOf(
            IceCreamOrderItem(
                quantity = 3,
                iceCream = IceCream(
                    flavour = Flavour.RockyRoad,
                    unitPrice = BigDecimal("8.0"),
                    promo = Promo.Buy2Get1Free
                )
            ),
            IceCreamOrderItem(
                quantity = 3,
                iceCream = IceCream(
                    flavour = Flavour.CockiesAndCream,
                    unitPrice = BigDecimal("10.0"),
                    promo = Promo.Buy2Get1HalfPrice
                )
            )
        )

        val calculator = OrderCalculator(strategyByPromo)

        val iceCreamOrderResult = calculator.calculateTotalPriceForOrder(iceCreamOrderItems)

         assertThat(iceCreamOrderResult.totalOrderPrice).isEqualTo(BigDecimal("54.00"))
         assertThat(iceCreamOrderResult.totalPromoDiscount).isEqualTo(BigDecimal("13.00"))
         assertThat(iceCreamOrderResult.totalToPay).isEqualTo(BigDecimal("41.00"))
    }

    @Test
    fun `calculate total order price without promo applied`() {

        val iceCreamOrderItems = listOf(
            IceCreamOrderItem(
                quantity = 2,
                iceCream = IceCream(
                    flavour = Flavour.RockyRoad,
                    unitPrice = BigDecimal("8.00"),
                    promo = Promo.Buy2Get1Free
                )
            ),
            IceCreamOrderItem(
                quantity = 2,
                iceCream = IceCream(
                    flavour = Flavour.CockiesAndCream,
                    unitPrice = BigDecimal("10.00"),
                    promo = Promo.Buy2Get1HalfPrice
                )
            ),
            IceCreamOrderItem(
                quantity = 3,
                iceCream = IceCream(
                    flavour = Flavour.NetflixAndChill,
                    unitPrice = BigDecimal("12.00")
                )
            )
        )

        val calculator = OrderCalculator(strategyByPromo)

        val iceCreamOrderResult = calculator.calculateTotalPriceForOrder(iceCreamOrderItems)

        assertThat(iceCreamOrderResult.totalOrderPrice).isEqualTo(BigDecimal("72.00"))
        assertThat(iceCreamOrderResult.totalPromoDiscount).isEqualTo(BigDecimal("0.00"))
        assertThat(iceCreamOrderResult.totalToPay).isEqualTo(BigDecimal("72.00"))
    }

    @Test
    fun `calculate total order price with multiple promo applied`() {

        val iceCreamOrderItems = listOf(
            IceCreamOrderItem(
                quantity = 5,
                iceCream = IceCream(
                    flavour = Flavour.RockyRoad,
                    unitPrice = BigDecimal("8.00"),
                    promo = Promo.Buy2Get1Free
                )
            ),
            IceCreamOrderItem(
                quantity = 6,
                iceCream = IceCream(
                    flavour = Flavour.CockiesAndCream,
                    unitPrice = BigDecimal("10.00"),
                    promo = Promo.Buy2Get1HalfPrice
                )
            ),
            IceCreamOrderItem(
                quantity = 3,
                iceCream = IceCream(
                    flavour = Flavour.NetflixAndChill,
                    unitPrice = BigDecimal("12.00")
                )
            )
        )

        val calculator = OrderCalculator(strategyByPromo)

        val iceCreamOrderResult = calculator.calculateTotalPriceForOrder(iceCreamOrderItems)

        assertThat(iceCreamOrderResult.totalOrderPrice).isEqualTo(BigDecimal("136.00"))
        assertThat(iceCreamOrderResult.totalPromoDiscount).isEqualTo(BigDecimal("18.00"))
        assertThat(iceCreamOrderResult.totalToPay).isEqualTo(BigDecimal("118.00"))
    }

    @Test
    fun `throws an exception when order contains zero quantity`() {
        val iceCreamOrderItems = listOf(
            IceCreamOrderItem(
                quantity = 0,
                iceCream = IceCream(
                    flavour = Flavour.RockyRoad,
                    unitPrice = BigDecimal("8.0"),
                    promo = Promo.Buy2Get1Free
                )
            )
        )

        val calculator = OrderCalculator(strategyByPromo)

        assertThatThrownBy {
            calculator.calculateTotalPriceForOrder(iceCreamOrderItems)
        }.isInstanceOf(Exception::class.java)
            .hasMessageContaining("Ice cream order item quantity is 0")
    }

    @Test
    fun `throws an exception when order contains negative quantity`() {
        val iceCreamOrderItems = listOf(
            IceCreamOrderItem(
                quantity = -1,
                iceCream = IceCream(
                    flavour = Flavour.RockyRoad,
                    unitPrice = BigDecimal("8.0"),
                    promo = Promo.Buy2Get1Free
                )
            )
        )

        val calculator = OrderCalculator(strategyByPromo)

        assertThatThrownBy {
            calculator.calculateTotalPriceForOrder(iceCreamOrderItems)
        }.isInstanceOf(Exception::class.java)
            .hasMessageContaining("Ice cream order item quantity is -1")
    }

    @Test
    fun `throws an exception when order contains negative price`() {
        val iceCreamOrderItems = listOf(
            IceCreamOrderItem(
                quantity = 3,
                iceCream = IceCream(
                    flavour = Flavour.RockyRoad,
                    unitPrice = BigDecimal("-1.0"),
                    promo = Promo.Buy2Get1Free
                )
            )
        )

        val calculator = OrderCalculator(strategyByPromo)

        assertThatThrownBy {
            calculator.calculateTotalPriceForOrder(iceCreamOrderItems)
        }.isInstanceOf(Exception::class.java)
            .hasMessageContaining("Ice cream price = -1.0 cannot be negative")
    }
}
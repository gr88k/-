package ru.tbank.education.school.lesson7.service

import org.springframework.stereotype.Service
import ru.tbank.education.school.lesson7.dto.CreateOrderRequest
import ru.tbank.education.school.lesson7.dto.Order
import ru.tbank.education.school.lesson7.dto.OrderItem
import ru.tbank.education.school.lesson7.dto.OrderStatus
import java.math.BigDecimal
import java.util.*

@Service
class OrderService {

    private val orders = mutableMapOf<Long, Order>()
    private var idCounter = 1L

    fun create(request: CreateOrderRequest): Order {
        val order = toOrder(idCounter++, request, OrderStatus.NEW)
        orders[order.id] = order
        return order
    }

    fun getById(id: Long): Order {
        return orders[id] ?: throw NoSuchElementException("Order with id $id not found")
    }

    fun getAll(status: OrderStatus?): List<Order> {
        return if (status == null) {
            orders.values.toList()
        } else {
            orders.values.filter { it.status == status }
        }
    }

    fun update(id: Long, request: CreateOrderRequest): Order {
        if (!orders.containsKey(id)) {
            throw NoSuchElementException("Order with id $id not found")
        }

        val updatedOrder = toOrder(id, request, orders[id]!!.status)
        orders[id] = updatedOrder
        return updatedOrder
    }

    fun delete(id: Long) {
        if (!orders.containsKey(id)) {
            throw NoSuchElementException("Order with id $id not found")
        }
        orders.remove(id)
    }

    fun updateStatus(id: Long, status: OrderStatus): Order {
        val existingOrder = getById(id)
        val updatedOrder = existingOrder.copy(status = status)
        orders[id] = updatedOrder
        return updatedOrder
    }

    private fun toOrder(id: Long, request: CreateOrderRequest, status: OrderStatus): Order {
        val items = request.items.map { item ->
            val lineTotal = item.price.multiply(BigDecimal.valueOf(item.quantity.toLong()))
            OrderItem(
                sku = item.sku,
                quantity = item.quantity,
                price = item.price,
                lineTotal = lineTotal
            )
        }
        val total = items.fold(BigDecimal.ZERO) { acc, item -> acc.add(item.lineTotal) }
        return Order(
            id = id,
            customerEmail = request.customerEmail,
            deliveryAddress = request.deliveryAddress,
            items = items,
            totalAmount = total,
            status = status
        )
    }
}
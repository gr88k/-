package com.example.demo

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

data class Item(
    val id: Long,
    val name: String,
    val category: String,
    val price: Double
)

@Service
class ItemService {
    private val items = ConcurrentHashMap<Long, Item>()
    private val idGenerator = AtomicLong(0)

    fun createItem(name: String, category: String, price: Double): Item {
        val id = idGenerator.incrementAndGet()
        val item = Item(id, name, category, price)
        items[id] = item
        println("Создан: $item")
        return item
    }

    fun getAllItems(): List<Item> {
        println("\nВсе товары (${items.size} шт.):")
        items.values.forEach { println("   $it") }
        return items.values.toList()
    }

    fun deleteAllItems() {
        items.clear()
        idGenerator.set(0)
        println("Все товары удалены.")
    }
}

@Component
class Starter(
    private val itemService: ItemService
) : CommandLineRunner {
    override fun run(vararg args: String) {
        println("Создание товаров")
        itemService.createItem("Котики и Ява", "книги", 500.0)
        itemService.createItem("Как стать милионером", "книги", 999.0)
        itemService.createItem("Кофеварка", "техника", 15000.0)

        println("\nПросмотр всех товаров")
        itemService.getAllItems()

        println("\nОчистка")
        itemService.deleteAllItems()

        println("\nПроверка после очистки")
        itemService.getAllItems()
    }
}

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
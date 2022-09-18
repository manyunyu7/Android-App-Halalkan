package com.feylabs.halalkan.utils.resto

import android.content.Context
import com.feylabs.halalkan.data.local.MyPreference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class OrderUtility(private val mContext: Context) {


    companion object {
        const val PREF_ORDER_KEY = "YzzzzUHUU"
    }

    fun clearOrder(){
        MyPreference(mContext).removeKey(PREF_ORDER_KEY)
    }

    fun checkResto(restoId: Int): Boolean {
        var isError=false
        getListOrder().forEachIndexed { index, orderLocalModel ->
            if (restoId!=orderLocalModel.restoId){
                isError=true
            }
        }
        return isError
    }

    fun addItem(order: OrderLocalModel) {
        if (isOrderExist().not()) {
            saveNewOrder(listOf(order))
        } else {
            if (isItemAlreadyInserted(order.menuId)) {
                changeItem(order)
            } else {
                val currentOrder = getListOrder()
                currentOrder.add(order)
                saveNewOrder(currentOrder.toList())
            }

        }
    }

    fun isItemAlreadyInserted(menuId: String): Boolean {
        val item = getItem(menuId = menuId)
        item?.let {
            return true
        } ?: kotlin.run {
            return false
        }
    }

    fun changeItem(newOrder: OrderLocalModel) {
        if (isOrderExist()) {
            val currentOrder = getListOrder()

            currentOrder.forEachIndexed { index, orderLocalModel ->
                if (orderLocalModel.menuId == newOrder.menuId) {
                    currentOrder[index] = newOrder
                }
            }
            saveNewOrder(currentOrder.toList())
        }
    }

    fun changeNotes(menuId: String, notes: String) {
        val item = getItem(menuId)
        removeItem(menuId)
        item?.let {
            it.notes = notes
            addItem(item)
        }
    }

    fun getItem(menuId: String): OrderLocalModel? {
        var searchedItem: OrderLocalModel? = null

        if (isOrderExist()) {
            val currentOrders = getListOrder()

            currentOrders.forEachIndexed { index, orderLocalModel ->
                if (menuId == orderLocalModel.menuId) {
                    searchedItem = orderLocalModel
                }
            }
        }

        return searchedItem
    }

    fun removeItem(menuId: String) {
        if (isOrderExist()) {
            val currentOrder = getListOrder()
            with(currentOrder.iterator()) {
                forEach {
                    if (menuId == it.menuId) {
                        remove()
                    }
                }
            }

            saveNewOrder(currentOrder.toList())
        }
    }

    fun getSummary(): OrderSummaryModel? {
        if (isOrderExist()) {
            var totalInWon = 0.0
            val orders = getListOrder()

            var rawQuantity = 0

            orders.forEachIndexed { index, orderLocalModel ->
                var quantity = orderLocalModel.quantity
                var price = orderLocalModel.price
                var total = price * quantity

                rawQuantity += quantity

                totalInWon += total
            }

            return OrderSummaryModel(
                quantity = rawQuantity,
                totalMenu = orders.size,
                totalInWon = totalInWon
            )
        }

        return null
    }


    fun saveNewOrder(list: List<OrderLocalModel>?) {
        val gson = Gson()
        val json: String = gson.toJson(list)
        MyPreference(mContext).save(PREF_ORDER_KEY, json)
    }

    fun isOrderExist(): Boolean {
        var orders = getListOrder()
        return orders.size != 0
    }

    fun getListOrder(): MutableList<OrderLocalModel> {
        val arrayItems: List<OrderLocalModel>
        val serializedObject: String =
            MyPreference(mContext).getPrefString(PREF_ORDER_KEY).orEmpty()
        if (serializedObject.isNotEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<List<OrderLocalModel>>() {}.type
            arrayItems = gson.fromJson<List<OrderLocalModel>>(serializedObject, type)
            return arrayItems.toMutableList()
        } else
            return emptyList<OrderLocalModel>().toMutableList()
    }


}
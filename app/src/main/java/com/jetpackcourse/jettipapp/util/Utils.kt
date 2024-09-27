package com.jetpackcourse.jettipapp.util

fun calculateTotalTipAmount(moneyBillState: String, tipPercentage: Int): Double {
    return if (moneyBillState.isNotBlank()){
        tipPercentage * moneyBillState.toDouble() / 100
    }else{
        0.0
    }
}
fun calculateTotalPerPerson(totalBill: String, tipPercentage: Int, splitNumber: Int) : Double{
    return if (totalBill.isNotBlank()) {
        val tipValue = calculateTotalTipAmount(totalBill, tipPercentage)
        (totalBill.toDouble() + tipValue) / splitNumber
    }else{
        0.0
    }

}
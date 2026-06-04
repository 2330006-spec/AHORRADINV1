package com.example.ahorradinv1

class StorageService {

    private var saldo = 3250.0
    private var ingresos = 5000.0
    private var gastos = 1750.0

    fun obtenerSaldo(): Double {
        return saldo
    }

    fun obtenerIngresos(): Double {
        return ingresos
    }

    fun obtenerGastos(): Double {
        return gastos
    }

    fun agregarIngreso(cantidad: Double) {
        ingresos += cantidad
        saldo += cantidad
    }

    fun agregarGasto(cantidad: Double) {
        gastos += cantidad
        saldo -= cantidad
    }
}
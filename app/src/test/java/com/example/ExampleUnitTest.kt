package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    private fun calculateFee(amount: Int): Int {
        if (amount <= 0) return 0
        return if (amount < 1000) 15 else 20
    }

    private fun calculateUnits(amount: Int): Double {
        if (amount <= 0) return 0.0
        val tariffPerKwh = 10.14
        val units = amount / tariffPerKwh
        return Math.round(units * 10.0) / 10.0
    }

    private fun isNawecMeterValid(input: String): Boolean {
        val clean = input.filter { it.isDigit() }
        return clean.length == 11
    }

    @Test
    fun testFeeTierUnder1000GMD() {
        assertEquals(15, calculateFee(500))
        assertEquals(15, calculateFee(999))
    }

    @Test
    fun testFeeTierAtOrAbove1000GMD() {
        assertEquals(20, calculateFee(1000))
        assertEquals(20, calculateFee(2500))
        assertEquals(20, calculateFee(5000))
    }

    @Test
    fun testUnitsCalculation() {
        val units500 = calculateUnits(500)
        assertTrue(units500 in 49.0..49.5)

        val units1000 = calculateUnits(1000)
        assertTrue(units1000 in 98.4..98.8)
    }

    @Test
    fun testTokenFormatting() {
        val sampleToken = "45829102381984021194"
        assertEquals(20, sampleToken.length)
        val formatted = sampleToken.chunked(4).joinToString(" - ")
        assertEquals("4582 - 9102 - 3819 - 8402 - 1194", formatted)
    }

    @Test
    fun testNawecMeterFormatValidation() {
        // Standard 11-digit NAWEC meters
        assertTrue(isNawecMeterValid("04219985523"))
        assertTrue(isNawecMeterValid("07114429018"))
        assertTrue(isNawecMeterValid("02883491204"))

        // Partial / incomplete meters should be invalid (verify button disabled)
        assertFalse(isNawecMeterValid(""))
        assertFalse(isNawecMeterValid("042199"))
        assertFalse(isNawecMeterValid("0421998552")) // 10 digits

        // Too long
        assertFalse(isNawecMeterValid("042199855231")) // 12 digits
    }
}

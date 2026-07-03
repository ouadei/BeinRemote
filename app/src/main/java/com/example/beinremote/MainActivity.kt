package com.example.beinremote

import android.hardware.ConsumerIrManager
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

object IrCodes {
    const val FREQUENCY = 38000

    val POWER = intArrayOf(9000, 4500, 560, 560, 560, 1690, 560, 560, 560, 1690, 560, 39000)
    val CHANNEL_UP = intArrayOf(9000, 4500, 560, 1690, 560, 560, 560, 1690, 560, 560, 560, 39000)
    val CHANNEL_DOWN = intArrayOf(9000, 4500, 560, 560, 560, 1690, 560, 1690, 560, 560, 560, 39000)
    val VOLUME_UP = intArrayOf(9000, 4500, 560, 1690, 560, 1690, 560, 560, 560, 560, 560, 39000)
    val VOLUME_DOWN = intArrayOf(9000, 4500, 560, 560, 560, 560, 560, 1690, 560, 1690, 560, 39000)

    val DIGITS = arrayOf(
        intArrayOf(9000, 4500, 560, 560, 560, 560, 560, 560, 560, 560, 560, 39000),
        intArrayOf(9000, 4500, 560, 560, 560, 560, 560, 560, 560, 1690, 560, 39000),
        intArrayOf(9000, 4500, 560, 560, 560, 560, 560, 1690, 560, 560, 560, 39000),
        intArrayOf(9000, 4500, 560, 560, 560, 560, 560, 1690, 560, 1690, 560, 39000),
        intArrayOf(9000, 4500, 560, 560, 560, 1690, 560, 560, 560, 560, 560, 39000),
        intArrayOf(9000, 4500, 560, 560, 560, 1690, 560, 560, 560, 1690, 560, 39000),
        intArrayOf(9000, 4500, 560, 560, 560, 1690, 560, 1690, 560, 560, 560, 39000),
        intArrayOf(9000, 4500, 560, 560, 560, 1690, 560, 1690, 560, 1690, 560, 39000),
        intArrayOf(9000, 4500, 560, 1690, 560, 560, 560, 560, 560, 560, 560, 39000),
        intArrayOf(9000, 4500, 560, 1690, 560, 560, 560, 560, 560, 1690, 560, 39000)
    )
}

class MainActivity : AppCompatActivity() {

    private var irManager: ConsumerIrManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        irManager = getSystemService(CONSUMER_IR_SERVICE) as? ConsumerIrManager
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        val hasIr = irManager?.hasIrEmitter() == true
        tvStatus.text = if (hasIr) {
            "✅ الهاتف يدعم الأشعة تحت الحمراء"
        } else {
            "❌ هذا الهاتف لا يحتوي على IR blaster"
        }

        findViewById<Button>(R.id.btnPower).setOnClickListener { send(IrCodes.POWER, "تشغيل/إيقاف") }
        findViewById<Button>(R.id.btnChannelUp).setOnClickListener { send(IrCodes.CHANNEL_UP, "القناة +") }
        findViewById<Button>(R.id.btnChannelDown).setOnClickListener { send(IrCodes.CHANNEL_DOWN, "القناة -") }
        findViewById<Button>(R.id.btnVolUp).setOnClickListener { send(IrCodes.VOLUME_UP, "الصوت +") }
        findViewById<Button>(R.id.btnVolDown).setOnClickListener { send(IrCodes.VOLUME_DOWN, "الصوت -") }

        buildNumPad()
    }

    private fun send(pattern: IntArray, label: String) {
        val manager = irManager
        if (manager == null || !manager.hasIrEmitter()) {
            Toast.makeText(this, "لا يوجد IR blaster في هذا الهاتف", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            manager.transmit(IrCodes.FREQUENCY, pattern)
            Toast.makeText(this, "تم إرسال: $label", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "فشل الإرسال: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buildNumPad() {
        val grid = findViewById<GridLayout>(R.id.numPad)
        for (digit in 0..9) {
            val btn = Button(this)
            btn.text = digit.toString()
            val params = GridLayout.LayoutParams()
            params.width = 100
            params.height = 100
            params.setMargins(8, 8, 8, 8)
            btn.layoutParams = params
            btn.setOnClickListener { send(IrCodes.DIGITS[digit], "رقم $digit") }
            grid.addView(btn)
        }
    }
}

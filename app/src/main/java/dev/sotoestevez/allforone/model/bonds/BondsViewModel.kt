package dev.sotoestevez.allforone.model.bonds

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider

/** ViewModel of the Bonds Activity */
class BondsViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider
) : PrivateViewModel(savedStateHandle, dispatchers) {

	fun getQrCodeBitmap(code: String, ): Bitmap {
		val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 }
		val size = 512 //pixels
		val bits = QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, size, size, hints)
		return Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
			for (x in 0 until size) {
				for (y in 0 until size) {
					it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
				}
			}
		}
	}

}
package dev.sotoestevez.allforone.util.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import dev.sotoestevez.allforone.util.extensions.logError

object BitmapGenerator {

	/**
	 * Generates a bitmap with the specified drawable and color
	 * Based on from ApiDemos on GitHub:
	 * https://github.com/googlemaps/android-samples/blob/main/ApiDemos/kotlin/app/src/main/java/com/example/kotlindemos/MarkerDemoActivity.kt
	 */
	fun fromDrawable(context: Context, @DrawableRes id: Int, @ColorInt color: Int): BitmapDescriptor {
		val drawable = ResourcesCompat.getDrawable(context.resources, id, null)
		if (drawable == null) {
			logError("Error loading resource with id[${id}]")
			return BitmapDescriptorFactory.defaultMarker()
		}
		val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
		val canvas = Canvas(bitmap)
		DrawableCompat.setTint(drawable, color)
		drawable.apply {
			setBounds(0, 0, canvas.width, canvas.height)
			draw(canvas)
		}
		return BitmapDescriptorFactory.fromBitmap(bitmap)
	}


	/**
	 * Generates the QR bitmap of the given code
	 *
	 * @param code  Code to represent as QR
	 * @return      Bitmap to display the QR Code
	 */
	fun getQrCode(code: String, size: Int): Bitmap {
		val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 }
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
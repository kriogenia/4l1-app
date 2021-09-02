package dev.sotoestevez.allforone.model.bonds

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import dev.sotoestevez.allforone.data.User
import dev.sotoestevez.allforone.model.PrivateViewModel
import dev.sotoestevez.allforone.repositories.SessionRepository
import dev.sotoestevez.allforone.repositories.UserRepository
import dev.sotoestevez.allforone.util.dispatcher.DefaultDispatcherProvider
import dev.sotoestevez.allforone.util.dispatcher.DispatcherProvider
import dev.sotoestevez.allforone.util.extensions.logDebug
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.ArrayList

/** ViewModel of the Bonds Activity */
class BondsViewModel(
	savedStateHandle: SavedStateHandle,
	dispatchers: DispatcherProvider = DefaultDispatcherProvider,
	sessionRepository: SessionRepository = SessionRepository(),
	private val userRepository: UserRepository = UserRepository()
) : PrivateViewModel(savedStateHandle, dispatchers, sessionRepository) {

	/** Current QR Code to create a bond */
	val qrCode: LiveData<String>
		get() = mQrCode
	private val mQrCode: MutableLiveData<String> = MutableLiveData("")

	/** List of bonds of the user */
	val bonds: LiveData<List<User>>
		get() = mBonds
	private val mBonds: MutableLiveData<List<User>> = MutableLiveData(ArrayList())

	/** Mutable live data to manage the QR section loading state */
	val loadingQr: MutableLiveData<Boolean> = MutableLiveData(false)

	/** Timestamp in seconds of the QR request*/
	private var lastQRRequest: Long = 0

	init {
		loading.value = true
		// Load bonds
		viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
			val response = userRepository.getBonds(authHeader())
			withContext(dispatchers.main()) {
				loading.value = false
				mBonds.value = response     // updates retrieved bonds
			}
		}
	}

	/** Requests a new QR code if the current one is not valid */
	fun generateNewQRCode() {
		if (lastQRRequest + 50 > Instant.now().epochSecond)
			return  // Don't request a new QR if the current one is still valid
		loadingQr.value = true
		lastQRRequest = Instant.now().epochSecond
		viewModelScope.launch(dispatchers.io() + coroutineExceptionHandler) {
			val code = userRepository.requestBondingCode(authHeader())
			logDebug("[${user.value?.id}] Generated new bonding token: ${code.substring(0, 6)}...")
			withContext(dispatchers.main()) {
				mQrCode.value = code
			}
		}
	}

	/**
	 * Generates the QR bitmap of the given code
	 *
	 * @param code  Code to represent as QR
	 * @return      Bitmap to display the QR Code
	 */
	fun getQrCodeBitmap(code: String): Bitmap {
		val hints = hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 }
		val size = 640 //pixels
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
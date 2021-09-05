package dev.sotoestevez.allforone.ui.keeper.qr

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import dev.sotoestevez.allforone.databinding.ActivityQrScannerBinding

/** Activity using the camera as a QR Scanner to forge the bond between Patient and Keeper  */
class QRScannerActivity : AppCompatActivity() {

	private lateinit var binding: ActivityQrScannerBinding

	private lateinit var codeScanner: CodeScanner

	companion object {
		private const val RC_PERMISSION = 10
	}
	private var mPermissionGranted = false

	@Suppress("KDocMissingDocumentation")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		bindLayout()
		initScanner()
		setCallbacks()
		requestPermission()
	}

	@Suppress("KDocMissingDocumentation")
	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<String?>,
		grantResults: IntArray
	) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults)
		if (requestCode == RC_PERMISSION) {
			if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				mPermissionGranted = true
				codeScanner.startPreview()
			} else {
				mPermissionGranted = false
			}
		}
	}

	@Suppress("KDocMissingDocumentation")
	override fun onResume() {
		super.onResume()
		if (mPermissionGranted) {
			codeScanner.startPreview()
		}
	}

	@Suppress("KDocMissingDocumentation")
	override fun onPause() {
		codeScanner.releaseResources()
		super.onPause()
	}

	private fun bindLayout() {
		binding = ActivityQrScannerBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	private fun initScanner() {
		codeScanner = CodeScanner(this, binding.scannerView)
		codeScanner.camera = CodeScanner.CAMERA_BACK
		codeScanner.formats = CodeScanner.TWO_DIMENSIONAL_FORMATS
		codeScanner.autoFocusMode = AutoFocusMode.SAFE
		codeScanner.scanMode = ScanMode.SINGLE
		codeScanner.isAutoFocusEnabled = true
		codeScanner.isFlashEnabled = false
	}

	private fun setCallbacks() {
		codeScanner.decodeCallback = DecodeCallback {
			val intent = Intent()
			intent.data = Uri.parse(it.text)
			setResult(Activity.RESULT_OK, intent)
			finish()
		}
		codeScanner.errorCallback = ErrorCallback {
			val intent = Intent()
			setResult(Activity.RESULT_CANCELED, intent)
			finish()
		}
	}

	private fun requestPermission() {
		if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			mPermissionGranted = false
			requestPermissions(arrayOf(Manifest.permission.CAMERA), RC_PERMISSION)
		} else {
			mPermissionGranted = true
		}
	}


}
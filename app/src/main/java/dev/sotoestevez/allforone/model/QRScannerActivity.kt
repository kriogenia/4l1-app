package dev.sotoestevez.allforone.model

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import dev.sotoestevez.allforone.R
import androidx.annotation.NonNull




class QRScannerActivity : AppCompatActivity() {

	private lateinit var codeScanner: CodeScanner

	private val RC_PERMISSION = 10
	private var mPermissionGranted = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_qr_scanner)
		val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

		codeScanner = CodeScanner(this, scannerView)

		// Parameters (default values)
		codeScanner.camera = CodeScanner.CAMERA_BACK
		codeScanner.formats = CodeScanner.TWO_DIMENSIONAL_FORMATS
		codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
		codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
		codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
		codeScanner.isFlashEnabled = false // Whether to enable flash or not

		// Callbacks
		codeScanner.decodeCallback = DecodeCallback {
			runOnUiThread {
				Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
			}
		}
		codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
			runOnUiThread {
				Toast.makeText(this, "Camera initialization error: ${it.message}",
					Toast.LENGTH_LONG).show()
			}
		}

		scannerView.setOnClickListener {
			codeScanner.startPreview()
		}

		if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			mPermissionGranted = false;
			requestPermissions(arrayOf(Manifest.permission.CAMERA), RC_PERMISSION);
		} else {
			mPermissionGranted = true;
		}
	}

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

	override fun onResume() {
		super.onResume()
		codeScanner.startPreview()
	}

	override fun onPause() {
		codeScanner.releaseResources()
		super.onPause()
	}


}
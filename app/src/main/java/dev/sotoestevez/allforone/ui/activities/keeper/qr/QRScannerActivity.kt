package dev.sotoestevez.allforone.ui.activities.keeper.qr

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
import dev.sotoestevez.allforone.util.extensions.logDebug

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
        logDebug("Launching QR activity")
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
                logDebug("Permission to camera granted")
            } else {
                mPermissionGranted = false
                logDebug("Permission to camera denied")
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
        logDebug("Initializing QR code scanner")
        codeScanner = CodeScanner(this, binding.scannerView).apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.TWO_DIMENSIONAL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
        }
    }

    private fun setCallbacks() {
        codeScanner.decodeCallback = DecodeCallback {
            logDebug("QR code decoded")
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
        logDebug("Checking camera permissions")
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            mPermissionGranted = false
            logDebug("Camera permission not granted. Requesting it.")
            requestPermissions(arrayOf(Manifest.permission.CAMERA), RC_PERMISSION)
        } else {
            mPermissionGranted = true
        }
    }

}
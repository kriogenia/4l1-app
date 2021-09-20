package dev.sotoestevez.allforone.vo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Collection of tokens and metadata to represent and use the authenticated session
 *
 * @property auth       Authentication token
 * @property refresh    Refresh token
 * @property expiration Expiration time of the authentication token
 */
@Parcelize
data class Session(
	val auth: String,
	val refresh: String,
	val expiration: Int
) : Parcelable

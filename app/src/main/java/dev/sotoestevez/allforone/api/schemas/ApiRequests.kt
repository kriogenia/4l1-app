package dev.sotoestevez.allforone.api.schemas

import dev.sotoestevez.allforone.vo.Address
import dev.sotoestevez.allforone.vo.User

// Schemas of the different body requests to communicate with the API

/**
 * Updatable fields of an user
 *
 * @property role
 * @property displayName
 * @property mainPhoneNumber
 * @property altPhoneNumber
 * @property address
 * @property email
 */
data class UserUpdateRequest(
	var role: User.Role? = null,
	var displayName: String? = null,
	var mainPhoneNumber: String? = null,
	var altPhoneNumber: String? = null,
	var address: Address? = null,
	var email: String? = null
)

/**
 * Properties of a request to create a new task
 *
 * @property title
 * @property description
 * @property submitter
 * @property done
 * @property timestamp
 */
data class TaskRequest(
	val title: String,
	val description: String?,
	val submitter: UserInfoMsg,
	val done: Boolean,
	val timestamp: Long
)

/**
 * Model of the request body for the user/bond/establish endpoint
 *
 * @property code   Bonding token to verify
 */
data class BondEstablishRequest(
	val code: String
)


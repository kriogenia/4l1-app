package dev.sotoestevez.allforone.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Properties of an user
 *
 * @property id unique identifier of the user
 * @property googleId unique Google identifier of the user
 * @property role type of user
 * @property displayName of the user
 */
@Parcelize
class User(
	@SerializedName("_id") val id: String,
	val googleId: String,
	var role: Role,
	var displayName: String?
): Parcelable {

	/**
	 * Different types of users of the application
	 */
	enum class Role {

		/**
		 * Users that didn't select a [Role] yet
		 */
		@SerializedName("blank") BLANK,

		/**
		 * Users helping a patient
		 */
		@SerializedName("keeper") KEEPER,

		/**
		 * Users suffering the disease
		 */
		@SerializedName("patient") PATIENT

	}

}

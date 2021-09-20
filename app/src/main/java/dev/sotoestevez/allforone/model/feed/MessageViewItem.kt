package dev.sotoestevez.allforone.model.feed

import androidx.annotation.LayoutRes

interface MessageViewItem {

	enum class ViewType {
		SENT
	}

	@get:LayoutRes
	val layoutId: Int

	val viewType: Int
		get() = 0

}
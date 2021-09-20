package dev.sotoestevez.allforone.ui.components.recyclerview.messages

import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView
import dev.sotoestevez.allforone.vo.Message

/** View of [Message] to display on BindableRecyclerView */
interface MessageView: BindedItemView {

    /** Types of views of feed messages*/
    enum class Type {
        /** Text message sent by the user */
        SENT,
        /** Text message received by the user */
        RECEIVED
    }

    /** Wrapped message */
    val data: Message

}
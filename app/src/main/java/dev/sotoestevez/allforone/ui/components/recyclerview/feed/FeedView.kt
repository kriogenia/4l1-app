package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.ui.components.recyclerview.BindedItemView

/** View of Feed components to display on BindableRecyclerView */
interface FeedView: BindedItemView {

    /** Types of views of feed messages*/
    enum class Type {
        /** Date header */
        HEADER,
        /** Task message sent by the user */
        TASK_SENT,
        /** Task message received by the user */
        TASK_RECEIVED,
        /** Text message sent by the user */
        TEXT_SENT,
        /** Text message received by the user */
        TEXT_RECEIVED
    }

}
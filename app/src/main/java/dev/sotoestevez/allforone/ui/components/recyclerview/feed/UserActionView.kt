package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.R

/**
 * View for user's action to notify in the feed
 *
 * @property template   text template
 * @property firstArg   text arg
 * @property secondArg  text arg
 */
class UserActionView(val template: Int, val firstArg: String, val secondArg: String = "") : FeedView {

    override val id: String = firstArg + secondArg + template

    override val layoutId: Int = R.layout.content_feed_user_action

    override val viewType: Int = FeedView.Type.ACTION.ordinal

}
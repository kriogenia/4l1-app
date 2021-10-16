package dev.sotoestevez.allforone.ui.components.recyclerview.feed

import dev.sotoestevez.allforone.R

/**
 * View for text headers on the Feed
 *
 * @property text   text to display
 */
class TextHeaderView(val text: String) : FeedView {

    override val id: String = text

    override val layoutId: Int = R.layout.content_feed_text_header

    override val viewType: Int = FeedView.Type.HEADER.ordinal

}
package com.r42914lg.major

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.element.ComposeScreen


class ListingScreen(semanticsProvider: SemanticsNodeInteractionsProvider) :
    ComposeScreen<ListingScreen>(
        semanticsProvider = semanticsProvider,
    ) {
    val root = onNode {
        hasTestTag("listing_root")
    }
    val copyToClipboardView = onNode {
        hasTestTag("copy_view")
    }
}
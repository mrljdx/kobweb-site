package com.varabyte.kobweb.site

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.varabyte.kobweb.browser.storage.createStorageKey
import com.varabyte.kobweb.browser.storage.getItem
import com.varabyte.kobweb.browser.storage.setItem
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.core.AppGlobals
import com.varabyte.kobweb.core.isExporting
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.layer
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.common.SmoothColorStyle
import com.varabyte.kobweb.silk.style.layer.SilkLayer
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.site.components.sections.NavHeaderHeight
import com.varabyte.kobweb.site.components.sections.listing.MobileNavHeight
import com.varabyte.kobweb.site.components.style.DividerColor
import kotlinx.browser.document
import kotlinx.browser.localStorage
import org.jetbrains.compose.web.css.*

private val COLOR_MODE_KEY = ColorMode.entries.createStorageKey("app:colorMode")

@InitSilk
fun initSilk(ctx: InitSilkContext) {
    ctx.config.apply {
        initialColorMode = localStorage.getItem(COLOR_MODE_KEY) ?: ColorMode.DARK

        // Script which runs at load time that needs to be kept in sync with `initialColorMode` above. This code checks
        // if the user's local color mode preference is different from what was exported by Kobweb, replacing it if
        // different to prevent a flash of color after the page loads.
        if (AppGlobals.isExporting) {
            document.head!!.appendChild(
                document.createElement("script").apply {
                    textContent = """
                        {
                            const storedColor = localStorage.getItem('${COLOR_MODE_KEY.name}'); // 'LIGHT', 'DARK', or null
                            const desiredColor = storedColor ? `silk-${'$'}{storedColor.toLowerCase()}` : 'silk-dark';
                            const oppositeColor = desiredColor === 'silk-dark' ? 'silk-light' : 'silk-dark';
                            document.documentElement.classList.replace(oppositeColor, desiredColor);
                        }
                        """.trimIndent()
                })
        }

    }

    ctx.stylesheet.apply {
        registerStyleBase("body") {
            Modifier
                .fontFamily(
                    "-apple-system", "BlinkMacSystemFont", "Segoe UI", "Roboto", "Oxygen", "Ubuntu",
                    "Cantarell", "Fira Sans", "Droid Sans", "Helvetica Neue", "sans-serif"
                )
                .overflowWrap(OverflowWrap.BreakWord)
        }
        registerStyleBase("code, pre") {
            Modifier.fontFamily("SFMono-Regular", "Menlo", "Monaco", "Consolas", "Courier New", "monospace")
        }

        registerStyle("html") {
            base {
                Modifier.scrollPadding(top = 1.5.cssRem + NavHeaderHeight.value() + MobileNavHeight.value())
            }
            cssRule(CSSMediaQuery.MediaFeature("prefers-reduced-motion", StylePropertyValue("no-preference"))) {
                Modifier.scrollBehavior(ScrollBehavior.Smooth)
            }
        }
        layer(SilkLayer.BASE) {
            registerStyleBase("ul, ol, menu") {
                Modifier
                    .listStyle(ListStyleType.None)
                    .padding(0.px)
                    .margin(0.px)
            }
            registerStyleBase("table") {
                Modifier.borderCollapse(BorderCollapse.Collapse)
            }
            registerStyleBase("img, picture, video, canvas, svg") {
                Modifier
                    .display(DisplayStyle.Block)
                    .maxWidth(100.percent)
            }
        }

        val headerCommon = Modifier
            .fontWeight(FontWeight.SemiBold)
            .margin { top(1.5.cssRem); bottom(1.cssRem) }
            .lineHeight(1.2)

        registerStyle("h1") {
            base {
                headerCommon
                    .fontSize(2.25.cssRem)
                    .margin { bottom(1.5.cssRem) }
            }
            Breakpoint.MD {
                Modifier.fontSize(2.5.cssRem)
            }
        }

        registerStyle("h2") {
            base {
                headerCommon
                    .fontSize(1.625.cssRem)
                    .margin { top(2.2.cssRem) }
            }
            Breakpoint.MD {
                Modifier.fontSize(1.875.cssRem)
            }
        }

        registerStyleBase("h3") {
            headerCommon.fontSize(1.25.cssRem)
        }

        registerStyleBase("h4") {
            headerCommon
        }
    }
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    SilkApp {
        val colorMode = ColorMode.current
        LaunchedEffect(colorMode) {
            localStorage.setItem(COLOR_MODE_KEY, colorMode)
        }

        Surface(
            SmoothColorStyle.toModifier()
                .fillMaxWidth()
                .minHeight(100.vh)
                .setVariable(
                    DividerColor,
                    if (colorMode.isDark) Color.rgba(238, 238, 238, 0.2f) else Color.rgba(17, 17, 17, 0.2f)
                )
        ) {
            content()
        }
    }
}

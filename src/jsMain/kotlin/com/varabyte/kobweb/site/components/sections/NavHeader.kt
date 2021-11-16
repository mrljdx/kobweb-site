package com.varabyte.kobweb.site.components.sections

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.foundation.layout.*
import com.varabyte.kobweb.compose.ui.*
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.icons.fa.*
import com.varabyte.kobweb.silk.theme.SilkTheme
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.rememberColorMode
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import com.varabyte.kobweb.site.components.widgets.CustomButton
import org.jetbrains.compose.web.attributes.href
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Img

val NAV_ITEM_PADDING = Modifier.padding(0.px, 12.px)

@Composable
private fun HomeLogo() {
    A(
        attrs = {
            href("/")
        }
    ) {
        Box(
            Modifier.padding(4.px)
        ) {
            Img(
                "https://storage.googleapis.com/kobweb-example-cdn/logo.png",
                attrs = {
                    style {
                        height(32.px)
                    }
                }
            )
        }
    }
}

@Composable
private fun ThemeSwitch(color: Color) {
    var colorMode by rememberColorMode()

    Button(
        onClick = { colorMode = colorMode.opposite() },
        NAV_ITEM_PADDING.clip(Circle())
    ) {
        Box(Modifier.padding(8.px)) {
            when (colorMode) {
                ColorMode.DARK -> FaMoon(color = color)
                ColorMode.LIGHT -> FaSun(color = color)
            }
        }
    }
}

private fun getNavBoxShadow(colorMode: ColorMode): String {
    val colorStr = when (colorMode) {
        ColorMode.DARK -> "#111111"
        ColorMode.LIGHT -> "#eee"
    }
    return "0 0 0 0.1px $colorStr"
}

private fun getNavBackgroundColor(colorMode: ColorMode): CSSColorValue {
    return when (colorMode) {
        ColorMode.DARK -> rgba(255, 255, 255, .65)
        ColorMode.LIGHT -> rgba(1, 1, 1, .65)
    }
}

@Composable
fun NavHeader() {
    val buttonIconColor = SilkTheme.palette.color
    val colorMode by rememberColorMode()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(getNavBackgroundColor(colorMode))
            .styleModifier {
                position(Position.Sticky)
                top(0.percent)
                property("backdrop-filter", "saturate(180%) blur(5px)")
                property("box-shadow", getNavBoxShadow(colorMode))
            }
    ) {
        Row(
            Modifier.fillMaxWidth(70.percent).padding(1.em),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeLogo()
            Spacer()
            CustomButton("https://discord.gg/5NZ2GKV5Cs", text="", shape = "circle") {
                FaDiscord(color = buttonIconColor)
            }
            CustomButton("https://github.com/varabyte/kobweb", text="", shape = "circle") {
                FaGithub(color = buttonIconColor)
            }
            ThemeSwitch(buttonIconColor)
        }
    }
}
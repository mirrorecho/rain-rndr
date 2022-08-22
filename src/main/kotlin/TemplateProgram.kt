import kotlinx.coroutines.*
import org.openrndr.Program
import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.loadFont
import org.openrndr.draw.loadImage
import org.openrndr.draw.tint
import org.openrndr.extra.olive.oliveProgram
import org.openrndr.launch
import org.openrndr.math.Vector2
import rain.interfaces.*
import rain.language.*
import rain.patterns.*
import rain.machines.*
import rain.utils.cycleOf

import kotlin.math.cos
import kotlin.math.sin


fun main() = application {

        program {

            val animation = object : Animatable() {
                var x: Double = 0.0
            }

            extend {

                animation.updateAnimation()
                if (!animation.hasAnimations()) {
                    animation.apply {
                        ::x.animate(width.toDouble(), 1000, Easing.CubicInOut)
                        ::x.complete()
                        ::x.animate(0.0, 1000, Easing.CubicInOut)
                        ::x.complete()
                    }
                }
                drawer.fill = ColorRGBa.PINK
                drawer.stroke = null
                drawer.circle(animation.x, height / 2.0, 100.0)
            }
        }
    }

//fun main() = application {
//    configure {
//        width = 768
//        height = 576
//    }
//
////    program {
////        val animation = object : Animatable() {
////            var color = ColorRGBa.WHITE
////            var position = Vector2.ZERO
////        }
////        animation.apply {
////            ::color.animate(ColorRGBa.PINK, 5000)
////            ::position.animate(Vector2(width.toDouble(), height.toDouble()), 5000)
////        }
////    }
//
////    program {
////        val image = loadImage("data/images/pm5544.png")
////        val font = loadFont("data/fonts/default.otf", 64.0)
////
////        extend {
////            drawer.drawStyle.colorMatrix = tint(ColorRGBa.WHITE.shade(0.2))
////            drawer.image(image)
////
////            drawer.fill = ColorRGBa.PINK
////            drawer.circle(cos(seconds) * width / 2.0 + width / 2.0, sin(0.5 * seconds) * height / 2.0 + height / 2.0, 140.0)
////
////            drawer.fontMap = font
////            drawer.fill = ColorRGBa.WHITE
////            drawer.text("OPENRNDR", width / 2.0, height / 2.0)
////        }
////    }
//}

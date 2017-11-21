package org.ethtrader.ticker

import com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.awt.*
import java.awt.Font.BOLD
import java.awt.Font.PLAIN
import java.awt.RenderingHints.VALUE_ANTIALIAS_ON
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.CEILING
import java.math.RoundingMode.HALF_DOWN
import java.net.URL
import java.util.Locale.ENGLISH
import javax.imageio.ImageIO

data class DataPointDef(
        val dataPointName: String,
        val valueName: String,
        val prefix: String,
        val suffix: String,
        val precision: String,
        val useColor: String,
        val shorten: String,
        val divisor: String,
        val highlight: Boolean,
        val sortMultiplier: String?,
        val sortValue: String?,
        val fxName: String?,
        val fxValue: String?,
        val fxValueInv: Boolean?,
        val image: String?)

data class DataPoint(
        val text: String,
        val sortValue: BigDecimal,
        val color: Color,
        val highlight: Boolean,
        val width: Int,
        val image: BufferedImage? = null)

class TickerFactory(private val outputDir: File, private val loadResource: (String) -> URL) {

    val apiStart = "/** Generated by ticker bot [start of section] */"
    val apiEnd = "/** Generated by ticker bot [end of section] */"
    val objectMapper = ObjectMapper().registerKotlinModule().enable(ALLOW_UNQUOTED_FIELD_NAMES)

    private val css = StringBuilder()

    init {
        System.setProperty("java.awt.headless", "true")
        resetCss()
    }

    fun produceTicker(name: String, data: Map<String, Map<String, BigDecimal?>?>, config: TickerConf): InputStream {

        val font = Font(config.font, BOLD, config.fontHeight)
        val graphics = BufferedImage(1, 1, TYPE_INT_ARGB).graphics as Graphics2D
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
        graphics.font = font
        val fontMetrics = graphics.fontMetrics

        val dataPoints = config.segments.mapNotNull {
            try {
                parseSegment(data, config, it, fontMetrics)
            } catch (e: Exception) {
                System.err.println(e.message)
                e.printStackTrace()
                null
            }
        }
        val sorted = when (config.sort) {
            Sort.asc -> dataPoints.sortedBy { it.sortValue }
            Sort.desc -> dataPoints.sortedByDescending { it.sortValue }
            else -> dataPoints
        }
        sorted.forEach { println("${it.text} - ${it.sortValue}") }
        val filtered = if (config.maxItems != null) sorted.take(config.maxItems) else sorted
        var index = 0
        val images = filtered.map { it ->
            try {
                if (it.sortValue > ZERO) render(config, it, fontMetrics, index++) else render(config, it, fontMetrics, null)
            } catch (e: Exception) {
                System.err.println(e.message)
                e.printStackTrace()
                emptyList<BufferedImage>()
            }
        }.filterNotNull().flatten()
        val image = combineImages(images)
        val file = File(outputDir, "$name.png")
        ImageIO.write(image, "png", file)
        println("Ticker [$name] saved to [${file.absolutePath}]")
        addCss(name, config, image)
        return getInputStream(image)
    }

    private fun loadImage(config: TickerConf, location: String, highlight: Boolean, fontMetrics: FontMetrics): BufferedImage? {
        if (config.enableImages == false) return null
        val image = ImageIO.read(loadResource(location))
        val padding = 8
        val width = image.width + padding * 2
        val imageWithBackground = BufferedImage(width, fontMetrics.height + 4, TYPE_INT_ARGB)
        val graphics = imageWithBackground.graphics as Graphics2D
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
        graphics.color = if (highlight) config.backgroundHighlight else config.background
        graphics.fillRect(0, 0, width, fontMetrics.height + 4)
        graphics.drawImage(image, padding, (imageWithBackground.height - image.height) / 2, null)
        return imageWithBackground
    }

    private fun addCss(name: String, conf: TickerConf, image: BufferedImage) {
        val template = loadResource("/per-ticker.css").readText(Charsets.UTF_8)
                .replace("\${ticker.name}", name)
                .replace("\${ticker.width}", image.width.toString())
                .replace("\${ticker.top}", conf.top)
                .replace("\${ticker.scrollDuration}", conf.scrollDuration)
                .replace("\${css.selector}", conf.cssSelector)
        css.append("$template\n\n")
    }

    private fun combineImages(images: List<BufferedImage>): BufferedImage {
        var cumulative = 0
        val image = BufferedImage(images.sumBy { it.width }, images.first().height, TYPE_INT_ARGB)
        val graphics = image.graphics as Graphics2D
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
        images.forEach { image ->
            graphics.drawImage(image, cumulative, 0, null)
            cumulative += image.width
        }
        return image
    }

    private fun parseSegment(data: Map<String, Map<String, BigDecimal?>?>, config: TickerConf, it: String, fontMetrics: FontMetrics): DataPoint {
        try {
            val dataPoint = objectMapper.readValue<DataPointDef>(it)
            return format(data, dataPoint, config, fontMetrics)
        } catch(e: JsonProcessingException) {
            if (it.startsWith('{')) throw e
            return DataPoint(it, ZERO, config.color, false, fontMetrics.stringWidth(it))
        }
    }

    private fun render(config: TickerConf, segment: DataPoint, fontMetrics: FontMetrics, index: Int?): List<BufferedImage> {
        val rankSegment = if (config.showRank == true && index != null) "${index + 1}. " else ""
        val totalWidth = segment.width + fontMetrics.stringWidth(rankSegment)
        val totalWidthPlusJoiner = totalWidth + fontMetrics.stringWidth(config.joiner)
        val image = BufferedImage(totalWidthPlusJoiner, fontMetrics.height + 4, TYPE_INT_ARGB)
        val graphics = image.graphics as Graphics2D
        val highlight = segment.text.startsWith("!") || segment.highlight
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
        graphics.color = if (highlight) config.backgroundHighlight else config.background

        val weight = if (true) BOLD else PLAIN
        graphics.color = if (highlight) config.backgroundHighlight else config.background
        graphics.fillRect(0, 0, totalWidth, image.height)
        graphics.color = config.background
        graphics.fillRect(totalWidth, 0, totalWidthPlusJoiner, image.height)
        graphics.color = segment.color
        graphics.font = Font(config.font, weight, config.fontHeight)
        graphics.drawString("$rankSegment${segment.text.trimStart('!')}${config.joiner}", 0, fontMetrics.ascent + 2)
        return if (segment.image != null) listOf(segment.image, image) else listOf(image)
    }

    private fun format(data: Map<String, Map<String, BigDecimal?>?>, dataPoint: DataPointDef, config: TickerConf, fontMetrics: FontMetrics): DataPoint {
        return dataPoint.dataPointName.split('^').map { dataPointName ->
            val value = data[dataPointName]?.get(dataPoint.valueName)?.divide(BigDecimal(dataPoint.divisor))
            val sortValue = dataPoint.sortValue?.let { data[dataPointName]?.get(it)?.multiply(BigDecimal(dataPoint.sortMultiplier ?: "1")) }
            val fxValue = if (dataPoint.fxName != null && dataPoint.fxValue != null) data[dataPoint.fxName]?.get(dataPoint.fxValue) else null
            val fxValueInv = if (fxValue != null && dataPoint.fxValueInv == true) ONE.divide(fxValue, 100, CEILING) else fxValue
            if (value == null) {
                null
            } else {
                val valueAfterFx = if (fxValueInv != null) value * fxValueInv else value
                val sortValueAfterFx = if (fxValueInv != null && sortValue != null) sortValue * fxValueInv else sortValue
                val sortValueMultiplier = if (dataPoint.sortMultiplier != null && sortValueAfterFx != null) sortValueAfterFx * dataPoint.sortMultiplier.toBigDecimal() else sortValue
                val color = if (dataPoint.useColor == "Y") if (valueAfterFx >= ZERO) config.positiveColor else config.negativeColor else config.color
                val adjustedPrecision = valueAfterFx.setScale(dataPoint.precision.toInt(), HALF_DOWN)
                val shortenedValue = if (dataPoint.shorten == "Y") shorten(adjustedPrecision, "", "k", "M", "B", "T") else String.format(ENGLISH, "%,.${dataPoint.precision}f", adjustedPrecision)

                val text = "${dataPoint.prefix}$shortenedValue${dataPoint.suffix}"
                DataPoint(text, sortValueMultiplier ?: ZERO, color, dataPoint.highlight, fontMetrics.stringWidth(text), dataPoint.image?.let { loadImage(config, it, dataPoint.highlight, fontMetrics) })
            }
        }.filterNotNull().firstOrNull() ?: throw RuntimeException("ERROR - No value [${dataPoint.valueName}] or no data point [${dataPoint.dataPointName}] in [$data]")
    }

    private val OneThousand = BigDecimal("1000")
    private val TenThousand = BigDecimal("10000")

    private fun shorten(value: BigDecimal, vararg denomiators: String): String {
        return if (value < TenThousand) value.toPlainString() + denomiators.first()
        else shorten(value / OneThousand, *denomiators.drop(1).toTypedArray())
    }

    private fun getInputStream(upper: BufferedImage): InputStream {
        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(upper, "png", byteArrayOutputStream)
        return ByteArrayInputStream(byteArrayOutputStream.toByteArray())
    }

    fun getAndResetCss(existing: String): String {
        val existingLines = existing.lines()
        val newLines = css.lines()
        resetCss()
        val last = if (existingLines.contains(apiEnd)) existingLines.dropWhile { it != apiEnd } else listOf(apiEnd)
        val first = existingLines.takeWhile { it != apiStart }
        return (first + apiStart + "\n\n" + newLines + last).joinToString(System.lineSeparator())
    }

    private fun resetCss() {
        css.setLength(0)
        css.append(loadResource("/ticker.css").readText(Charsets.UTF_8)).append("\n\n")
    }
}

private fun String.toBigDecimal(): BigDecimal =
    BigDecimal(this)

operator fun <E> List<E>.component6(): Any = get(5)!!
operator fun <E> List<E>.component7(): Any = get(6)!!
operator fun <E> List<E>.component8(): Any = get(7)!!

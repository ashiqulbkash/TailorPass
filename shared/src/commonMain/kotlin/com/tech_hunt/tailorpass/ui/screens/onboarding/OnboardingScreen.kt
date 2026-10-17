package com.tech_hunt.tailorpass.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech_hunt.tailorpass.resources.Res
import com.tech_hunt.tailorpass.resources.action_next
import com.tech_hunt.tailorpass.resources.action_skip
import com.tech_hunt.tailorpass.resources.action_start
import com.tech_hunt.tailorpass.resources.ic_arrow_forward
import com.tech_hunt.tailorpass.resources.ic_check
import com.tech_hunt.tailorpass.resources.ic_edit
import com.tech_hunt.tailorpass.resources.ic_share
import com.tech_hunt.tailorpass.resources.illus_onb_how
import com.tech_hunt.tailorpass.resources.illus_onb_privacy
import com.tech_hunt.tailorpass.resources.illus_onb_share
import com.tech_hunt.tailorpass.resources.onb1_note
import com.tech_hunt.tailorpass.resources.onb1_step1
import com.tech_hunt.tailorpass.resources.onb1_step2
import com.tech_hunt.tailorpass.resources.onb1_step3
import com.tech_hunt.tailorpass.resources.onb1_title
import com.tech_hunt.tailorpass.resources.onb2_point1
import com.tech_hunt.tailorpass.resources.onb2_point2
import com.tech_hunt.tailorpass.resources.onb2_point3
import com.tech_hunt.tailorpass.resources.onb2_title
import com.tech_hunt.tailorpass.resources.onb3_point1
import com.tech_hunt.tailorpass.resources.onb3_point2
import com.tech_hunt.tailorpass.resources.onb3_point3
import com.tech_hunt.tailorpass.resources.onb3_title
import com.tech_hunt.tailorpass.resources.onb_in_app
import com.tech_hunt.tailorpass.resources.onb_page_of
import com.tech_hunt.tailorpass.resources.onb_pdf
import com.tech_hunt.tailorpass.resources.onb_tailor
import com.tech_hunt.tailorpass.resources.onb_welcome
import com.tech_hunt.tailorpass.resources.profile_height
import com.tech_hunt.tailorpass.resources.size_col_chest
import com.tech_hunt.tailorpass.resources.value_inch
import com.tech_hunt.tailorpass.ui.components.OnboardingBullet
import com.tech_hunt.tailorpass.ui.components.OnboardingPage
import com.tech_hunt.tailorpass.ui.components.PageDots
import com.tech_hunt.tailorpass.ui.components.PrimaryButton
import com.tech_hunt.tailorpass.ui.theme.Dimens
import com.tech_hunt.tailorpass.ui.theme.MaapTheme
import com.tech_hunt.tailorpass.ui.util.LANGUAGE_BN
import com.tech_hunt.tailorpass.ui.util.LocalAppLocale
import com.tech_hunt.tailorpass.util.formatInches
import com.tech_hunt.tailorpass.util.toBanglaDigits
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 1.2–1.4 Onboarding: three pages in a HorizontalPager. Next/swipe moves between pages (UI-only state);
 * Skip and Start both leave for profile creation.
 */
data class OnboardingState(val initialPage: Int = 0)

sealed interface OnboardingAction {
    data object Skip : OnboardingAction
    data object Start : OnboardingAction
}

private const val PAGE_COUNT = 3

@Composable
fun OnboardingScreen(state: OnboardingState, onAction: (OnboardingAction) -> Unit) {
    val pager = rememberPagerState(initialPage = state.initialPage) { PAGE_COUNT }
    val scope = rememberCoroutineScope()
    val useBangla = LocalAppLocale.current == LANGUAGE_BN
    val digits: (Int) -> String = { if (useBangla) it.toString().toBanglaDigits() else it.toString() }
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(horizontal = Dimens.space20, vertical = Dimens.space24),
    ) {
        HorizontalPager(
            state = pager,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            pageSpacing = Dimens.space20,
            verticalAlignment = Alignment.Top,
        ) { page ->
            when (page) {
                0 -> HowItWorksPage(digits)
                1 -> PrivacyPage()
                else -> SharePage()
            }
        }
        Spacer(Modifier.height(Dimens.space16))
        PageDots(
            count = PAGE_COUNT,
            current = pager.currentPage,
            contentDescription = stringResource(Res.string.onb_page_of, digits(pager.currentPage + 1), digits(PAGE_COUNT)),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(18.dp))
        if (pager.currentPage < PAGE_COUNT - 1) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.space12)) {
                TextButton(
                    onClick = { onAction(OnboardingAction.Skip) },
                    modifier = Modifier.height(Dimens.buttonHeight),
                    contentPadding = PaddingValues(horizontal = 22.dp),
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant),
                ) {
                    Text(
                        text = stringResource(Res.string.action_skip),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                PrimaryButton(
                    text = stringResource(Res.string.action_next),
                    onClick = { scope.launch { pager.animateScrollToPage(pager.currentPage + 1) } },
                    modifier = Modifier.weight(1f),
                    trailingIcon = painterResource(Res.drawable.ic_arrow_forward),
                )
            }
        } else {
            PrimaryButton(
                text = stringResource(Res.string.action_start),
                onClick = { onAction(OnboardingAction.Start) },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = painterResource(Res.drawable.ic_arrow_forward),
            )
        }
    }
}

@Composable
private fun HowItWorksPage(digits: (Int) -> String) {
    val useBangla = LocalAppLocale.current == LANGUAGE_BN
    OnboardingPage(
        title = stringResource(Res.string.onb1_title),
        eyebrow = stringResource(Res.string.onb_welcome),
        illustration = {
            Illustration(Res.drawable.illus_onb_how, 250.dp, 240.dp) {
                IllustrationLabel(
                    text = stringResource(Res.string.size_col_chest) + " " +
                        stringResource(Res.string.value_inch, formatInches(40f, useBangla)),
                    x = 203.dp, baseline = 97.dp, fontSize = 20.sp, weight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxWidth = 62.dp,
                )
                IllustrationLabel(
                    text = stringResource(Res.string.profile_height),
                    x = 30.dp, baseline = 226.dp, fontSize = 15.sp, weight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
    ) {
        listOf(Res.string.onb1_step1, Res.string.onb1_step2, Res.string.onb1_step3).forEachIndexed { i, step ->
            OnboardingBullet(stringResource(step), markerColor = MaterialTheme.colorScheme.primary) {
                Text(
                    text = digits(i + 1),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Text(
            text = stringResource(Res.string.onb1_note),
            modifier = Modifier.padding(top = 2.dp),
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun PrivacyPage() {
    OnboardingPage(
        title = stringResource(Res.string.onb2_title),
        illustration = { Illustration(Res.drawable.illus_onb_privacy, 260.dp, 230.dp) {} },
    ) {
        listOf(Res.string.onb2_point1, Res.string.onb2_point2, Res.string.onb2_point3).forEach {
            CheckBullet(it)
        }
    }
}

@Composable
private fun SharePage() {
    OnboardingPage(
        title = stringResource(Res.string.onb3_title),
        illustration = {
            Illustration(Res.drawable.illus_onb_share, 280.dp, 220.dp) {
                IllustrationLabel(
                    text = stringResource(Res.string.onb_pdf),
                    x = 56.dp, baseline = 78.dp, fontSize = 13.sp, weight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                IllustrationLabel(
                    text = stringResource(Res.string.onb_tailor),
                    x = 222.dp, baseline = 164.dp, fontSize = 16.sp, weight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                IllustrationLabel(
                    text = stringResource(Res.string.onb_in_app),
                    x = 54.dp, baseline = 201.dp, fontSize = 13.sp, weight = FontWeight.SemiBold,
                    color = MaapTheme.colors.onSuccessContainer,
                    // Starts after the check mark: Hind Siliguri is wider than the design render, centred it would overlap.
                    anchorStart = true,
                    maxWidth = 60.dp,
                )
            }
        },
    ) {
        IconBullet(Res.string.onb3_point1, Res.drawable.ic_share)
        IconBullet(Res.string.onb3_point2, Res.drawable.ic_edit)
        CheckBullet(Res.string.onb3_point3)
    }
}

/** Green check marker (privacy points, "stays in the app"). */
@Composable
private fun CheckBullet(text: StringResource) {
    OnboardingBullet(stringResource(text), markerColor = MaapTheme.colors.successContainer) {
        Icon(
            painter = painterResource(Res.drawable.ic_check),
            contentDescription = null,
            tint = MaapTheme.colors.onSuccessContainer,
            modifier = Modifier.size(18.dp),
        )
    }
}

/** Blue icon marker (share, note). */
@Composable
private fun IconBullet(text: StringResource, icon: DrawableResource) {
    OnboardingBullet(stringResource(text), markerColor = MaterialTheme.colorScheme.primaryContainer) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp),
        )
    }
}

/** Illustration artwork at its design size; labels are placed on top in the artwork's own dp coordinates. */
@Composable
private fun Illustration(
    image: DrawableResource,
    width: Dp,
    height: Dp,
    labels: @Composable () -> Unit,
) {
    Box(Modifier.size(width, height)) {
        Image(painterResource(image), contentDescription = null, modifier = Modifier.size(width, height))
        labels()
    }
}

/**
 * Text centred on x with its first baseline on `baseline` (same as SVG text-anchor="middle").
 * anchorStart = true starts the text at x instead (text-anchor="start").
 * maxWidth: the text shrinks (down to 11sp) to fit, e.g. the longer English "Chest 40"" in its box.
 */
@Composable
private fun IllustrationLabel(
    text: String,
    x: Dp,
    baseline: Dp,
    fontSize: TextUnit,
    weight: FontWeight,
    color: Color,
    anchorStart: Boolean = false,
    maxWidth: Dp? = null,
) {
    BasicText(
        text = text,
        style = TextStyle(
            fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
            fontSize = fontSize,
            fontWeight = weight,
            color = color,
        ),
        maxLines = 1,
        softWrap = false,
        autoSize = if (maxWidth != null) TextAutoSize.StepBased(minFontSize = 11.sp, maxFontSize = fontSize) else null,
        modifier = Modifier.layout { measurable, constraints ->
            val placeable = measurable.measure(
                if (maxWidth != null) Constraints(maxWidth = maxWidth.roundToPx()) else Constraints(),
            )
            layout(constraints.maxWidth, constraints.maxHeight) {
                placeable.place(
                    x = x.roundToPx() - if (anchorStart) 0 else placeable.width / 2,
                    y = baseline.roundToPx() - placeable[FirstBaseline],
                )
            }
        },
    )
}

@Preview
@Composable
private fun OnboardingPage1Preview() {
    MaapTheme(darkTheme = false) { OnboardingScreen(OnboardingState(initialPage = 0)) {} }
}

@Preview
@Composable
private fun OnboardingPage2Preview() {
    MaapTheme(darkTheme = false) { OnboardingScreen(OnboardingState(initialPage = 1)) {} }
}

@Preview
@Composable
private fun OnboardingPage3Preview() {
    MaapTheme(darkTheme = false) { OnboardingScreen(OnboardingState(initialPage = 2)) {} }
}

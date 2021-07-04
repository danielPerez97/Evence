package daniel.perez.licensesview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dagger.hilt.android.AndroidEntryPoint
import daniel.perez.core.compose.Header
import daniel.perez.licensesview.data.License
import daniel.perez.core.compose.EvenceTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

@ExperimentalAnimationApi
@AndroidEntryPoint
class LicensesActivity : AppCompatActivity()
{
    @Inject
    lateinit var retriever: LicenseRetriever

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val dataLambda: () -> Flow<List<License>> = { flow { emit(retriever.getLicenses()) } }
        setContent {
            EvenceTheme {
                LicensesScreen(
                    dataLambda,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW)
                        val licenseUrl: String
                        with(it)
                        {
                            licenseUrl = when {
                                spdxLicenses != null -> {
                                    spdxLicenses.first().url
                                }
                                unknownLicenses != null -> {
                                    unknownLicenses.first().url
                                }
                                else -> {
                                    throw Exception("No License found")
                                }
                            }
                        }

                        intent.data = Uri.parse(licenseUrl)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}

@ExperimentalFoundationApi
@ExperimentalAnimationApi
@Composable
fun LicensesScreen(
    licensesData: () -> Flow<List<License>>,
    onClick: (License) -> Unit
)
{
    val licenses = licensesData().collectAsState(initial = emptyList())
    val listState = rememberLazyListState()
    val showList = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    val density = LocalDensity.current

        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                bottomBar = {
                    TopAppBar(
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("Open Source Licenses",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier.padding(it)
                ) {
//                    AnimatedVisibility(
//                        visible = showList.value,
//                        enter = slideInHorizontally(initialOffsetX = { with(density) { -40.dp.roundToPx() } })
//                                + expandVertically(expandFrom = Alignment.Top)
//                                + fadeIn(initialAlpha = 0.3f),
//                        exit = slideOutHorizontally() + shrinkVertically() + fadeOut()
//                    ) {
//                        MessageCard()
//                    }

                    LicensesList(
                        licenses = licenses.value,
//                        modifier = Modifier.padding(top = 12.dp),
                        onClick = onClick,
                        listState = listState
                    )
                }
            }
        }
}

@Composable
fun MessageCard()
{
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Evence Loves Open Source",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "This application is built with the following open source projects.",
            modifier = Modifier.fillMaxWidth(0.8f),
            maxLines = 2,
            overflow = TextOverflow.Visible
        )
    }
}

@ExperimentalFoundationApi
@Composable
fun LicensesList(
    licenses: List<License>,
    modifier: Modifier = Modifier,
    onClick: (License) -> Unit,
    listState: LazyListState
)
{
    LazyColumn(state = listState, modifier = modifier) {
        val grouped = licenses.sortedBy { it.artifactId }.groupBy { it.artifactId[0].uppercase() }

        grouped.forEach { initial, licenses  ->
            stickyHeader {
                Header(text = initial, modifier = Modifier.fillParentMaxWidth()
                    .height(30.dp)
                    .background(MaterialTheme.colors.primary)
                )
            }

            items(licenses) { license ->
                LicenseItem(license = license, onClick = onClick)
            }
        }
    }
}

@Preview
@Composable
fun PreviewLicenseItem()
{
    LicenseItem(license = License("daniel.perez", "test", "1.0"))
}

@Composable
fun LicenseItem(
    license: License,
    onClick: ((License) -> Unit)? = null
)
{
    Card(
        shape = RoundedCornerShape(3.dp),
        elevation = 12.dp,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp)),
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable { onClick?.invoke(license) }
                .padding(8.dp)
        ) {
            val (projectName, licenseConstraint) = createRefs()

            Text(
                text = license.artifactId.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                },
                fontSize = 18.sp,
                modifier = Modifier
                    .constrainAs(projectName) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)

                        width = Dimension.fillToConstraints

                    },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
            )

            Text(
                text = when {
                    license.spdxLicenses?.first()?.name != null -> license.spdxLicenses.first().name
                    license.unknownLicenses?.first()?.name != null -> license.unknownLicenses.first().name
                    else -> {
                        throw Exception("Could not find license")
                    }
                },
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier
                    .constrainAs(licenseConstraint) {
                        start.linkTo(parent.start)
                        top.linkTo(projectName.bottom)

                        width = Dimension.fillToConstraints
                    },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
            )
        }
    }
}
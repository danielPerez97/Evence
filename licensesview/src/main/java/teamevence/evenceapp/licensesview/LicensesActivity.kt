package teamevence.evenceapp.licensesview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import dagger.hilt.android.AndroidEntryPoint
import teamevence.evenceapp.licensesview.data.License
import teamevence.evenceapp.core.compose.EvenceTheme
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
                                scm != null -> {
                                    scm.url
                                }
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

        Surface {
            Scaffold(
                bottomBar = {
                    BottomAppBar(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                    ) {
                        Text("Open Source Licenses",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                                color = Color.White
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier.padding(it)
                ) {

                    LicensesList(
                        licenses = licenses.value,
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
                Row(
                  modifier = Modifier.fillMaxWidth()
                          .background(MaterialTheme.colors.background)
                ) {
                    Spacer(modifier = Modifier.padding(PaddingValues(start = 16.dp)))
                    Text(text = initial)
                }

//                Header(text = initial, modifier = Modifier
//                        .fillParentMaxWidth()
//                        .height(30.dp)
//                        .background(MaterialTheme.colors.primary)
//                        .padding(vertical = 30.dp)
//                )
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
    LicenseItem(license = License("teamevence.evenceapp", "test", "1.0"))
}

@Composable
fun LicenseItem(
    license: License,
    onClick: ((License) -> Unit)? = null,
    dark: Boolean = isSystemInDarkTheme()
)
{
    val textColor = remember { mutableStateOf(
            if(dark)
                Color.White
            else
                Color.Black
    ) }
    Card(
        shape = RoundedCornerShape(3.dp),
        elevation = 8.dp,
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
                    color = textColor.value,
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
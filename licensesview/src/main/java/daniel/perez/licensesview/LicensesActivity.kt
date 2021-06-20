package daniel.perez.licensesview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import daniel.perez.licensesview.ui.theme.EvenceTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class LicensesActivity : ComponentActivity()
{
    @Inject
    lateinit var retriever: LicenseRetriever

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        val dataLambda: () -> Flow<List<License>> = { flow { emit(retriever.getLicenses()) } }
        setContent {
            LicensesScreen(
                dataLambda,
                onClick = {

                }
            )
        }
    }
}

@Composable
fun LicensesScreen(
    licensesData: () -> Flow<List<License>>,
    onClick: (License) -> Unit
)
{
    val licenses = licensesData().collectAsState(initial = emptyList())

    EvenceTheme {
        Surface(color = MaterialTheme.colors.background) {
            Scaffold(
                topBar = { Header(text = "Evence") }
            ) {
                Column(
                    modifier = Modifier.padding(it)
                ) {
                    MessageCard()
                    LicensesList(
                        licenses = licenses.value,
                        modifier = Modifier.padding(top = 12.dp),
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun LicensesScreenPreview()
{
    LicensesScreen(
        licensesData = {  return@LicensesScreen flow { emit(emptyList()) } },
        onClick = {  }
    )
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

@Composable
fun LicensesList(
    licenses: List<License>,
    modifier: Modifier = Modifier,
    onClick: (License) -> Unit
)
{
    LazyColumn(modifier = modifier) {
        items(licenses) { license ->
            LicenseItem(license = license, onClick = onClick)
            Divider()
        }
    }
}

@Composable
fun LicenseItem(
    license: License,
    onClick: (License) -> Unit
)
{
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { onClick(license) },
    ) {
        val (projectName, licenseConstraint) = createRefs()
        createHorizontalChain(projectName, licenseConstraint)

        Text(
            text = license.artifactId.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
            modifier = Modifier.constrainAs(projectName) {
                start.linkTo(parent.start, margin = 12.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)

                width = Dimension.percent(0.6f)
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
        )

        Text(
            text = when {
                license.spdxLicenses?.first()?.name != null -> license.spdxLicenses.first().name
                license.unknownLicenses?.first()?.name != null -> license.unknownLicenses.first().name
                else -> { throw Exception("Could not find license") }
            },
            modifier = Modifier.constrainAs(licenseConstraint) {
                end.linkTo(parent.end, margin = 12.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)

                width = Dimension.percent(0.4f)
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End,
        )
    }
}
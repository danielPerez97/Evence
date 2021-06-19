package daniel.perez.generateqrview.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import daniel.perez.core.BaseActivity
import daniel.perez.core.compose.Footer
import daniel.perez.core.compose.Header
import daniel.perez.core.compose.evenceBlue

class NewEventActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ComposeFormView()
            }
        }
    }
}

@Composable
@Preview
fun ComposeFormView() {
    ConstraintLayout(Modifier.background(Color.White).fillMaxSize()) {
        val (header, body, footer) = createRefs()
        Header( "Create an Event!", modifier = Modifier.constrainAs(header) {
            top.linkTo(parent.top)
        })
        Body( modifier = Modifier.constrainAs(body) {
            top.linkTo(header.bottom)
        } )
        Footer( "Generate QR Code", modifier = Modifier.constrainAs(footer) {
            bottom.linkTo(parent.bottom)
        } )
    }
}


@Composable
fun Body( modifier: Modifier = Modifier )
{
    Forms(modifier)
}

@Composable
fun Forms( modifier: Modifier = Modifier ) {
    val (title, setTitle) = rememberSaveable { mutableStateOf("") }
    val (startDate, setStartDate) = rememberSaveable { mutableStateOf("") }
    val (startTime, setStartTime) = rememberSaveable { mutableStateOf("") }
    val (endDate, setEndDate) = rememberSaveable { mutableStateOf("") }
    val (endTime, setEndTime) = rememberSaveable { mutableStateOf("") }
    val (repeating, setRepeating) = rememberSaveable { mutableStateOf("") }
    val (location, setLocation) = rememberSaveable { mutableStateOf("") }

    Card(modifier = modifier.shadow(15.dp)
        .padding(10.dp)
        .background(shape = RoundedCornerShape(30.dp), color = Color.White)
    ) {
        Column(modifier = Modifier.padding(10.dp).background(color = Color.Transparent)) {
            Text("Title", color = evenceBlue, modifier = Modifier.fillMaxWidth())
            EvenceTextField( textChanges = setTitle )

            Spacer(modifier = Modifier.height(20.dp))
            Text("Start date", color = evenceBlue)
            Row {
                EvenceTextField(initialText = "Select Date", textChanges = setStartDate)
                Spacer(Modifier.padding(3.dp))
                EvenceTextField(initialText = "Select Time", textChanges = setStartTime)
            }

            Text("End date", color = evenceBlue)
            Row {
                EvenceTextField(initialText = "Select Date", textChanges = setEndDate)
                Spacer(Modifier.padding(3.dp))
                EvenceTextField(initialText = "Select Time", textChanges = setEndTime)
            }
            Spacer(modifier = Modifier.height(20.dp))

            Text("Repeat", color = evenceBlue)
            EvenceTextField( textChanges = setRepeating )

            Text("Location", color = evenceBlue )
            EvenceTextField( textChanges = setLocation )
        }
    }
}

@Composable
fun EvenceTextField(
    modifier: Modifier = Modifier,
    initialText: String = "",
    textChanges: (String) -> Unit,
) {
    val (text, setText) = rememberSaveable { mutableStateOf("") }

    BoxWithConstraints(
        modifier = modifier.width(180.dp).height(50.dp)
    ) {
        TextField(
            value = text,
            label = { Text(initialText) },
            onValueChange = { setText(it); textChanges(it) },
            modifier = Modifier
                .background(
                    shape = CircleShape,
                    color = Color.White
                )
                .shadow(40.dp),
            shape = CircleShape,
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Gray,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

}